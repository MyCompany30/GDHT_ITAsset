package com.gdht.itasset;

import com.gdht.itasset.receiver.ConnectionChangeReceiver;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

public class BaseActivity extends Activity {

	ConnectionChangeReceiver ccr = null;
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerDateTransReceiver();
	}
	
	private void registerDateTransReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(CONNECTIVITY_CHANGE_ACTION);
		filter.setPriority(1000);
		ccr = new ConnectionChangeReceiver();
		registerReceiver(ccr, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
