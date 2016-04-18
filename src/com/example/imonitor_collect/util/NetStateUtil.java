package com.example.imonitor_collect.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetStateUtil {
	private Context context;
	/**
     * ��������Ƿ����
     * @return
     */
	public NetStateUtil(Context context){
		this.context = context;
	}

    public boolean isNetworkConnected() { 
    	if (context != null) { 
    	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
    	.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
    	if (mNetworkInfo != null) { 
    	return mNetworkInfo.isAvailable(); 
    	} 
    	} 
    	return false; 
    	}
    /**
     * ��ȡ��ǰ��������
     * @return 0��û������   1��WIFI����   2��WAP����    3��NET����
     */
    
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }        
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!extraInfo.isEmpty() && !extraInfo.equals(null)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
    /** 
     * ��ip��������ʽת����ip��ʽ 
     *  
     * @param ipInt 
     * @return 
     */  
    public static String int2ip(int ipInt) {  
        StringBuilder sb = new StringBuilder();  
        sb.append(ipInt & 0xFF).append(".");  
        sb.append((ipInt >> 8) & 0xFF).append(".");  
        sb.append((ipInt >> 16) & 0xFF).append(".");  
        sb.append((ipInt >> 24) & 0xFF);  
        return sb.toString();  
    }  
    public static String getLocalIpAddress(Context context,int netType) {  
        try {  
            switch (netType) {
			case NETTYPE_WIFI:;
				WifiManager wifiManager = (WifiManager) context  
	                    .getSystemService(Context.WIFI_SERVICE);  
	            WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
	            int i = wifiInfo.getIpAddress();  
	            return int2ip(i);  
				
			case NETTYPE_CMWAP: 
			case NETTYPE_CMNET: 
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
	            {  
	               NetworkInterface intf = en.nextElement();  
	               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
	               {  
	                   InetAddress inetAddress = enumIpAddr.nextElement();  
	                   if (!inetAddress.isLoopbackAddress())  
	                   {  
	                       return inetAddress.getHostAddress().toString();  
	                   }  
	               }  
	           } 
				break;
			default:
				break;
			}
        } catch (Exception ex) {  
            Log.d("NetUtil", ex.getMessage());  
        }
		return null;  
    }
}
