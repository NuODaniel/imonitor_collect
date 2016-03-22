package com.example.imonitor_collect.Handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class UpdateConnectStateHandler extends Handler{
	TextView view;
	public UpdateConnectStateHandler(View v){
		view = (TextView) v;
	}
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		switch(msg.arg1){
		case 1:
			view.setText("");
			break;
		default:
		}
		
	}
}
