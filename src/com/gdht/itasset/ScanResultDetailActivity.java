package com.gdht.itasset;

import java.util.ArrayList;

import org.apache.http.protocol.HTTP;

import com.gdht.itasset.adapter.RfidAdapter;
import com.gdht.itasset.adapter.RfidPanDianAdapter;
import com.gdht.itasset.adapter.RfidSelectAdapter;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.utils.GlobalParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
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
	private View xinzengLayout = null;
	private ImageView xinzengBtn = null;
	private View pankuiLayout = null;
	private ImageView pankuiBtn = null;
	private ProgressDialog pd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result_detail);
		titleTv = (TextView)findViewById(R.id.title);
		listView = (ListView)findViewById(R.id.listView);
		type = getIntent().getStringExtra("type");
		pankuiLayout = findViewById(R.id.pankuiLayout);
		xinzengLayout = findViewById(R.id.xinzengLayout);
		pankuiBtn = (ImageView)findViewById(R.id.pankuiBtn);
		xinzengBtn = (ImageView)findViewById(R.id.xinzengBtn);
		pd = new ProgressDialog(this);
		pd.setMessage("处理中...");
		if(type.equals("0")){
			titleTv.setText("未盘资产");
			xinzengLayout.setVisibility(View.GONE);
			pankuiLayout.setVisibility(View.VISIBLE);
			pankuiBtn.setOnClickListener(new OnClickListener() {
				//盘亏
				@Override
				public void onClick(View v) {
					new AsyncTask<Void, Void, String>(){
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							pd.show();
						}
						@Override
						protected String doInBackground(Void... params) {
							return new HttpClientUtil(ScanResultDetailActivity.this).getIdByRfid(ScanResultDetailActivity.this, ((RfidSelectAdapter)adapter).getCheckedRfid(),((RfidSelectAdapter)adapter).getOriginalRfids());
						}
						@Override
						protected void onPostExecute(final String result) {
							super.onPostExecute(result);
							new AsyncTask<Void, Void, String>(){
								@Override
								protected void onPreExecute() {
									super.onPreExecute();
									
								}
								@Override
								protected String doInBackground(Void... params) {
									return new HttpClientUtil(ScanResultDetailActivity.this).updateAssetStatus(ScanResultDetailActivity.this, planId, result, "", "3", GlobalParams.username);
								}
								@Override
								protected void onPostExecute(String result) {
									// TODO Auto-generated method stub
									super.onPostExecute(result);
									pd.dismiss();
									if(result.equals("1")){
										Toast.makeText(ScanResultDetailActivity.this, "处理成功", Toast.LENGTH_SHORT).show();
										if(GlobalParams.LOGIN_TYPE == 1){
											new AsyncTask<Void, Void, Void>() {

												@Override
												protected Void doInBackground(Void... params) {
													rfids = new HttpClientUtil(ScanResultDetailActivity.this).getRfidByPlanIdAndState(ScanResultDetailActivity.this, planId, type);
													return null;
												}
												
												protected void onPostExecute(Void result) {
													if(type.equals("0")){
														adapter = new RfidSelectAdapter(ScanResultDetailActivity.this, rfids, planId);
														listView.setAdapter(adapter);
													}else{
														adapter = new RfidAdapter(ScanResultDetailActivity.this, rfids, planId);
														listView.setAdapter(adapter);
													}
													
												};
												
											}.execute();
										}else {
											strRfids = ScanResultDetailActivity.this.getIntent().getStringExtra("rfids");
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
									}else{
										Toast.makeText(ScanResultDetailActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
									}
								}
							}.execute();
						}
						
					}.execute();
				}
			});
		}else if(type.equals("1")){
			pankuiLayout.setVisibility(View.GONE);
			xinzengLayout.setVisibility(View.GONE);
			titleTv.setText("已盘资产");
		}else if(type.equals("2")){
			pankuiLayout.setVisibility(View.GONE);
			titleTv.setText("盘盈资产");
			xinzengLayout.setVisibility(View.VISIBLE);
			xinzengBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//新增
					
				}
			});
		}else if(type.equals("3")){
			pankuiLayout.setVisibility(View.GONE);
			xinzengLayout.setVisibility(View.GONE);
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
					if(type.equals("0")){
						adapter = new RfidSelectAdapter(ScanResultDetailActivity.this, rfids, planId);
						listView.setAdapter(adapter);
					}else{
						adapter = new RfidAdapter(ScanResultDetailActivity.this, rfids, planId);
						listView.setAdapter(adapter);
					}
					
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
