package com.gdht.itasset.eventbus;

public class SelectCangKuListener {

	private int location = 0;
	private String dept;
	private String deptName;
	public SelectCangKuListener(int location, String dept, String deptName) {
		super();
		this.location = location;
		this.dept = dept;
		this.deptName = deptName;
	}
	public String getDept() {
		return dept;
	}
	
	public String getDeptName() {
		return deptName;
	}
	
	public int getLocation() {
		return location;
	}
}
