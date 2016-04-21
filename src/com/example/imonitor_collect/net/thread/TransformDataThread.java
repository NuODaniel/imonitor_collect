package com.example.imonitor_collect.net.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;

import com.example.imonitor_collect.net.NetThread;
import com.example.imonitor_collect.util.CompareUtil;

public class TransformDataThread extends NetThread{
	private static byte[][] tempData = new byte[3][];
	//private byte[] previousData = null;
	//private boolean isTransform = true;
	private int mWidth;
	private int mHeight;
	private Handler mHandler;
	public TransformDataThread(byte[] data,int w,int h,int codeway ,int collectionid,int accountid, String serverUrl, int serverPort,Handler handler) {
		super(serverUrl, serverPort);
		StringBuilder sb = new StringBuilder();
		sb.append(side+"##"+TRANSFROM_DATA+"##"+collectionid+"$"+accountid+"$"+codeway+"\n");
		setMessage(sb.toString());
		setData(data, w, h);
		mHandler = handler;
	}
	public TransformDataThread(byte[] data,int w,int h,int codeway,int collectionid,int accountid,Handler handler) {
		super();
		StringBuilder sb = new StringBuilder();
		sb.append(side+"##"+TRANSFROM_DATA+"##"+codeway+"$"+collectionid+"$"+accountid+"\n");
		setMessage(sb.toString());
		setData(data, w, h);
		mHandler = handler;
	}

	@Override
	public void run() {
		Socket socket;
		try {
			socket = new Socket(mServerUrl,mServerPort);
			
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				if(tempData[0]!=null && tempData[1]!=null && tempData[2]!=null){
					//if(){
						out.writeBytes(message);
						out.flush();

						out.writeInt(tempData[0].length);
						out.writeInt(mWidth);
						out.writeInt(mHeight);
						out.flush();
						
						out.write(tempData[0]);
						out.flush();
					//}
					
				}
			out.close();
			socket.close();
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			mHandler.sendMessage(msg);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setData(byte[] data,int w,int h){
		tempData[2] = tempData[1];
		tempData[1] = tempData[0];
		tempData[0] = data;
		mWidth = w;
		mHeight = h;
	}
}