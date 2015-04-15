package com.gdht.itasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainScanActivity extends Activity {
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_main);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.rfidScan:
			intent = new Intent();
			intent.setClass(MainScanActivity.this, ScanActivity.class);
			this.startActivity(intent);
			break;
		case R.id.erweiScan:
			intent = new Intent(this, ErWeiScanActivity.class);
			startActivity(intent);
			break;
		case R.id.localDB:
			intent = new Intent(this, LocalDBActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}
}
