package com.gdht.itasset;

import com.gdht.itasset.utils.AppSharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class OptionActivity extends Activity {
	private EditText ipEdt = null;
	private EditText portEdt = null;
	private EditText PjEdt = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		ipEdt = (EditText)findViewById(R.id.ipEdt);
		portEdt = (EditText)findViewById(R.id.portEdt);
		PjEdt = (EditText)findViewById(R.id.PjEdt);
		
		String defaultAddr = new AppSharedPreferences(OptionActivity.this, "gdht").getIP();
		ipEdt.setText(defaultAddr.split("http://")[1].split(":")[0]);
		portEdt.setText(defaultAddr.split(":")[2].split("/")[0]);
		PjEdt.setText(defaultAddr.substring(defaultAddr.lastIndexOf("/")+1));
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
		case R.id.quxiao:
			this.finish();
			break;
		
		case R.id.queding:
			String ip = ipEdt.getText().toString().trim();
			String port = portEdt.getText().toString().trim();
			String project = PjEdt.getText().toString().trim();
			MainActivity.ipStr = "http://"+ip+":"+port+"/"+project;
			new AppSharedPreferences(OptionActivity.this, "gdht").saveIP(MainActivity.ipStr);
			this.finish();
			break;
		
		}
	}

}
