package com.mysoft.entity;

import java.io.Serializable;

public class SMS implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5138191257837905373L;
	private String id,address,content,target;
	private long date_time;
	private boolean isSended;
	
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isSended() {
		return isSended;
	}

	public void setSended(boolean isSended) {
		this.isSended = isSended;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDate_time() {
		return date_time;
	}

	public void setDate_time(long date_time) {
		this.date_time = date_time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
