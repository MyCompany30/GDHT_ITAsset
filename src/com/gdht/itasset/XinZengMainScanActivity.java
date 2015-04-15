package com.gdht.itasset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class XinZengMainScanActivity extends Activity {
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_main_xinzeng);
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.rfidScan:
			intent = new Intent();
			intent.setClass(XinZengMainScanActivity.this,
					XinZengScanActivity.class);
			this.startActivity(intent);
			break;
		case R.id.erweiScan:
			intent = new Intent(this, XinZengErWeiScanActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}
}
