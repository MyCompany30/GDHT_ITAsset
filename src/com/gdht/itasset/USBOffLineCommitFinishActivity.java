package com.gdht.itasset;

import java.io.File;

import com.gdht.itasset.db.service.LocalPandianService;
import com.gdht.itasset.receiver.ConnectionChangeReceiver;
import com.gdht.itasset.utils.GlobalParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;

public class USBOffLineCommitFinishActivity extends Activity {
//	private AlertDialog ad;
	private LocalPandianService localPandianService = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_offline_commit);
		localPandianService = new LocalPandianService(this);
//		initAd(this);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.sure:
		case R.id.close:
			localPandianService.delete();
			String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "/itasset";
			File fileRoot = new File(filePath);
			if(fileRoot.exists()) {
				if(fileRoot.listFiles().length > 0) {
					for(File f : fileRoot.listFiles()) {
						f.delete();
					}
				}
			}
			this.finish();
			break;
		}
	}
}
