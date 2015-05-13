package com.gdht.itasset.pojo;

import java.io.Serializable;

public class PlanInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3178209081701721579L;
	private String id;//计划id
	private String title;//计划名称
	private String type;//计划部门类型(1.仓库2.在运)
	private String depts;//仓库(部门)
	private int number;//计划盘点数
	private String planstate;//计划状态(0已完成；1执行中)
	private String detail;
	private String deptcode;//仓库部门的code
	private String qdtime;//启动日期
	private String wctime;//完成日期
	private String persons;  //所属人
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDepts() {
		return depts;
	}
	public void setDepts(String depts) {
		this.depts = depts;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getPlanstate() {
		return planstate;
	}
	public void setPlanstate(String planstate) {
		this.planstate = planstate;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getQdtime() {
		return qdtime;
	}
	public void setQdtime(String qdtime) {
		this.qdtime = qdtime;
	}
	public String getWctime() {
		return wctime;
	}
	public void setWctime(String wctime) {
		this.wctime = wctime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPersons() {
		return persons;
	}
	public void setPersons(String persons) {
		this.persons = persons;
	}
	@Override
	public String toString() {
		return "PlanInfo [id=" + id + ", title=" + title + ", type=" + type
				+ ", depts=" + depts + ", number=" + number + ", planstate="
				+ planstate + ", detail=" + detail + ", deptcode=" + deptcode
				+ ", qdtime=" + qdtime + ", wctime=" + wctime + ", persons="
				+ persons + "]";
	}
	
	
	
	
}
