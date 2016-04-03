package com.example.imonitor_collect.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SocketReceiver extends BroadcastReceiver {

	public static final String NET_ACTION = "com.example.imonitor_collect.net";  
	public static final String SOCKER_RCV = "com.example.imonitor_collect.net.socketreceiver"; 
	@Override
	public void onReceive(Context context, Intent intent) {
		 String action = intent.getAction();  
         if(action.equals(NET_ACTION)) {  
           String sub_action = intent.getExtras().getString("ACTION");  
           if(sub_action.equals("reconnect")) {  
             Log.d("service", "socket service: reconnect.");  

//
//              socketAcceptThread = new SocketAcceptThread();  
//               // ¿ªÆô Socket ¼àÌýÏß³Ì  
//               socketAcceptThread.start();  
           }  
         }  
	}

}
