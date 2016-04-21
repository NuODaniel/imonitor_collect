package com.example.imonitor_collect.device;

public class VideoSetting {
	private int VideoPreRate;//帧率
	private int VideoQuality=85;//视频质量
    private int VideoWidthRatio=1;//发送视频宽度比例
    private int VideoHeightRatio=1;//发送视频高度比例
    private int VideoFormatIndex=0;//视频格式索引
    private int VideoWidth=640;//发送视频宽度
    private int VideoHeight=480;//发送视频高度
    private int Codeway=0;//1硬件0软件
    
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
	public int getVideoWidthRatio() {
		return VideoWidthRatio;
	}
	public void setVideoWidthRatio(int videoWidthRatio) {
		VideoWidthRatio = videoWidthRatio;
	}
	public int getVideoHeightRatio() {
		return VideoHeightRatio;
	}
	public void setVideoHeightRatio(int videoHeightRatio) {
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
