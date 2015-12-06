package com.apps.captainjack.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Song implements Serializable {

	private int id;
	private String urlStream;
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrlStream() {
		return urlStream;
	}

	public void setUrlStream(String urlStream) {
		this.urlStream = urlStream;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
