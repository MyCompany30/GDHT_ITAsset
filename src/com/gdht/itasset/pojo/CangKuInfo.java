package com.gdht.itasset.pojo;

public class CangKuInfo implements DeptInfo{
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CangKuInfo [key=" + key + ", value=" + value + "]";
	}
	
	
}
