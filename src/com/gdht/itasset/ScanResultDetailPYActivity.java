package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.adapter.RfidAdapter;
import com.gdht.itasset.http.HttpClientUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ScanResultDetailPYActivity extends Activity {
	private TextView titleTv = null;
	private ListView listView = null;
	private String type = null;
	private String planId = null;
	private ArrayList<String> rfids = null;
	private RfidAdapter adapter = null;
	private ImageView xinzengBtn = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result_detail_py);
		titleTv = (TextView)findViewById(R.id.title);
		listView = (ListView)findViewById(R.id.listView);
		xinzengBtn = (ImageView)findViewById(R.id.xinzengBtn);
		
		xinzengBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		type = getIntent().getStringExtra("type");
		planId = getIntent().getStringExtra("planId");
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				rfids = new HttpClientUtil(ScanResultDetailPYActivity.this).getRfidByPlanIdAndState(ScanResultDetailPYActivity.this, planId, type);
				return null;
			}
			
			protected void onPostExecute(Void result) {
				adapter = new RfidAdapter(ScanResultDetailPYActivity.this, rfids);
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
