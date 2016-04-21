package com.example.imonitor_collect.main;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.Messenger;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.imonitor_collect.device.VideoSetting;
import com.example.imonitor_collect.net.thread.TransformDataThread;
import com.example.imonitor_collect.service.RetrieveCommandService;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{
	private String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Parameters params;
    private VideoSetting videoSetting;
    
    private Context mContext;
    private int tempPreFrameNo = 0;
    private boolean isStartTransform = false;
    private boolean isContinue = true;
	private int mFrameRate;
	private long mFrameRateStartTime;
	private int mHeight;
	private int mWidth;
	private int mCollectionId;
	private int mAccountId;
	private static Intent retriIntent;
	private static TransformDataThread transformThread;
    
	 
    public CameraPreview(Context context,VideoSetting videoSetting) {
        super(context);
        mContext = context;
        
        this.videoSetting = videoSetting;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        runRetriService();
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	mCamera = Camera.open();
        	mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
        runRetriService();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        
        // start preview with new settings
        try {
        	
        	mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
    		mCamera.cancelAutoFocus();//自动对焦

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    private void initCameraParams(){
		params = mCamera.getParameters();
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1�����Խ�
		mCamera.setParameters(params);
	}
    int count =0;
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
    	// TODO Auto-generated method stub
//    	if(!netinfo.isStartedSend())
//    		return;
    	Log.v("CameraPre", "data frame");
//    	if(tempPreFrameNo<videoSetting.getVideoPreRate()){
//    		tempPreFrameNo++;
//    		return;
//    	}
    	tempPreFrameNo=0;		
    	if(data!=null&&count<4)
    	{	
    		
    		  mWidth = videoSetting.getVideoWidth();
    		  mHeight = videoSetting.getVideoHeight();
    		  int length = data.length;

//    		  if(mFrameRateStartTime == 0)
//    		  {
//    		      mFrameRateStartTime = System.currentTimeMillis();
//    		  }
//    		  mFrameRate ++;
//    		  if(mFrameRate % 30 == 0){
//    		      long rate = mFrameRate * 1000 / (System.currentTimeMillis() - mFrameRateStartTime);
//                      Toast.makeText(mContext, "Frame Rate:" + rate, Toast.LENGTH_SHORT).show();
//    		  }
    		  if(isStartTransform && isContinue){
//    			  mWidth = camera.getParameters().getPreviewSize().width;
//    		  	mHeight = camera.getParameters().getPreviewSize().height;
    			  count++;
//    			  int[] temp;
//    			  if(0==videoSetting.getCodeway())
//    				  temp = YuvToRGB.NV21ToRGB(data, mWidth, mHeight);
//    			  else
//    				  temp = ImageUtil.decodeYUV420SP(data, mWidth, mHeight);
    			  transformThread = new TransformDataThread(data,mWidth,mHeight,videoSetting.getCodeway()
    					  						,mCollectionId,mAccountId,contiTransHandler);
    			  isContinue = false;
    			  new Thread(transformThread).start();
    		  }
    	}
    	
    }
    Handler contiTransHandler = new Handler(){
    	@Override
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what == 1){
    			isContinue = true;

    		}else if(msg.what == 0){
    			isContinue = false;

    		}
    	};
    };
    private void runRetriService() {
    	if(retriIntent==null){
    		Intent retriIntent = new Intent(); 
    		retriIntent.putExtra("CameraPreview", new Messenger(beginTransformHandler));
    		retriIntent.setClass(mContext, RetrieveCommandService.class);
    		mContext.startService(retriIntent); 
    	}
    }
    Handler beginTransformHandler = new Handler(){
    	@Override
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what == 1){
    			isStartTransform = true;
    			mCollectionId = msg.arg1;
    			mAccountId = msg.arg2;

    		}else if(msg.what == 0){
    			isStartTransform = false;
    			mCollectionId = msg.arg1;
    			mAccountId = msg.arg2;

    		}
    	};
    };
    public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(0); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    	e.printStackTrace();
	    }
	    return c; // returns null if camera is unavailable
	}
}
