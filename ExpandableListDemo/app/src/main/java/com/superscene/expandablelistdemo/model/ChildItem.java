package com.superscene.expandablelistdemo.model;

public class ChildItem {
		private String title;//������ʾ������
		private int markerImgId;//ÿ�������ͼ��
	
	public ChildItem(String title, int markerImgId)
	{
		this.title = title;
		this.markerImgId = markerImgId;
		
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMarkerImgId() {
		return markerImgId;
	}
	public void setMarkerImgId(int markerImgId) {
		this.markerImgId = markerImgId;
	}
	

}
