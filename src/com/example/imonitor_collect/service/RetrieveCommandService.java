package com.example.imonitor_collect.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.imonitor_collect.net.NetThread;
import com.example.imonitor_collect.util.NetStateUtil;

public class RetrieveCommandService extends IntentService {
	private ServerSocket ss;
	private boolean isListened;
	public RetrieveCommandService() {
		super("RetrieveCommandService");
		try {
			ss = new ServerSocket(9999);
			isListened = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Socket fromServer;
		NetStateUtil netUtil = new NetStateUtil(this);
		if(netUtil.isNetworkConnected()){
			while(isListened){
				try {
					fromServer = ss.accept();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(fromServer.getInputStream()));
					String[] msg = reader.readLine().split("##");
					if(msg[0].equals("SERVER")){
						int command = Integer.parseInt(msg[1]);
						
						if(command == NetThread.START_TRANSFORM_VIDEO){
							String[] args = msg[2].split("\\$");
							int collectionid = Integer.parseInt(args[0]);
							int accountid = Integer.parseInt(args[1]);
							
							Bundle bundle = intent.getExtras();
							Messenger messenger = (Messenger)bundle.get("MainActivity");
							Message message = Message.obtain();
							message.what = 1;
							message.arg1 = collectionid;
							message.arg2 = accountid;
							messenger.send(message);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
