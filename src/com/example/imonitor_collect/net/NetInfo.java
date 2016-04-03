package com.example.imonitor_collect.net;

public class NetInfo {
	private boolean isConnected;
	private boolean isStartedSend;
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	public boolean isStartedSend() {
		return isStartedSend;
	}
	public void setStartedSend(boolean isStartedSend) {
		this.isStartedSend = isStartedSend;
	}
}
