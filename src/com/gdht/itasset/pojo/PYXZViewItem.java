package com.gdht.itasset.pojo;

import android.widget.EditText;
import android.widget.TextView;

public class PYXZViewItem {

	private TextView rfid;
	private EditText name;
	private TextView classify;
	private TextView type;
	private EditText keeper;
	public TextView getRfid() {
		return rfid;
	}
	public void setRfid(TextView rfid) {
		this.rfid = rfid;
	}
	public EditText getName() {
		return name;
	}
	public void setName(EditText name) {
		this.name = name;
	}
	public TextView getClassify() {
		return classify;
	}
	public void setClassify(TextView classify) {
		this.classify = classify;
	}
	public TextView getType() {
		return type;
	}
	public void setType(TextView type) {
		this.type = type;
	}
	public EditText getKeeper() {
		return keeper;
	}
	public void setKeeper(EditText keeper) {
		this.keeper = keeper;
	}
	@Override
	public String toString() {
		return "PYXZViewItem [rfid=" + rfid + ", name=" + name + ", classify="
				+ classify + ", type=" + type + ", keeper=" + keeper + "]";
	}
	
	
	
	
}
