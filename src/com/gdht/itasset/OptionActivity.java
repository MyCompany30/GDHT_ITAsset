package com.gdht.itasset;

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
			
			
			break;
		
		}
	}

}
