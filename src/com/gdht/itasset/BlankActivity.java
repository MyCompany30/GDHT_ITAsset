package com.gdht.itasset;

import com.gdht.itasset.receiver.ConnectionChangeReceiver;
import com.gdht.itasset.utils.GlobalParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class BlankActivity extends Activity {
//	private AlertDialog ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_blank);
		GlobalParams.isOffLineDialogRefresh = true;
//		initAd(this);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.sure:
			GlobalParams.LOGIN_TYPE = 2;
			this.finish();
			break;
		case R.id.close:
			this.finish();
			break;
		}
	}
}
