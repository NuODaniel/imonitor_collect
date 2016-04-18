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
import android.widget.Toast;

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
	private int mFrameRate;
	private long mFrameRateStartTime;
	private int mHeight;
	private int mWidth;
	private int mCollectionId;
	private int mAccountId;
	private static Intent retriIntent;
	private static TransformDataThread transformThread;
    
    public CameraPreview(Context context,VideoSetting videoSetting,Camera camera) {
        super(context);
        mContext = context;
        mCamera = camera;
        this.videoSetting = videoSetting;
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        runRetriService();
    }
    public void reset(Camera c){
    	mCamera = c;
    	mHolder = getHolder();
    	c.setPreviewCallback(this);
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
        runRetriService();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
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
        	
        	mCamera.setPreviewDisplay(mHolder);
        	initCameraParams();
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

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
//		if(!netinfo.isStartedSend())
//			return;
		if(tempPreFrameNo<videoSetting.getVideoPreRate()){
			tempPreFrameNo++;
			return;
		}
		tempPreFrameNo=0;		
		if(data!=null)
		{	
			  mWidth = videoSetting.getVideoWidth();
			  mHeight = videoSetting.getVideoHeight();
			  int length = data.length;
	
			  if(mFrameRateStartTime == 0)
			  {
			      mFrameRateStartTime = System.currentTimeMillis();
			  }
			  mFrameRate ++;
			  if(mFrameRate % 30 == 0){
			      long rate = mFrameRate * 1000 / (System.currentTimeMillis() - mFrameRateStartTime);
	                  Toast.makeText(mContext, "Frame Rate:" + rate, Toast.LENGTH_SHORT).show();
			  }
			  if(isStartTransform){
//				  mWidth = camera.getParameters().getPreviewSize().width;
//			  	mHeight = camera.getParameters().getPreviewSize().height;
				  transformThread.setData(data,mWidth,mHeight);
			  }
		}
		
	}

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
				if(transformThread == null)
					transformThread = new TransformDataThread(null,mCollectionId,mAccountId);
				new Thread(transformThread).start();
			}
		};
	};
	
}
