package com.gdht.itasset.eventbus;

public class SelectCangKuListener {

	private int location = 0;
	private String dept;
	private String deptName;
	private String isCk;
	public SelectCangKuListener(int location, String dept, String deptName,
			String isCk) {
		super();
		this.location = location;
		this.dept = dept;
		this.deptName = deptName;
		this.isCk = isCk;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getIsCk() {
		return isCk;
	}
	public void setIsCk(String isCk) {
		this.isCk = isCk;
	}
	
	
}
