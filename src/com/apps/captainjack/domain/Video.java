package com.apps.captainjack.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Video implements Serializable {

	private String youtubeId;
	private String title;
	private String imageHight;
	private String publishAt;
	private String imageDefault;
	private String imageMedium;

	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageHight() {
		return imageHight;
	}

	public void setImageHight(String imageHight) {
		this.imageHight = imageHight;
	}

	public String getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(String publishAt) {
		this.publishAt = publishAt;
	}

	public String getImageDefault() {
		return imageDefault;
	}

	public void setImageDefault(String imageDefault) {
		this.imageDefault = imageDefault;
	}

	public String getImageMedium() {
		return imageMedium;
	}

	public void setImageMedium(String imageMedium) {
		this.imageMedium = imageMedium;
	}

}
