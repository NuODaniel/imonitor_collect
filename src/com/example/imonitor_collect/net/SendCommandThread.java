package com.example.imonitor_collect.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.util.ByteArrayBuffer;

import com.example.imonitor_collect.device.*;

public class SendCommandThread implements Runnable{
	private String mCommand;
	private String mServerUrl;
	private int mServerPort;
	/**
	 * client send these orders to server
	 */
	public static final String CONNECTING_TO_SERVER = "##collection$connect";
	public static final String DISCONNECTING = "##collection&disconnect";
	
	/**
	 * 
	 * @param commond
	 * @param serverUrl
	 * @param serverPort
	 */
	public SendCommandThread(String command,String serverUrl,int serverPort){
		this.mCommand = command;
		this.mServerPort = serverPort;
		this.mServerUrl = serverUrl;
	}
	public void run(){
		//ÊµÀý»¯Socket  
        try {
			Socket socket=new Socket(mServerUrl,mServerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(mCommand);
			
			out.flush();
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}  
	}
}