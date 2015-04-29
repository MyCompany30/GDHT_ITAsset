package com.gdht.itasset.pojo;

import java.io.Serializable;

public class PlanInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3178209081701721579L;
	private String id;
	private String title;
	private String depts;
	private String detail;
	private String planstate;
	private String registerdate1;
	private String registerdate2;
	private String registerdate3;
	private String registrant1;
	private String registrant2;
	private String registrant3;
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
	public String getDepts() {
		return depts;
	}
	public void setDepts(String depts) {
		this.depts = depts;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPlanstate() {
		return planstate;
	}
	public void setPlanstate(String planstate) {
		this.planstate = planstate;
	}
	public String getRegisterdate1() {
		return registerdate1;
	}
	public void setRegisterdate1(String registerdate1) {
		this.registerdate1 = registerdate1;
	}
	public String getRegisterdate2() {
		return registerdate2;
	}
	public void setRegisterdate2(String registerdate2) {
		this.registerdate2 = registerdate2;
	}
	public String getRegisterdate3() {
		return registerdate3;
	}
	public void setRegisterdate3(String registerdate3) {
		this.registerdate3 = registerdate3;
	}
	public String getRegistrant1() {
		return registrant1;
	}
	public void setRegistrant1(String registrant1) {
		this.registrant1 = registrant1;
	}
	public String getRegistrant2() {
		return registrant2;
	}
	public void setRegistrant2(String registrant2) {
		this.registrant2 = registrant2;
	}
	public String getRegistrant3() {
		return registrant3;
	}
	public void setRegistrant3(String registrant3) {
		this.registrant3 = registrant3;
	}
}
