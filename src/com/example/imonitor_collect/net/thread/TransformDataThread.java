package com.example.imonitor_collect.net.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.imonitor_collect.net.NetThread;
import com.example.imonitor_collect.util.CompareUtil;

public class TransformDataThread extends NetThread{
	private byte[] tempData = null;
	private byte[] previousData = null;
	private boolean isTransform = false;
	private int mWidth;
	private int mHeight;;
	public TransformDataThread(byte[] data,int collectionid,int accountid, String serverUrl, int serverPort) {
		super(serverUrl, serverPort);
		StringBuilder sb = new StringBuilder();
		sb.append(side+"##"+TRANSFROM_DATA+"##"+collectionid+"$"+accountid);
		setMessage(sb.toString());
		tempData = data;
	}
	public TransformDataThread(byte[] data,int collectionid,int accountid) {
		super();
		StringBuilder sb = new StringBuilder();
		sb.append(side+"##"+TRANSFROM_DATA+"##"+collectionid+"$"+accountid);
		setMessage(sb.toString());
		tempData = data;
	}

	@Override
	public void run() {
		Socket socket;
		try {
			socket = new Socket(mServerUrl,mServerPort);
		
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			while(isTransform){
				
				if(CompareUtil.bytesEqual(tempData, previousData) && 
						tempData!=null && previousData!=null){
					out.writeBytes(message);
					out.flush();
					out.writeInt(tempData.length);
					out.writeInt(mWidth);
					out.writeInt(mHeight);
					out.write(tempData);
					out.flush();
				}
			}
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
	public void setData(byte[] data,int w,int h){
		previousData = tempData;
		tempData = data;
		mWidth = w;
		mHeight = h;
	}
}