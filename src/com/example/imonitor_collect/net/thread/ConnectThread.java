package com.example.imonitor_collect.net.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.example.imonitor_collect.net.NetThread;

/**
 * Thread to connect or disconnect to server
 * @author Administrator
 *
 */
public class ConnectThread extends NetThread {
	Messenger connectMessenger;
	public ConnectThread(String msg,String serverUrl,int serverPort, Messenger messenger){
		super(msg, serverUrl, serverPort);
		connectMessenger = messenger;
	}
	public void run(){
		int result = 0;
		Message msg = Message.obtain();
		
		//ʵ����Socket  
        try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(message);
			out.flush();
			String tempmsg = bReader.readLine();
			result = 0;
			if(tempmsg!=null){
				String[] fromServerMessage = tempmsg.split("##");
				if(fromServerMessage[0].equals("COLLECTION")){
					if(Integer.parseInt(fromServerMessage[1])==CONNECTING_TO_SERVER){
						if(fromServerMessage[2].equals("SUCCESS"))
							result = 1;
						else
							result = 0;
					}
				}
			}
			
			out.close();
			socket.close();
			
			
		} catch (UnknownHostException e) {
			result = 0;
		} catch (IOException e) {
			result = 0;
		}
        finally{
        	msg.arg1 = result;
			try {
				connectMessenger.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}