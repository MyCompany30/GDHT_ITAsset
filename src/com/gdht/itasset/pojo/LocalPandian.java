package com.gdht.itasset.pojo;

public class LocalPandian {

	private String planId;
	private String rfids;
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getRfids() {
		return rfids;
	}
	public void setRfids(String rfids) {
		this.rfids = rfids;
	}
	@Override
	public String toString() {
		return "LocalPandian [planId=" + planId + ", rfids=" + rfids + "]";
	}
	
	
}
