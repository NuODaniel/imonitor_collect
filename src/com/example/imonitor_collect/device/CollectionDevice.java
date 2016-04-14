package com.example.imonitor_collect.device;

public class CollectionDevice {
	private int collectionid = 0;
	private String cid ;
	private String deviceName;
	private String userName;
	private String password;
	private int state;//0->offline;1->online
	
	public CollectionDevice(int collectionid, String cid, String deviceName,
			String userName, String password) {
		super();
		this.collectionid = collectionid;
		this.cid = cid;
		this.deviceName = deviceName;
		this.userName = userName;
		this.password = password;
	}
	public CollectionDevice() {
		
	}
	public CollectionDevice(String cid, String username, String password,
			String devicename) {
		this.cid = cid;
		this.userName = username;
		this.password = password;
		this.deviceName = devicename;
	}
	public int getCollectionid() {
		return collectionid;
	}
	public void setCollectionid(int collectionid) {
		this.collectionid = collectionid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}