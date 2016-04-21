package com.example.imonitor_collect.main;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imonitor_collect.R;
import com.example.imonitor_collect.device.CollectionDevice;
import com.example.imonitor_collect.device.VideoSetting;
import com.example.imonitor_collect.dialog.QRCodeDialog;
import com.example.imonitor_collect.net.NetThread;
import com.example.imonitor_collect.net.thread.DisconnectThread;
import com.example.imonitor_collect.net.thread.RegisterThread;
import com.example.imonitor_collect.service.CheckNetStateService;
import com.example.imonitor_collect.util.NetStateUtil;
import com.example.imonitor_collect.util.QRCodeUtil;


public class MainActivity extends Activity {
    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private PlaceholderFragment placeFragment ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_main);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // init the ListView and Adapter, nothing new
        initListView();
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
	                GravityCompat.START);
	        
		if (savedInstanceState == null) {
			if(placeFragment==null)
				placeFragment = new PlaceholderFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.fragment_content, placeFragment).commit();
		}
		
	}
    @SuppressWarnings("deprecation")
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
            // 创建退出对话框  
            AlertDialog isExit = new AlertDialog.Builder(this).create();  
            // 设置对话框标题  
            isExit.setTitle("系统提示");  
            // 设置对话框消息  
            isExit.setMessage("确定要退出吗");  
            // 添加选择按钮并注册监听  
            isExit.setButton("确定", new  DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					 switch (which)  
			            {  
			            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
			            	new Thread(new DisconnectThread(MainActivity.this)).start();
			                finish();  
			                break;  
			            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
			                break;  
			            default:  
			                break;  
			            }  
				}
            	
            });  
            isExit.setButton2("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});  
            // 显示对话框  
            isExit.show();  
  
        }  
          
        return false;  
          
    }  
	private void initListView()
    {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mMenuTitles = getResources().getStringArray(R.array.setting_array);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
            	mDrawerList.setItemChecked(position, true);
            	Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            	intent.putExtra("setting", position);
                startActivity(intent);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }
	/**
	 * 
	 * @author zhj
	 *
	 */
	public static class PlaceholderFragment extends Fragment{
		private String TAG = "monitor_collection_main";
		private String side = "COLLECTION";
		
		private View rootView = null;
        private CameraPreview mPreview;
        private TextView txtUsername;
        private TextView txtPassword;
        private TextView txtCID;
        private TextView txtAlarm;
        private TextView txtRecord;
        private TextView txtDevicename;
        private TextView txtLinkstate;
        private TextView txtCodeWay;
        private Button btnGenerateQR;
       
       // private Camera mCamera;
        private RegisterThread registerThread;
        
        private String serverUrl = "192.168.253.1";
		private int serverPort = 6789;
		
		private CollectionDevice device;
		private VideoSetting videoSetting;
		private NetStateUtil netUtil;
		private SharedPreferences preParas = null;
		private Camera mCamera;

		private static Intent connectIntent = null;
		private static int connectServiceState = 0;//0 closed 1 started
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
	        if (null != rootView) {
	            ViewGroup parent = (ViewGroup) rootView.getParent();
	            if (null != parent) {
	                parent.removeView(rootView);
	            }
	        } else {
	            rootView = inflater.inflate(R.layout.fragment_main, container, false);
	            initView(rootView);// 控件初始化
	        }
			
			
			return rootView;
		}
		
		private void initView(View rv) {
			device = new CollectionDevice();
			videoSetting = new VideoSetting();
			netUtil = new NetStateUtil(this.getActivity());
			boolean first = justifyFirstRun();
			loadSetting(device,videoSetting);
			if(first)
				registerDevice();
			
			FrameLayout preview = (FrameLayout)rootView.findViewById(R.id.monitor_sv1);
			mPreview = new CameraPreview(this.getActivity(), videoSetting);
			preview.addView(mPreview);
			
			txtUsername = (TextView)rootView.findViewById(R.id.text_username);
			txtPassword = (TextView)rootView.findViewById(R.id.text_password);
			txtCID = (TextView)rootView.findViewById(R.id.text_cid);
			txtDevicename = (TextView)rootView.findViewById(R.id.text_devicename);
			txtLinkstate = (TextView)rootView.findViewById(R.id.text_linkstate);
			txtCodeWay = (TextView)rootView.findViewById(R.id.text_codeway);
			btnGenerateQR = (Button)rootView.findViewById(R.id.btn_generateQRcode);
			btnGenerateQR.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String cid = device.getCid();
					String username = device.getUserName();
					String password = device.getPassword();
					
					if(cid!=null && username!=null && password != null){
						
						QRCodeUtil qrcGenerator = new QRCodeUtil(getActivity());
						qrcGenerator.createQRImage(cid+"\n"+username+"\n"+password);
						
						QRCodeDialog.Builder builder = new QRCodeDialog.Builder(v.getContext());
						builder.setQrCode(qrcGenerator.getSweepIV());
						
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								//设置你的操作事项
							}
						});

						builder.setNegativeButton("取消",
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								});

						builder.create().show();
					}
					
				}
			});
			txtUsername.setText(device.getUserName());
			txtPassword.setText(device.getPassword());
			txtCID.setText(device.getCid());
			txtDevicename.setText("设备名："+device.getDeviceName());
			if(videoSetting.getCodeway() == 0)
				txtCodeWay.setText("软件编码");
			else
				txtCodeWay.setText("硬件编码");
			
			runConnectService();
		}

		/**
		 * load cid,devicename,username,password
		 * and the videosetting from xml
		 */
	    public void loadSetting(CollectionDevice device,VideoSetting videoSetting){
	    	preParas = PreferenceManager.getDefaultSharedPreferences(this.getActivity());//setting
	    	SharedPreferences preferences = this.getActivity().getSharedPreferences("reg", Context.MODE_PRIVATE);//cid  
	    	if(preParas != null){
		    	device.setDeviceName(preParas.getString("Devicename", "android1111"));
		    	device.setUserName(preParas.getString("Username", "user999"));
		    	device.setPassword(preParas.getString("Password","123456"));
		    	device.setCid(preferences.getString("Cid", "no registered cid"));
		    	
		    	videoSetting.setVideoPreRate(Integer.parseInt(preParas.getString("VideoPreRate", "15")));
		    	videoSetting.setVideoHeightRatio(Integer.parseInt(preParas.getString("VideoHeightRatio", "100")));
		    	videoSetting.setVideoWidthRatio(Integer.parseInt(preParas.getString("VideoWidthRatio", "100")));
		    	videoSetting.setCodeway(Integer.parseInt(preParas.getString("Codeway", "0")));
		    	
		    	Editor editor = preParas.edit();
		    	editor.putString("Devicename", device.getDeviceName());
		    	editor.putString("Username", device.getUserName());
		    	editor.putString("Password", device.getPassword());
		    	editor.putString("VideoPreRate", ((Integer)videoSetting.getVideoPreRate()).toString());
		    	editor.putString("VideoHeightRatio", ((Integer)videoSetting.getVideoHeightRatio()).toString());
		    	editor.putString("VideoWidthRatio", ((Integer)videoSetting.getVideoWidthRatio()).toString());
		    	editor.putString("Codeway", ((Integer)videoSetting.getCodeway()).toString());
		    	editor.commit();
		    }
	    }
	    public boolean justifyFirstRun(){
		   SharedPreferences preferences = this.getActivity().getSharedPreferences("reg", Context.MODE_PRIVATE);  
		   preParas = PreferenceManager.getDefaultSharedPreferences(this.getActivity());//setting
		   if (preferences.getBoolean("firststart", true)) { 
		    	Editor editor = preferences.edit();  
				editor.putBoolean("firststart", false);  
				editor.putString("Cid",getUUID());
		    	editor.commit();
		    	
		    	Editor editor2 = preParas.edit();
		    	editor2.putString("Cid",getUUID());
		    	editor2.commit();
		    	
		    	return true;
		    }else
		    	return false;
        }
	    public void registerDevice(){
	    	if(netUtil.isNetworkConnected()){
	    		registerThread = new RegisterThread(side+"##"+NetThread.REGISTER_COLLECTION+"##"+
	    												device.getCid()+"$"+
	    												device.getUserName()+"$"+
	    												device.getPassword()+"$"+
	    												device.getDeviceName(), serverUrl, serverPort,
	    												registerHandler);
	    		//registerHandler.post(registerThread);
	    		
	    		new Thread(registerThread).start();
	    	}
	    }
	    public void setRegisterState(boolean state){
			 SharedPreferences preferences = this.getActivity().getSharedPreferences("reg", Context.MODE_PRIVATE);  
		    	Editor editor = preferences.edit();  
				editor.putBoolean("firststart", state);
				editor.commit();
			}
	    Handler registerHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				if(msg.arg1 == 1)
					setRegisterState(false);
				else
					setRegisterState(true);
			}
	    };
	    
	    /**
	     * run connectService
	     */
		public void runConnectService() {
			if(connectIntent==null){
				connectIntent = new Intent();
				connectIntent.setClass(getActivity(), CheckNetStateService.class);
				connectIntent.putExtra("MainActivity", new Messenger(connectHandler));
				connectIntent.putExtra("cid", device.getCid());
				connectIntent.putExtra("devicename", device.getDeviceName());
				connectIntent.putExtra("username", device.getUserName());
				connectIntent.putExtra("password", device.getPassword());
			}
			if(connectServiceState == 0){
				this.getActivity().startService(connectIntent); 
				connectServiceState = 1;
			}
		}
		Handler connectHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if(msg.arg1 == 1)
					txtLinkstate.setText("--已连接--");
				else if(msg.arg1 == 0)
					txtLinkstate.setText("--未连接--");
			}
		};

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			this.getActivity().stopService(connectIntent);
			connectServiceState = 0;
		}

		
		/**
		 * get the android device UUID
		 * by computing with androidId, serialId, deviceId;
		 */
		private String getUUID(){
			String tmDevice, tmSerial, androidId;
			final TelephonyManager tm = (TelephonyManager) this.getActivity().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
			
			tmDevice = "" + tm.getDeviceId();
		    tmSerial = "" + tm.getSimSerialNumber();
			androidId = Settings.Secure.getString(this.getActivity().getContentResolver(),Secure.ANDROID_ID); 
			UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
			
			return deviceUuid.toString();
		}
		
	}
}
