package com.gdht.itasset.pojo;

import java.io.Serializable;

/**
 * 移动巡检实体类
 * @author dongyang 2015年4月17日 下午5:03:31	
 * @update
 * @copyright 北京国电海通科技发展有限公司
 * @version 1.0.0
 */
public class PlanAssetInfoNew implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4674035043107081077L;
	private String id;//计划id
	private String title;//计划名称
	private String depts;//仓库(部门)
	private int number;//计划数量
	private String detail;
	private String qdtime;//启动日期
	private String wctime;//完成日期
	private int yp;//已盘数量
	private int wp;//未盘数量
	private int pk;//盘亏数量
	private int py;//盘盈数量
	
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
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
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
	public int getYp() {
		return yp;
	}
	public void setYp(int yp) {
		this.yp = yp;
	}
	public int getWp() {
		return wp;
	}
	public void setWp(int wp) {
		this.wp = wp;
	}
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public int getPy() {
		return py;
	}
	public void setPy(int py) {
		this.py = py;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
