package com.gdht.itasset;

import com.gdht.itasset.utils.GlobalParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PlanActivity extends Activity {
	public static String PLAN_ID;
	public static String operator;
	private ImageView saomiao_btn;
	private ImageView weipan_btn;
	private ImageView yipan_btn;
	private ImageView pankui_btn;
	private ImageView panying_btn;
	private ImageView xinzeng_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan);
		PLAN_ID = getIntent().getStringExtra("planId");
		GlobalParams.planId = PLAN_ID;
		operator = getIntent().getStringExtra("operator");
		findViews();
		setOnClicks();
	}

	private void setOnClicks() {
		//扫描
		saomiao_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, MainScanActivity.class);
				PlanActivity.this.startActivity(intent);
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
	}
	private void findViews() {
		saomiao_btn = (ImageView)findViewById(R.id.saomiao_btn);
		weipan_btn = (ImageView)findViewById(R.id.weipan_btn);
		yipan_btn = (ImageView)findViewById(R.id.yipan_btn);
		pankui_btn = (ImageView)findViewById(R.id.pankui_btn);
		panying_btn = (ImageView)findViewById(R.id.panying_btn);
		xinzeng_btn = (ImageView)findViewById(R.id.xinzeng_btn);
	}

}
