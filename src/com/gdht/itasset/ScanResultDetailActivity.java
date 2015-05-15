package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.adapter.RfidAdapter;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.utils.GlobalParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultDetailActivity extends Activity {
	private TextView titleTv = null;
	private ListView listView = null;
	private String type = null;
	private String planId = null;
	private ArrayList<String> rfids = null;
	private RfidAdapter adapter = null;
	private String strRfids = "";
	private ImageView xinzengBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result_detail_py);
		titleTv = (TextView)findViewById(R.id.title);
		listView = (ListView)findViewById(R.id.listView);
		type = getIntent().getStringExtra("type");
		xinzengBtn = (ImageView)findViewById(R.id.xinzengBtn);
		xinzengBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//新增
				
			}
		});
		if(type.equals("0")){
			titleTv.setText("未盘资产");
		}else if(type.equals("1")){
			titleTv.setText("已盘资产");
		}else if(type.equals("2")){
			titleTv.setText("盘盈资产");
			xinzengBtn.setVisibility(View.VISIBLE);
		}else if(type.equals("3")){
			titleTv.setText("盘亏资产");
		}
		
		planId = getIntent().getStringExtra("planId");
		if(GlobalParams.LOGIN_TYPE == 1){
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
		}else {
			strRfids = this.getIntent().getStringExtra("rfids");
			rfids = new ArrayList<String>();
			String [] rfid = strRfids.replace("\"", "").split(",");
			for(String r : rfid){
				if(r.equals(""))
					continue;
				rfids.add(r);
			}
			adapter = new RfidAdapter(ScanResultDetailActivity.this, rfids, planId);
			listView.setAdapter(adapter);
		}
		
		

	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}

}
