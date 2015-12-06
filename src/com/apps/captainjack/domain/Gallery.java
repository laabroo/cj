package com.apps.captainjack.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Gallery implements Serializable {

	private int no;
	private String thumbnail;
	private String large;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

}
