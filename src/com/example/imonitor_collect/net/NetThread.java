package com.example.imonitor_collect.net;

public abstract class NetThread implements Runnable{
	/*
	 * collection client send these orders to server
	 */
	public static final String REGISTER_COLLECTION = "REGISTER";
	public static final String CONNECTING_TO_SERVER = "CONNECT";
	public static final String DISCONNECTING = "DISCONNECT";
	public static final String MODIFY_INFO = "MODIFY_INFO";
	
	protected String message;
	protected String mServerUrl;
	protected int mServerPort;
	public NetThread(String msg, String serverUrl, int serverPort) {
		message = msg;
		mServerUrl = serverUrl;
		mServerPort = serverPort;
	}
	
}
