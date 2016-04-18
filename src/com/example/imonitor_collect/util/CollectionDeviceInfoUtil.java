package com.example.imonitor_collect.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.example.imonitor_collect.device.CollectionDevice;


public class CollectionDeviceInfoUtil {
	private CollectionDevice mCollectionDevice;
	SharedPreferences prefs; 
	
	public CollectionDeviceInfoUtil(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	public CollectionDeviceInfoUtil(SharedPreferences prefs) {
		this.prefs = prefs;
	}
	public CollectionDevice getCollectionDeviceFromPre(){
		mCollectionDevice = new CollectionDevice();
		mCollectionDevice.setCollectionid(prefs.getInt("id", -1));
		mCollectionDevice.setDeviceName(prefs.getString("Devicename", "android1111"));
		mCollectionDevice.setUserName(prefs.getString("Username", "user999"));
		mCollectionDevice.setPassword(prefs.getString("Password","123456"));
		mCollectionDevice.setCid(prefs.getString("Cid", "no registered cid"));
		
		return mCollectionDevice;
	}
	public void editCollectionDevicePre(CollectionDevice CollectionDevice){
		mCollectionDevice = CollectionDevice;
		Editor editor = prefs.edit();
		editor.putInt("id", mCollectionDevice.getCollectionid());
    	editor.putString("Devicename", mCollectionDevice.getDeviceName());
    	editor.putString("Username", mCollectionDevice.getUserName());
    	editor.putString("Password", mCollectionDevice.getPassword());
		editor.commit();
	}
}
