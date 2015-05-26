package com.gdht.itasset;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.db.service.LocalPandianService;
import com.gdht.itasset.db.service.LocalPlanResultService;
import com.gdht.itasset.eventbus.GetPandianStrListener;
import com.gdht.itasset.eventbus.RefreshDatas;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.LocalPlanResult;
import com.gdht.itasset.pojo.PlanDetail;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import de.greenrobot.event.EventBus;

public class ScanResultActivity extends Activity {
	private LinearLayout yipanBtn, weipanBtn, panyingBtn, pankuiBtn;
	private TextView yipanTxt, weipanTxt, panyingTxt, pankuiTxt, name, date,
			keeper, planNum;
	private int width, nameWidth, lineWidth;
	private View lineLeft, lineRight;
	private String planId;
	private WaitingDialog wd;
	private RelativeLayout dateLayout;
	private SharedPreferences loginSettings = null;
	private String userid, deptcode, isCk;
	private String planState;
	private LinearLayout finishBtn, tongbuBtn;
	private LocalPlanResultService localPlanResultService;
	private LocalPlanResult localPlanResult;
	private String pandianStr = "";
	private LocalPandianService localPandianService;
	private ImageView pyxx = null;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		localPlanResultService = new LocalPlanResultService(this);
		localPandianService = new LocalPandianService(getApplicationContext());
		if (planState.equals("0")) {
			this.findViewById(R.id.bottomBtnGroup).setVisibility(View.INVISIBLE);
		}
		if (GlobalParams.LOGIN_TYPE == 1) {
			tongbuBtn.setVisibility(View.GONE);
			finishBtn.setVisibility(View.VISIBLE);
			new GetInfoAt().execute("");
		} else {
			pyxx.setVisibility(View.GONE);
			tongbuBtn.setVisibility(View.GONE);
			finishBtn.setVisibility(View.VISIBLE);
			new GetLoccalInfoAt().execute("");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_result);
		if (getIntent().hasExtra("planState")) {
			planState = getIntent().getStringExtra("planState");
		}
		pyxx = (ImageView)findViewById(R.id.pyxx);
		localPlanResultService = new LocalPlanResultService(this);
		localPandianService = new LocalPandianService(this);
		wd = new WaitingDialog(this);
		planId = this.getIntent().getStringExtra("planId");
		deptcode = this.getIntent().getStringExtra("deptcode");
		isCk = this.getIntent().getStringExtra("isCk");
		loginSettings = this.getSharedPreferences("GDHT_ITASSET_SETTINGS",
				Context.MODE_PRIVATE);
		userid = loginSettings.getString("LOGIN_NAME", "");
		finishBtn = (LinearLayout) this.findViewById(R.id.finish);
		tongbuBtn = (LinearLayout) this.findViewById(R.id.shujutongbu);
		initViews();
		EventBus.getDefault().register(this);
		if (GlobalParams.LOGIN_TYPE == 1) {
			tongbuBtn.setVisibility(View.GONE);
			finishBtn.setVisibility(View.VISIBLE);
			new GetInfoAt().execute("");
		} else {
			tongbuBtn.setVisibility(View.GONE);
			finishBtn.setVisibility(View.VISIBLE);
			new GetLoccalInfoAt().execute("");
		}
	}

	private class GetLoccalInfoAt extends
			AsyncTask<String, Integer, LocalPlanResult> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			wd.show();
		}

		@Override
		protected LocalPlanResult doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return localPlanResultService.getLocalPlanResultByPlanId(planId, userid);
		}

		@Override
		protected void onPostExecute(LocalPlanResult result) {
			super.onPostExecute(result);
			wd.dismiss();
			if (result != null) {
//				Log.i("a", "result = " + result.toString());
				localPlanResult = result;
				name.setText(result.getTitle());
				name.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener() {

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
				if (pandianDate == null || "".equals(pandianDate)
						|| "null".equals(pandianDate)) {
					pandianDate = result.getQdtime();
				}
				if (pandianDate == null || "".equals(pandianDate)
						|| "null".equals(pandianDate)) {
					dateLayout.setVisibility(View.GONE);
				} else {
					pandianDate = pandianDate.substring(0,
							pandianDate.indexOf(" "));
					date.setText(pandianDate);
				}
				if (result.getPy().equals("0")) {
					panyingBtn.setVisibility(View.GONE);
				} else {
					panyingBtn.setVisibility(View.VISIBLE);
				}
				if (result.getPk().equals("0")) {
					pankuiBtn.setVisibility(View.GONE);
				} else {
					pankuiBtn.setVisibility(View.VISIBLE);
				}
				yipanTxt.setText("已盘：" + result.getYp());
				weipanTxt.setText("未盘：" + result.getWp());
				panyingTxt.setText("盘盈：" + result.getPy());
				pankuiTxt.setText("盘亏：" + result.getPk());
				keeper.setText(result.getPersons());
				planNum.setText(result.getNumber());

			} else {
				Toast.makeText(ScanResultActivity.this, "获取服务器数据失败", 0).show();
				ScanResultActivity.this.finish();
			}
		}

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
			return new HttpClientUtil(ScanResultActivity.this).getPlanInfoById(
					ScanResultActivity.this, planId, userid);
		}

		@Override
		protected void onPostExecute(PlanDetail result) {
			super.onPostExecute(result);
			wd.dismiss();
			if (result != null) {
				Log.i("a", "result = " + result.toString());
				name.setText(result.getTitle());
				name.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener() {

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
								name.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							}
						});
				String pandianDate = result.getWctime();
				if (pandianDate == null || "".equals(pandianDate)
						|| "null".equals(pandianDate)) {
					pandianDate = result.getQdtime();
				}
				if (pandianDate == null || "".equals(pandianDate)
						|| "null".equals(pandianDate)) {
					dateLayout.setVisibility(View.GONE);
				} else {
					pandianDate = pandianDate.substring(0,
							pandianDate.indexOf(" "));
					date.setText(pandianDate);
				}
				if (result.getPy().equals("0")) {
					panyingBtn.setVisibility(View.GONE);
				} else {
					panyingBtn.setVisibility(View.VISIBLE);
				}
				if (result.getPk().equals("0")) {
					pankuiBtn.setVisibility(View.GONE);
				} else {
					pankuiBtn.setVisibility(View.VISIBLE);
				}
				yipanTxt.setText("已盘：" + result.getYp());
				weipanTxt.setText("未盘：" + result.getWp());
				panyingTxt.setText("盘盈：" + result.getPy());
				pankuiTxt.setText("盘亏：" + result.getPk());
				keeper.setText(result.getPersons());
				planNum.setText(result.getNumber());

			} else {
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
		planNum = (TextView) this.findViewById(R.id.planNum);
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
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					yipanTxt.setTextColor(Color.WHITE);
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
					yipanTxt.setTextColor(ScanResultActivity.this
							.getResources().getColor(R.color.yipanColor));
				}

				return false;
			}
		});
		yipanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScanResultActivity.this,	ScanResultDetailActivity.class);
				intent.putExtra("type", "1");
				intent.putExtra("planId", planId);
				intent.putExtra("planState", planState);
				if(GlobalParams.LOGIN_TYPE == 2 && localPlanResult != null) {
					intent.putExtra("rfids", localPlanResult.getYpRfids());
				}
				startActivity(intent);
			}
		});
		weipanBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					weipanTxt.setTextColor(Color.WHITE);
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
					weipanTxt.setTextColor(ScanResultActivity.this
							.getResources().getColor(R.color.weipanColor));
				}

				return false;
			}
		});
		weipanBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScanResultActivity.this, ScanResultDetailActivity.class);
				intent.putExtra("type", "0");
				intent.putExtra("planId", planId);
				intent.putExtra("planState", planState);
				if(GlobalParams.LOGIN_TYPE == 2 && localPlanResult != null) {
					intent.putExtra("rfids", localPlanResult.getWpRfids());
				}
				startActivity(intent);
			}
		});
		panyingBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					panyingTxt.setTextColor(Color.WHITE);
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
					panyingTxt.setTextColor(ScanResultActivity.this
							.getResources().getColor(R.color.panyingColor));
				}

				return false;
			}
		});
		panyingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScanResultActivity.this,
						ScanResultDetailActivity.class);
				intent.putExtra("type", "2");
				intent.putExtra("planId", planId);
				intent.putExtra("planState", planState);
				if(GlobalParams.LOGIN_TYPE == 2 && localPlanResult != null) {
					intent.putExtra("rfids", localPlanResult.getPyRfids());
				}
				startActivity(intent);
			}
		});
		pankuiBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					pankuiTxt.setTextColor(Color.WHITE);
				} else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
					pankuiTxt.setTextColor(ScanResultActivity.this
							.getResources().getColor(R.color.pankuiColor));
				}

				return false;
			}
		});
		pankuiBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ScanResultActivity.this,
						ScanResultDetailActivity.class);
				intent.putExtra("type", "3");
				intent.putExtra("planId", planId);
				intent.putExtra("planState", planState);
				if(GlobalParams.LOGIN_TYPE == 2 && localPlanResult != null) {
					intent.putExtra("rfids", localPlanResult.getPkRfids());
				}
				startActivity(intent);
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
			intent.putExtra("planId", planId);
			startActivity(intent);
			break;
		case R.id.finish:
			if(GlobalParams.LOGIN_TYPE == 1) {
				new AlertDialog.Builder(ScanResultActivity.this)
				.setTitle("完成计划？")
				.setNegativeButton("取消", null)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new AsyncTask<Void, Void, Boolean>() {
									WaitingDialog dialog = new WaitingDialog(
											ScanResultActivity.this);

									protected void onPreExecute() {
										dialog.show();
									};

									@Override
									protected Boolean doInBackground(
											Void... params) {

										return new HttpClientUtil(
												ScanResultActivity.this)
												.finishInventoryPlan(
														ScanResultActivity.this,
														planId);
									}

									protected void onPostExecute(
											Boolean result) {
										dialog.dismiss();
										if (result) {
											Toast.makeText(
													ScanResultActivity.this,
													"完成计划请求成功",
													Toast.LENGTH_SHORT)
													.show();
											ScanResultActivity.this
													.finish();
										} else {
											Toast.makeText(
													ScanResultActivity.this,
													"完成计划请求失败",
													Toast.LENGTH_SHORT)
													.show();
										}
									};
								}.execute();
							}
						}).show();

			}else {
//				Toast.makeText(ScanResultActivity.this, GlobalParams.pandian_str, 0).show();
				if(!"".equals(GlobalParams.pandian_str)) {
					localPandianService.save(planId, userid, GlobalParams.pandian_str);
				}
				ScanResultActivity.this.finish();
			}
			break;
		case R.id.pyxx:
			intent = new Intent(this, XinZengScanNewActivity.class);
			intent.putExtra("planId", planId);
			intent.putExtra("userid", userid);
			intent.putExtra("deptcode", deptcode);
			intent.putExtra("isCk", isCk);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		localPandianService.close();
		localPlanResultService.close();
	}

	public void onEvent(RefreshDatas event) {
		new GetInfoAt().execute("");
	}

	
}
