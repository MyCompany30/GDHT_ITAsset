package com.gdht.itasset.pojo;

public class RealName {

	private String username;
	private String realname;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	@Override
	public String toString() {
		return "RealName [username=" + username + ", realname=" + realname
				+ "]";
	}
	
	
}
