package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.adapter.RfidAdapter;
import com.gdht.itasset.http.HttpClientUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ScanResultDetailActivity extends Activity {
	private TextView titleTv = null;
	private ListView listView = null;
	private String type = null;
	private String planId = null;
	private ArrayList<String> rfids = null;
	private RfidAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result_detail);
		titleTv = (TextView)findViewById(R.id.title);
		listView = (ListView)findViewById(R.id.listView);
		type = getIntent().getStringExtra("type");
		if(type.equals("0")){
			titleTv.setText("未盘资产");
		}else if(type.equals("1")){
			titleTv.setText("已盘资产");
		}else if(type.equals("2")){
			titleTv.setText("盘盈资产");
		}else if(type.equals("3")){
			titleTv.setText("盘亏资产");
		}
		planId = getIntent().getStringExtra("planId");
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				rfids = new HttpClientUtil(ScanResultDetailActivity.this).getRfidByPlanIdAndState(ScanResultDetailActivity.this, planId, type);
				return null;
			}
			
			protected void onPostExecute(Void result) {
				adapter = new RfidAdapter(ScanResultDetailActivity.this, rfids, planId);
				listView.setAdapter(adapter);
			};
			
		}.execute();
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}

}
