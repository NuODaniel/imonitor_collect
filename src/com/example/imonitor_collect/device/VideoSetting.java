package com.example.imonitor_collect.device;

public class VideoSetting {
	private int VideoPreRate;//֡��
	private int VideoQuality=85;//��Ƶ����
    private float VideoWidthRatio=1;//������Ƶ��ȱ���
    private float VideoHeightRatio=1;//������Ƶ�߶ȱ���
    private int VideoFormatIndex=0;//��Ƶ��ʽ����
    private int VideoWidth=320;//������Ƶ���
    private int VideoHeight=240;//������Ƶ�߶�
    private int Codeway=1;//1Ӳ��0���
    
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
