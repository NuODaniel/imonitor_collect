package com.example.imonitor_collect.device;

public class VideoSetting {
	private int VideoPreRate;//帧率
	private int VideoQuality=85;//视频质量
    private float VideoWidthRatio=1;//发送视频宽度比例
    private float VideoHeightRatio=1;//发送视频高度比例
    private int VideoFormatIndex=0;//视频格式索引
    private int VideoWidth=320;//发送视频宽度
    private int VideoHeight=240;//发送视频高度
    private int Codeway=1;//1硬件0软件
    
	public int getVideoPreRate() {
		return VideoPreRate;
	}
	public void setVideoPreRate(int videoPreRate) {
		VideoPreRate = videoPreRate;
	}
	public int getVideoQuality() {
		return VideoQuality;
	}
	public void setVideoQuality(int videoQuality) {
		VideoQuality = videoQuality;
	}
	public float getVideoWidthRatio() {
		return VideoWidthRatio;
	}
	public void setVideoWidthRatio(float videoWidthRatio) {
		VideoWidthRatio = videoWidthRatio;
	}
	public float getVideoHeightRatio() {
		return VideoHeightRatio;
	}
	public void setVideoHeightRatio(float videoHeightRatio) {
		VideoHeightRatio = videoHeightRatio;
	}
	public int getVideoFormatIndex() {
		return VideoFormatIndex;
	}
	public void setVideoFormatIndex(int videoFormatIndex) {
		VideoFormatIndex = videoFormatIndex;
	}
	public int getVideoWidth() {
		return VideoWidth;
	}
	public void setVideoWidth(int videoWidth) {
		VideoWidth = videoWidth;
	}
	public int getVideoHeight() {
		return VideoHeight;
	}
	public void setVideoHeight(int videoHeight) {
		VideoHeight = videoHeight;
	}
	public int getCodeway() {
		return Codeway;
	}
	public void setCodeway(int codeway) {
		Codeway = codeway;
	}
    
	
}
