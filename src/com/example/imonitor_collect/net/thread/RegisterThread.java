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

public class RegisterThread extends NetThread{
	Handler registerHandler = null;
	public RegisterThread(String msg,String serverUrl, int serverPort, Handler handler) {
		super(msg, serverUrl, serverPort);
		registerHandler = handler;
	}
	
	@Override
	public void run(){
		//ÊµÀý»¯Socket  
        try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println(message);
			out.flush();
			
			String tempmsg = bReader.readLine();
			int result = 0;
			if(tempmsg!=null){
				String[] fromServerMessage = tempmsg.split("##");
				if(fromServerMessage[0].equals("SERVER")){
					if(fromServerMessage[1].equals(REGISTER_COLLECTION)){
						if(fromServerMessage[2].equals("SUCCESS"))
							result = 1;
					}
				}
			}
			Message msg = registerHandler.obtainMessage();
			msg.arg1 = result;
			registerHandler.sendMessage(msg);
			
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}  
	}
}