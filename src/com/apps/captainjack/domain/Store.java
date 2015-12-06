package com.apps.captainjack.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Store implements Serializable {

	private int no;
	private String url;
	private String title;
	private String imgThumbnail;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgThumbnail() {
		return imgThumbnail;
	}

	public void setImgThumbnail(String imgThumbnail) {
		this.imgThumbnail = imgThumbnail;
	}

}
