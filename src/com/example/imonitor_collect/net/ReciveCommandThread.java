package com.example.imonitor_collect.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.util.ByteArrayBuffer;

import com.example.imonitor_collect.device.*;

public class ReciveCommandThread implements Runnable{
	private String mCommand;
	private String mServerUrl;
	private int mServerPort;
	
	/**
	 * server send these orders to client 
	 */
	public static final String TRANSFORM_VIDEO_DATA = "##server$transformdata";
	public static final String SUCCESS = "##collection&disconnect";
	
	private OutputStream inputStream;
	/**
	 * 
	 * @param commond
	 * @param serverUrl
	 * @param serverPort
	 */
	public ReciveCommandThread(String serverUrl,int serverPort){
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