package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.PlanDetail;
import com.gdht.itasset.widget.WaitingDialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultActivity extends Activity {
	private LinearLayout yipanBtn, weipanBtn, panyingBtn, pankuiBtn;
	private TextView yipanTxt, weipanTxt, panyingTxt, pankuiTxt, name;
	private int width, nameWidth, lineWidth;
	private View lineLeft, lineRight;
	private String planId;
	private WaitingDialog wd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_result);
		wd = new WaitingDialog(this);
		planId = this.getIntent().getStringExtra("planId");
		initViews();
	}
	
	private class GetInfoAt extends AsyncTask<String, Integer, PlanDetail> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			wd.show();
		}
		
		@Override
		protected PlanDetail doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(PlanDetail result) {
			super.onPostExecute(result);
		}
		
	}
	
	private void initViews() {
		width = this.getResources().getDisplayMetrics().widthPixels;
		name = (TextView) this.findViewById(R.id.name);
		lineLeft = this.findViewById(R.id.lineLeft);
		lineRight = this.findViewById(R.id.lineRight);
		name.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				nameWidth = name.getWidth();
				lineWidth = (width - nameWidth) / 2 - 20;
				LayoutParams lp1 = lineLeft.getLayoutParams();
				lp1.width = lineWidth;
				lineLeft.setLayoutParams(lp1);
				LayoutParams lp2 = lineRight.getLayoutParams();
				lp2.width = lineWidth;
				lineRight.setLayoutParams(lp2);
				name.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		
		yipanBtn = (LinearLayout) this.findViewById(R.id.yipanBtn);
		yipanTxt = (TextView) this.findViewById(R.id.yipanTxt);
		weipanBtn = (LinearLayout) this.findViewById(R.id.weipanBtn);
		weipanTxt = (TextView) this.findViewById(R.id.weipanTxt);
		panyingBtn = (LinearLayout) this.findViewById(R.id.panyingBtn);
		panyingTxt = (TextView) this.findViewById(R.id.panyingTxt);
		pankuiBtn = (LinearLayout) this.findViewById(R.id.pankuiBtn);
		pankuiTxt = (TextView) this.findViewById(R.id.pankuiTxt);
		yipanBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					yipanTxt.setTextColor(Color.WHITE);
				}else if(event.getAction() == MotionEvent.ACTION_UP) {
					yipanTxt.setTextColor(ScanResultActivity.this.getResources().getColor(R.color.yipanColor));
				}
				
				return false;
			}
		});
		yipanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(ScanResultActivity.this, "yipan", 0).show();
			}
		});
		weipanBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					weipanTxt.setTextColor(Color.WHITE);
				}else if(event.getAction() == MotionEvent.ACTION_UP) {
					weipanTxt.setTextColor(ScanResultActivity.this.getResources().getColor(R.color.weipanColor));
				}
				
				return false;
			}
		});
		weipanBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(ScanResultActivity.this, "weipan", 0).show();
			}
		});
		panyingBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					panyingTxt.setTextColor(Color.WHITE);
				}else if(event.getAction() == MotionEvent.ACTION_UP) {
					panyingTxt.setTextColor(ScanResultActivity.this.getResources().getColor(R.color.panyingColor));
				}
				
				return false;
			}
		});
		panyingBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(ScanResultActivity.this, "panying", 0).show();
			}
		});
		pankuiBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					pankuiTxt.setTextColor(Color.WHITE);
				}else if(event.getAction() == MotionEvent.ACTION_UP) {
					pankuiTxt.setTextColor(ScanResultActivity.this.getResources().getColor(R.color.pankuiColor));
				}
				
				return false;
			}
		});
		pankuiBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(ScanResultActivity.this, "yi盘亏pan", 0).show();
			}
		});
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.start:
			break;
		case R.id.finish:
			break;
		}
	}
	
}
