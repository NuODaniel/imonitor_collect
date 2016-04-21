package com.example.imonitor_collect.main;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.imonitor_collect.R;



public class SettingActivity extends PreferenceActivity {
	final int USER_INFO = 0;
    final int SETTING_INFO = 1;
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        int type = b.getInt("setting");
        if(type == USER_INFO){
        	addPreferencesFromResource(R.xml.userinfo);
        }else if(type == SETTING_INFO){
        	addPreferencesFromResource(R.xml.setting);
        }
    }
}
