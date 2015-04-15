package com.gdht.itasset.pojo;

public class Message {
	private String user;
	private String title;
	private String content;
	private boolean isLooked;
	
	public boolean isLooked() {
		return isLooked;
	}
	public void setLooked(boolean isLooked) {
		this.isLooked = isLooked;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
