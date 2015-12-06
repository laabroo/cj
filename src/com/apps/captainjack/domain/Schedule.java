package com.apps.captainjack.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Schedule implements Serializable {

	private int no;
	private String time;
	private String place;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

}
