package com.gdht.itasset.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;

public class AppSharedPreferences {
	SharedPreferences sp = null;
	public AppSharedPreferences(Context context, String name) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	public void saveIP(String ip) {
		Editor editor = sp.edit();
		editor.putString("ip", ip);
		editor.commit();
	}
	
	public String getIP(){
		return sp.getString("ip", "http://192.168.23.1:8080/itasset2.0");
	}
	
	public void setGongLv(int gl) {
		Editor editor = sp.edit();
		editor.putInt("gonglv", gl);
		editor.commit();
	}
	
	public int getGongLv() {
		return sp.getInt("gonglv", 10);
	}

}















































