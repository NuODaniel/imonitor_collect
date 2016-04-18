package com.example.imonitor_collect.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.imonitor_collect.device.CollectionDevice;
import com.example.imonitor_collect.net.NetThread;
import com.example.imonitor_collect.net.thread.ConnectThread;
import com.example.imonitor_collect.util.NetStateUtil;

public class CheckNetStateService extends IntentService {

	private String mServerUrl = "192.168.253.1";
	private int mServerPort = 6789;
	NetStateUtil netUtil = new NetStateUtil(this); 
	public CheckNetStateService() {
		super("CheckNetStateService");
	}
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {  
    	  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {  
            	 
                if(netUtil != null && netUtil.isNetworkConnected()) {  
                    //String name = info.getTypeName();  
                    //Log.d(tag, "当前网络名称：" + name);  
                    //doSomething()  
                	
                } else {  
                	// Log.d(tag, "没有可用网络");  
                	//doSomething()  
                }  
            }  
        }  
    };  
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		CollectionDevice device = (CollectionDevice) bundle.get("cdevice");
		Messenger messenger = (Messenger)bundle.get("MainActivity");
		boolean flag = true;
		long timeStart = System.currentTimeMillis();
		long timeNow = System.currentTimeMillis();
		while(true){
			timeNow = System.currentTimeMillis();
			if(netUtil.isNetworkConnected()){
				if(timeNow - timeStart>100000 || flag){
					flag = false;
					timeStart = timeNow;
					ConnectThread connectThread = new ConnectThread("COLLECTION##"+NetThread.CONNECTING_TO_SERVER+"##"+
							bundle.getString("cid")+"$"+
							bundle.getString("devicename")+"$"+
							bundle.getString("username")+"$"+
							bundle.getString("password"), mServerUrl, mServerPort,
							messenger);	
					//start the thread using handlder, get return value update the stateText;
					new Thread(connectThread).start();;
				}
			}else{
				Message msg = Message.obtain();
				msg.arg1 = 0;
				try {
					messenger.send(msg);
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}	

}
