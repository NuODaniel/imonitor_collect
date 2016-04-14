package com.example.imonitor_collect.net.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor_collect.net.NetThread;

/**
 * Thread to connect or disconnect to server
 * @author Administrator
 *
 */
public class ConnectThread extends NetThread {
	Handler connectHandler;
	public ConnectThread(String msg,String serverUrl,int serverPort, Handler handler){
		super(msg, serverUrl, serverPort);
		connectHandler = handler;
	}
	public void run(){
		//ÊµÀý»¯Socket  
        try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(message);
			String tempmsg = bReader.readLine();
			int result = 0;
			if(tempmsg!=null){
				String[] fromServerMessage = tempmsg.split("##");
				if(fromServerMessage[0].equals("SERVER")){
					if(fromServerMessage[1].equals(CONNECTING_TO_SERVER)){
						if(fromServerMessage[2].equals("SUCCESS"))
							result = 1;
					}
				}
			}
			Message msg = connectHandler.obtainMessage();
			msg.arg1 = result;
			connectHandler.sendMessage(msg);
			out.flush();
			out.close();
			socket.close();
			
			if(msg.arg1 == 1)
				connectHandler.removeCallbacks(this);

		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}  
	}
}