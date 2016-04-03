package com.example.imonitor_collect.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.imonitor_collect.device.VideoSetting;
import com.example.imonitor_collect.net.NetInfo;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback{
	private String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Parameters params;
    private NetInfo netinfo;
    private VideoSetting videoSetting;
    private int tempPreFrameNo = 0;
    
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
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
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
        	
        	mCamera.setPreviewDisplay(mHolder);
        	initCameraParams();
        	mCamera.startPreview();
    		mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
    private void initCameraParams(){
		params = mCamera.getParameters();
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
		mCamera.setParameters(params);
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		if(!netinfo.isStartedSend())
			return;
		if(tempPreFrameNo<videoSetting.getVideoPreRate()){
			tempPreFrameNo++;
			return;
		}
		tempPreFrameNo=0;		
		try {
		      if(data!=null)
		      {
		        YuvImage image = new YuvImage(data,
        									videoSetting.getVideoFormatIndex(),
        									videoSetting.getVideoWidth(),
        									videoSetting.getVideoHeight(),
        									null);
		        if(image!=null)
		        {
		        	ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		      	  	//在此设置图片的尺寸和质量 
		        	Camera.Size imageSize = mCamera.getParameters().getPreviewSize();
		      	  	image.compressToJpeg(new Rect(0, 0,
		      	  			(int)(videoSetting.getVideoWidthRatio()*imageSize.width), 
		      	  			(int)(videoSetting.getVideoHeightRatio()*imageSize.height)),
		      	  			videoSetting.getVideoQuality(), outstream);  
		      	  	outstream.flush();
		      	  	//启用线程将图像数据发送出去
		      	  	//Thread th =ort) new MySendFileThread(outstream,pUsername,serverUrl,serverP;
		      	  	//th.start();  
		      	  	
		        }
		      }
		  } catch (IOException e) {
		      e.printStackTrace();
		  }
	}
   
}