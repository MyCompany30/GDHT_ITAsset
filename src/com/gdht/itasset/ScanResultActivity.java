package com.gdht.itasset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.PlanDetail;
import com.gdht.itasset.widget.WaitingDialog;

public class ScanResultActivity extends Activity {
	private LinearLayout yipanBtn, weipanBtn, panyingBtn, pankuiBtn;
	private TextView yipanTxt, weipanTxt, panyingTxt, pankuiTxt, name,date, keeper;
	private int width, nameWidth, lineWidth;
	private View lineLeft, lineRight;
	private String planId;
	private WaitingDialog wd;
	private RelativeLayout dateLayout;
	private SharedPreferences loginSettings = null;
	private String userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_result);
		wd = new WaitingDialog(this);
		planId = this.getIntent().getStringExtra("planId");
		loginSettings = this.getSharedPreferences("GDHT_ITASSET_SETTINGS", Context.MODE_PRIVATE);
		userid = loginSettings.getString("LOGIN_NAME", "");
		initViews();
		new GetInfoAt().execute("");
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
			return new HttpClientUtil(ScanResultActivity.this).getPlanInfoById(ScanResultActivity.this, planId, userid);
		}
		
		@Override
		protected void onPostExecute(PlanDetail result) {
			super.onPostExecute(result);
			wd.dismiss();
			if(result != null) {
				Log.i("a", "result = " + result.toString());
				name.setText(result.getTitle());
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
				String pandianDate = result.getWctime();
				if(pandianDate == null || "".equals(pandianDate) || "null".equals(pandianDate)) {
					pandianDate = result.getQdtime();
				}
				if(pandianDate == null || "".equals(pandianDate) || "null".equals(pandianDate)) {
					dateLayout.setVisibility(View.GONE);
				}else {
					pandianDate = pandianDate.substring(0, pandianDate.indexOf(" "));
					date.setText(pandianDate);
				}
				yipanTxt.setText("已盘：" + result.getYp());
				weipanTxt.setText("未盘：" + result.getWp());
				panyingTxt.setText("盘盈：" + result.getPy());
				pankuiTxt.setText("盘亏：" + result.getPk());
				
			}else {
				Toast.makeText(ScanResultActivity.this, "获取服务器数据失败", 0).show();
			}
		}
		
	}
	
	private void initViews() {
		width = this.getResources().getDisplayMetrics().widthPixels;
		name = (TextView) this.findViewById(R.id.name);
		date = (TextView) this.findViewById(R.id.date);
		lineLeft = this.findViewById(R.id.lineLeft);
		lineRight = this.findViewById(R.id.lineRight);
		dateLayout = (RelativeLayout) this.findViewById(R.id.dateLayout);
		keeper = (TextView) this.findViewById(R.id.keeper);
		
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
			Intent intent = new Intent(this, ScanPandianActivity.class);
			startActivity(intent);
			break;
		case R.id.finish:
			break;
		}
	}
	
}
