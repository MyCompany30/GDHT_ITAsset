package com.gdht.itasset.eventbus;

public class SelectClassifyAndTypeListener {
	String zcflCode;
	String zcflName;
	String zclxCode;
	String zclxName;
	
	
	public SelectClassifyAndTypeListener(String zcflCode, String zcflName,
			String zclxCode, String zclxName) {
		super();
		this.zcflCode = zcflCode;
		this.zcflName = zcflName;
		this.zclxCode = zclxCode;
		this.zclxName = zclxName;
	}
	public String getZcflCode() {
		return zcflCode;
	}
	public void setZcflCode(String zcflCode) {
		this.zcflCode = zcflCode;
	}
	public String getZcflName() {
		return zcflName;
	}
	public void setZcflName(String zcflName) {
		this.zcflName = zcflName;
	}
	public String getZclxCode() {
		return zclxCode;
	}
	public void setZclxCode(String zclxCode) {
		this.zclxCode = zclxCode;
	}
	public String getZclxName() {
		return zclxName;
	}
	public void setZclxName(String zclxName) {
		this.zclxName = zclxName;
	}
	
}
