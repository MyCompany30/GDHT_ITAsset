package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;
import com.senter.go;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class PlanActivity extends Activity {
	public static String PLAN_ID;
	public static String operator;
	public static boolean isZYPlan;
	private ImageView saomiao_btn;
	private ImageView weipan_btn;
	private ImageView yipan_btn;
	private ImageView pankui_btn;
	private ImageView panying_btn;
	private ImageView xinzeng_btn;
	private ImageView ziChan_btn;
	private ArrayList<PlanAssetInfo> planAssetInfoList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan);
		PLAN_ID = getIntent().getStringExtra("planId");
		GlobalParams.planId = PLAN_ID;
		operator = getIntent().getStringExtra("operator");
		findViews();
		setOnClicks();
		
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				GlobalParams.planAssetInfoList = new HttpClientUtil(PlanActivity.this).getPlanInfoById(PlanActivity.this, GlobalParams.planId);
				if(GlobalParams.planAssetInfoList.size()>0){
					return "盘点资产清单获取成功";
				}
				return "";
			}
			protected void onPostExecute(String result) {
				if(!result.equals("")){
					Toast.makeText(PlanActivity.this,result, Toast.LENGTH_LONG).show();
				}
			};
		}.execute();
	}

	private void setOnClicks() {
		//扫描
		saomiao_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(GlobalParams.isLogin){
					Intent intent = new Intent();
					intent.setClass(PlanActivity.this, MainScanActivity.class);
					PlanActivity.this.startActivity(intent);
				}else{
					Intent intent = new Intent();
					intent.setClass(PlanActivity.this, MainScanActivity.class);
					PlanActivity.this.startActivity(intent);
				}
				
			}
		});
		//未盘
		weipan_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, WeiPanActivity.class);
				PlanActivity.this.startActivity(intent);
			}
		});
		//已盘
		yipan_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, YiPanActivity.class);
				PlanActivity.this.startActivity(intent);
			}
		});
		//盘亏
		pankui_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, PanKuiActivity.class);
				PlanActivity.this.startActivity(intent);
			}
		});
		//盘盈
		panying_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, PanYingActivity.class);
				PlanActivity.this.startActivity(intent);
			}
		});
		//盘盈新增
		xinzeng_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, CangKuSelectActivity.class);
				PlanActivity.this.startActivity(intent);
			}
		});
		ziChan_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(PlanActivity.this, MainScanActivity.class);
					intent.putExtra("check", "check");
					PlanActivity.this.startActivity(intent);
			}
		});
	}
	private void findViews() {
		saomiao_btn = (ImageView)findViewById(R.id.saomiao_btn);
		weipan_btn = (ImageView)findViewById(R.id.weipan_btn);
		yipan_btn = (ImageView)findViewById(R.id.yipan_btn);
		pankui_btn = (ImageView)findViewById(R.id.pankui_btn);
		panying_btn = (ImageView)findViewById(R.id.panying_btn);
		xinzeng_btn = (ImageView)findViewById(R.id.xinzeng_btn);
		ziChan_btn = (ImageView) findViewById(R.id.zichan_btn);
	}
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}
}
