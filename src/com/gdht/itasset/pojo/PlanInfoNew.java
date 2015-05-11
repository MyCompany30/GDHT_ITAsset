package com.gdht.itasset.pojo;

public class PlanInfoNew {
	
	private String id;
	private String name;
	private boolean zk;  //在库，在运
	private String cangku;
	private String bumen;
	private String quyu;
	private String huojia;
	private String bangongshi;
	private String planCount; //计划盘点数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isZk() {
		return zk;
	}
	public void setZk(boolean zk) {
		this.zk = zk;
	}
	public String getCangku() {
		return cangku;
	}
	public void setCangku(String cangku) {
		this.cangku = cangku;
	}
	public String getBumen() {
		return bumen;
	}
	public void setBumen(String bumen) {
		this.bumen = bumen;
	}
	public String getQuyu() {
		return quyu;
	}
	public void setQuyu(String quyu) {
		this.quyu = quyu;
	}
	public String getHuojia() {
		return huojia;
	}
	public void setHuojia(String huojia) {
		this.huojia = huojia;
	}
	public String getBangongshi() {
		return bangongshi;
	}
	public void setBangongshi(String bangongshi) {
		this.bangongshi = bangongshi;
	}
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	
	
	
}
