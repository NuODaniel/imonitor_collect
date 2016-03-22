package com.example.imonitor_collect.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imonitor_collect.R;
import com.example.imonitor_collect.net.NetStateUtil;
import com.example.imonitor_collect.net.ReciveCommandThread;


public class MainActivity extends Activity {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_main);
		
		 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	        // init the ListView and Adapter, nothing new
	        initListView();

	        // set a custom shadow that overlays the main content when the drawer
	        // opens
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
	                GravityCompat.START);

//	        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//	                R.drawable.ic_drawer, R.string.drawer_open,
//	                R.string.drawer_close)
//	        {
//
//	            /** Called when a drawer has settled in a completely closed state. */
//	            public void onDrawerClosed(View view)
//	            {
//
//	                invalidateOptionsMenu(); // creates call to
//	                                            // onPrepareOptionsMenu()
//	            }
//
//	            /** Called when a drawer has settled in a completely open state. */
//	            public void onDrawerOpened(View drawerView)
//	            {
//
//	                invalidateOptionsMenu(); // creates call to
//	                                            // onPrepareOptionsMenu()
//	            }
//	        };
//
//	        // Set the drawer toggle as the DrawerListener
//	        mDrawerLayout.setDrawerListener(mDrawerToggle);
	        
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.fragment_content, new PlaceholderFragment()).commit();
		}
		
	}
	private void initListView()
    {
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id)
            {
                // Highlight the selected item, update the title, and close the
                // drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(mPlanetTitles[position]);
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
		private Camera mCamera;
        private CameraPreview mPreview;
        private String TAG = "monitor_collection_main";
        private String serverUrl = "192.168.1.1";
		private int serverPort = 6789;
		private TextView mConnectStateView;
		private SendCommandThread connectThread;
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			mCamera = getCameraInstance();
			initParams(mCamera);
			FrameLayout preview = (FrameLayout)rootView.findViewById(R.id.monitor_sv1);
			mPreview = new CameraPreview(this.getActivity(), mCamera);
			preview.addView(mPreview);
			mConnectStateView = (TextView)rootView.findViewById(R.id.device_state);
			runConnectThread();
			
			
			return rootView;
		}
		
	    
		public void runConnectThread() {
			// TODO Auto-generated method stub
			String cid = getUUID();
		    
			if(new NetStateUtil(this.getActivity()).isNetworkConnected()){
				connectThread = new SendCommandThread(SendCommandThread.CONNECTING_TO_SERVER.concat(cid), serverUrl, serverPort);	
				//start the thread using handlder, get return value update the stateText;
				connectHandler.post(connectThread);
			}
			
		}
		Handler connectHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if(msg.arg1 == 1)
					mConnectStateView.setText("--������--");
				else
					mConnectStateView.setText("--δ����--");
				//this.postDelayed(connectThread, 2000);
		        //this.postAtTime(r, System.currentTimeMillis() + 100000);
			}
		};
		
		
		class SendCommandThread implements Runnable{
			private String mCommand;
			private String mServerUrl;
			private int mServerPort;
			/**
			 * client send these orders to server
			 */
			public static final String CONNECTING_TO_SERVER = "##collection$connect";
			public static final String DISCONNECTING = "##collection&disconnect";
			/**
			 * 
			 * @param commond
			 * @param serverUrl
			 * @param serverPort
			 */
			public SendCommandThread(String command,String serverUrl,int serverPort){
				this.mCommand = command;
				this.mServerPort = serverPort;
				this.mServerUrl = serverUrl;
			}
			public void run(){
				//ʵ����Socket  
		        try {
					Socket socket=new Socket(mServerUrl,mServerPort);
					PrintWriter out = new PrintWriter(socket.getOutputStream());
					BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					out.println(mCommand);
					String result = bReader.readLine();
					
					out.flush();
					out.close();
					socket.close();
				} catch (UnknownHostException e) {
				} catch (IOException e) {
				}  
			}
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			//cameraId = 0;// default id
			//OpenCameraAndSetSurfaceviewSize(0);
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			kill_camera();
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			kill_camera();
		}

		public static Camera getCameraInstance(){
		    Camera c = null;
		    try {
		            c = Camera.open(0); // attempt to get a Camera instance
		    }
		    catch (Exception e){
		        // Camera is not available (in use or does not exist)
		    }
		    return c; // returns null if camera is unavailable
		}
		
		private Parameters initParams(Camera camera){
        	Parameters param=camera.getParameters();//��ȡ����
            //get the best size of pictures
        	//if not there will be dull color photos taken.
        	//call getSupportedPreviewSizes()to get the support size list ,and get the biggest one
            Camera.Size bestSize = null;
            List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
            bestSize = sizeList.get(0);
            for(int i = 1; i < sizeList.size(); i++){
                if((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)){
                    bestSize = sizeList.get(i);
                }
            }

            List<Integer> supportedPreviewFormats = param.getSupportedPreviewFormats();
            Iterator<Integer> supportedPreviewFormatsIterator = supportedPreviewFormats.iterator();
            while(supportedPreviewFormatsIterator.hasNext()){
                Integer previewFormat =supportedPreviewFormatsIterator.next();
                if (previewFormat == ImageFormat.YV12) {
                    param.setPreviewFormat(previewFormat);
                }
            }
            param.setPreviewSize(bestSize.width, bestSize.height);

            camera.setParameters(param);
            param.setPreviewFrameRate(5);//����Ԥ����ʱ����ÿ����֡������ʾ...
			return param;
        	
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
		
		
		private void kill_camera() {
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		}
		
		
	}
}
