package com.gdht.itasset.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CheckLinearLayout extends LinearLayout {
	private boolean isChecked;
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		onCheckedChanged(isChecked);
	}
	private void onCheckedChanged(boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			this.setBackgroundColor(Color.parseColor("#f8f4d1"));
		}else{
			this.setBackgroundColor(Color.parseColor("#f6fafd"));
		}
	}
	public CheckLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CheckLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public CheckLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
