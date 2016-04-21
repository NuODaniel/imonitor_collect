package com.example.imonitor_collect.net;

public abstract class NetThread implements Runnable{
	/*
	 * collection client send these orders to server
	 */
	public static final int REGISTER_COLLECTION = 100001;
	public static final int CONNECTING_TO_SERVER = 100002;
	public static final int DISCONNECTING = 100003;
	public static final int MODIFY_INFO = 100004;
	public static final int START_TRANSFORM_VIDEO = 100009;
	public static final int TRANSFROM_DATA = 100010;
	public static final int END_TRANSFORM_VIDEO = 100012;
	
	public static final String side = "COLLECTION";
	protected String message;
	protected String mServerUrl = "192.168.253.1";
	protected int mServerPort = 6789;
	public NetThread(String msg, String serverUrl, int serverPort) {
		message = msg;
		mServerUrl = serverUrl;
		mServerPort = serverPort;
	}
	public NetThread(String serverUrl, int serverPort) {
		mServerUrl = serverUrl;
		mServerPort = serverPort;
	}
	public NetThread() {
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
