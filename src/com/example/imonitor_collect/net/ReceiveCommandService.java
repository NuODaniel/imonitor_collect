package com.example.imonitor_collect.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ReceiveCommandService extends Service{
	private String mCommand;
	private String mServerUrl;
	private int mServerPort;
	private ServerSocket mServerSocket;
	private Socket mSocket;
	private OutputStream inputStream;
	private SocketAcceptThread socketAcceptThread;
	private SocketReceiver mSocketReceiver;

	public static final String NET_ACTION = "com.example.imonitor_collect.net";  
	public static final String SOCKER_RCV = "com.example.imonitor_collect.net.socketreceiver";  
	/**
	 * server send these orders to client 
	 */
	public static final String TRANSFORM_VIDEO_DATA = "##server$transformdata";
	public static final String SUCCESS = "##collection&disconnect";
	
	
	/**
	 * 
	 * @param commond
	 * @param serverUrl
	 * @param serverPort
	 */
	public ReceiveCommandService(String serverUrl,int serverPort){
		this.mServerPort = serverPort;
		this.mServerUrl = serverUrl;
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override  
	public void onCreate() {  
		super.onCreate();   
		mSocketReceiver= new SocketReceiver(); 
		
		
		IntentFilter filter = new IntentFilter();  
		filter.addAction(NET_ACTION);  
		registerReceiver(mSocketReceiver, filter);  
		socketAcceptThread = new SocketAcceptThread();  
		// ���� Socket �����߳�  
		socketAcceptThread.start();  
	}  
	private class SocketAcceptThread extends Thread{  
	   @Override  
	   public void run()  
	   {  
	     Log.d("service", "socket service - SocketAcceptThread::run");  
		   try {  
		       // ʵ����ServerSocket�������ö˿ں�Ϊ 9999
		       mServerSocket = new ServerSocket(9999);  
		   } catch (IOException e) {  
		       // TODO Auto-generated catch block  
		       e.printStackTrace();  
		   }  

		   try {  
		       // �ȴ��������˵���Ϣ  
		       mSocket = mServerSocket.accept();  
		   } catch (IOException e) {  
		       // TODO Auto-generated catch block  
		       e.printStackTrace();  
		   }  
  
  
//           socketReceiveThread = new SocketReceiveThread(clientSocket);  
//           stop = false;  
//           // ���������߳�  
//           socketReceiveThread.start();  
//  
//  
//             Intent sendIntent = new Intent(SOCKER_RCV);  
//             sendIntent.putExtra("action", "ClientIP");  
//             sendIntent.putExtra("content", clientSocket.getInetAddress().getHostAddress());  
//             // ���͹㲥������Activity����е�BroadcastReceiver���յ�  
//             sendBroadcast(sendIntent);  
       }  
	}
}