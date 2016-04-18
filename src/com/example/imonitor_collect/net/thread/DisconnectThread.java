package com.example.imonitor_collect.net.thread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;

import com.example.imonitor_collect.device.CollectionDevice;
import com.example.imonitor_collect.net.NetThread;
import com.example.imonitor_collect.util.CollectionDeviceInfoUtil;

/**
 * Thread to connect or disconnect to server
 * @author Administrator
 *
 */
public class DisconnectThread extends NetThread {
	private Context context;
	
	public DisconnectThread(Context c){
		super();
		context = c;
	}
	public void run(){
		int result = 0;
		Message msg = Message.obtain();
		
		Socket socket;
		try {
			socket = new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			CollectionDeviceInfoUtil cdUtil = new CollectionDeviceInfoUtil(context);
			CollectionDevice cd  = cdUtil.getCollectionDeviceFromPre();
			message = "COLLECTION##"+NetThread.DISCONNECTING+"##"+cd.getCid();
			out.println(message);
			out.flush();
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
	}
}