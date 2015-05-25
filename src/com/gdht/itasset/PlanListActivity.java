package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.adapter.GuideActivityPagerViewAdapter;
import com.gdht.itasset.adapter.PlanListAdapterNew;
import com.gdht.itasset.db.service.LocalPandianService;
import com.gdht.itasset.db.service.LocalPlanResultService;
import com.gdht.itasset.db.service.LocalPlanService;
import com.gdht.itasset.db.service.LocalRealNameService;
import com.gdht.itasset.db.service.LocalStockService;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.LocalPandian;
import com.gdht.itasset.pojo.LocalPlanResult;
import com.gdht.itasset.pojo.PlanInfo;
import com.gdht.itasset.pojo.RealName;
import com.gdht.itasset.pojo.StockItemNew;
import com.gdht.itasset.receiver.ConnectionChangeReceiver;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.utils.OutputDBUtils;
import com.gdht.itasset.widget.WaitingDialog;
import com.google.gson.Gson;

public class PlanListActivity extends Activity {
	private ViewPager viewPager;
	private List<PlanInfo> zzPlanInfos = null;
	private List<PlanInfo> ypPlanInfos = null;
	private ArrayList<View> views;
	private LayoutInflater inflater;
	private ListView zzListView, ypListView;
	private PlanListAdapterNew zzAdapter, ypAdapter;
	private GuideActivityPagerViewAdapter viewAdapter;
	private LinearLayout zzBtn, ypBtn;
	private LocalStockService localStockService;
	private LocalPlanService localPlanService;
	private LocalPlanResultService localPlanResultService;
	private LocalRealNameService localRealNameService;
	private LocalPandianService localPandianService;
	private AlertDialog ad;
	private String name;
	private ArrayList<PlanInfo> plans;
	private int currentSelected;
	private Long assetNumber = 0l, planNumber = 0l, planResultNumber = 0l;
	private ImageView shujukugengxin;
	private View shujutongbu;
	private String userid;
	private SharedPreferences loginSettings;
	private WaitingDialog wd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_plan_list);
		loginSettings = this.getSharedPreferences("GDHT_ITASSET_SETTINGS",
				Context.MODE_PRIVATE);
		wd = new WaitingDialog(this);
		userid = loginSettings.getString("LOGIN_NAME", "");
		localPlanResultService = new LocalPlanResultService(this);
		localRealNameService = new LocalRealNameService(this);
		localPandianService = new LocalPandianService(this);
		name = getIntent().getStringExtra("name");
		viewPager = (ViewPager) this.findViewById(R.id.viewPager);
		zzBtn = (LinearLayout) this.findViewById(R.id.zhengzai);
		ypBtn = (LinearLayout) this.findViewById(R.id.yipan);
		shujukugengxin = (ImageView) this.findViewById(R.id.shujukugengxin);
		shujutongbu = this.findViewById(R.id.shujutongbu);
		inflater = LayoutInflater.from(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!GlobalParams.isOffLineDialogRefresh){
	//		Toast.makeText(this, "onResume", 0).show();
			zzPlanInfos = new ArrayList<PlanInfo>();
			ypPlanInfos = new ArrayList<PlanInfo>();
			localStockService = new LocalStockService(this);
			localPlanService = new LocalPlanService(this);
			if (localPandianService.getCount()) {
				shujutongbu.setVisibility(View.VISIBLE);
			} else {
				shujutongbu.setVisibility(View.GONE);
			}
			initPagerView();
			switch (currentSelected) {
			case 0:
				currentSelected = 0;
				zzBtn.setBackgroundResource(R.drawable.tab_selected);
				ypBtn.setBackgroundResource(R.drawable.tab_normal);
				break;
			case 1:
				currentSelected = 1;
				zzBtn.setBackgroundResource(R.drawable.tab_normal);
				ypBtn.setBackgroundResource(R.drawable.tab_selected);
				break;
			}
			/*
			 * if (GlobalParams.LOGIN_TYPE == 1) {
			 * shujukugengxin.setVisibility(View.VISIBLE); } else {
			 * hujukugengxin.setVisibility(View.GONE); }
			 */
			viewPager.setCurrentItem(currentSelected);
			/*
			 * if(PlanListActivity.this.getResources().getConfiguration().orientation
			 * == 0){ shujukugengxin.setImageResource(R.drawable.
			 * selector_shujukugengxin_novalue_land); }else {
			 * shujukugengxin.setImageResource
			 * (R.drawable.selector_shujukugengxin_novalue); }
			 */
		}
		//重置标记
		GlobalParams.isOffLineDialogRefresh = false;
	}

	private void initPagerView() {
		views = new ArrayList<View>();
		View view = inflater.inflate(R.layout.activity_plan_views, null);
		zzListView = (ListView) view.findViewById(R.id.listView);
		zzListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final String planId = zzPlanInfos.get(arg2).getId();
				Intent intent = new Intent(PlanListActivity.this,
						ScanResultActivity.class);
				intent.putExtra("planId", planId);
				intent.putExtra("planState", "1");
				intent.putExtra("deptcode", zzPlanInfos.get(arg2).getDeptcode());
				intent.putExtra("isCk", zzPlanInfos.get(arg2).getType());
				startActivity(intent);
				// new AsyncTask<Void, Void, Void>(){
				//
				// @Override
				// protected Void doInBackground(Void... params) {
				// new
				// HttpClientUtil(PlanListActivity.this).getPlanInfoById(PlanListActivity.this,
				// planId);
				// return null;
				// }
				//
				// }.execute();

			}
		});
		views.add(view);
		View view2 = inflater.inflate(R.layout.activity_plan_views, null);
		ypListView = (ListView) view2.findViewById(R.id.listView);
		ypListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final String planId = ypPlanInfos.get(arg2).getId();
				Intent intent = new Intent(PlanListActivity.this,
						ScanResultActivity.class);
				intent.putExtra("planState", "0");
				intent.putExtra("planId", planId);
				startActivity(intent);

			}
		});
		views.add(view2);
		viewAdapter = new GuideActivityPagerViewAdapter(views);
		viewPager.setAdapter(viewAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					currentSelected = 0;
					zzBtn.setBackgroundResource(R.drawable.tab_selected);
					ypBtn.setBackgroundResource(R.drawable.tab_normal);
					break;
				case 1:
					currentSelected = 1;
					zzBtn.setBackgroundResource(R.drawable.tab_normal);
					ypBtn.setBackgroundResource(R.drawable.tab_selected);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (GlobalParams.LOGIN_TYPE == 2) {
			new GetLocalPlanListAt().execute("");
		} else {
			new GetPlanListAt().execute("");
		}

	}

	private class GetLocalPlanListAt extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			plans = localPlanService.getPlanInfoByUsername(name);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			for (int i = 0; i < plans.size(); i++) {
				if (plans.get(i).getPlanstate().equals("0")) {
					// 已完成
					ypPlanInfos.add(plans.get(i));
				} else if (plans.get(i).getPlanstate().equals("1")) {
					// 进行中
					zzPlanInfos.add(plans.get(i));
				}
			}
			// PlanInfo pi = new PlanInfo();
			// pi.setId("aaaa");
			// zzPlanInfos.add(pi);
			zzAdapter = new PlanListAdapterNew(PlanListActivity.this,
					zzPlanInfos, userid, "1", localPandianService);
			zzListView.setAdapter(zzAdapter);

			ypAdapter = new PlanListAdapterNew(PlanListActivity.this,
					ypPlanInfos);
			ypListView.setAdapter(ypAdapter);
		}
	}

	private class GetPlanListAt extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			plans = new HttpClientUtil(PlanListActivity.this).getPlans(
					PlanListActivity.this, name);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			for (int i = 0; i < plans.size(); i++) {
				if (plans.get(i).getPlanstate().equals("0")) {
					// 已完成
					ypPlanInfos.add(plans.get(i));
				} else if (plans.get(i).getPlanstate().equals("1")) {
					// 进行中
					zzPlanInfos.add(plans.get(i));
				}
			}
			// PlanInfo pi = new PlanInfo();
			// pi.setId("aaaa");
			// zzPlanInfos.add(pi);
			zzAdapter = new PlanListAdapterNew(PlanListActivity.this,
					zzPlanInfos, userid, "1", localPandianService);
			zzListView.setAdapter(zzAdapter);

			ypAdapter = new PlanListAdapterNew(PlanListActivity.this,
					ypPlanInfos);
			ypListView.setAdapter(ypAdapter);
		}

	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.zhengzai:
			viewPager.setCurrentItem(0);
			break;
		case R.id.yipan:
			viewPager.setCurrentItem(1);
			break;
		case R.id.scanBtn:
			Intent intent = new Intent(this, ScanActivityNew.class);
			startActivity(intent);
			break;
		case R.id.shujukugengxin:
			new RefreshAssetDataSourceAt().execute("");
			break;
		case R.id.shujutongbu:
			if(GlobalParams.LOGIN_TYPE == 1) {
				List<LocalPandian> lps = localPandianService
						.getLocalPandian(userid);
				if (lps.size() <= 0) {
					Toast.makeText(PlanListActivity.this, "暂时没有未提交的盘点数据. username = " +userid, 0)	.show();
				} else {
					Gson gson = new Gson();
					String str = gson.toJson(lps);
					str = str.replace("\\u0027", "'");
					Log.i("a", "json = " + str);
					new DataCommitAt().execute(str);
				}
			}else {
				OutputDBUtils outputDBUtils = new OutputDBUtils(this);
				outputDBUtils.copyDatabase();
			}
			break;
		}
	}

	private class DataCommitAt extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			wd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return new HttpClientUtil(PlanListActivity.this)
					.plupdaterfidandplan(PlanListActivity.this, params[0]);
			
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			wd.dismiss();
			if ("1".equals(result)) {
				localPandianService.deleteByUser(userid);
				onResume();
				Toast.makeText(PlanListActivity.this, "数据提交成功!", 0).show();
			} else {
				Toast.makeText(PlanListActivity.this, "数据提交失败!", 0).show();
			}
		}

	}

	private ProgressDialog pd;

	private class RefreshAssetDataSourceAt extends
			AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(PlanListActivity.this);
			pd.setTitle("提示");
			pd.setMessage("资产信息数据库更新中...");
			pd.setCancelable(true);
			pd.show();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ArrayList<StockItemNew> sis = new HttpClientUtil(
					PlanListActivity.this).getAssetInfos(PlanListActivity.this);
			if (sis == null) {
				return 0l;
			} else {
				return localStockService.save(sis);
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			assetNumber = result;
			new RefreshPlanDataSourceAt().execute("");
		}
	}

	private class RefreshPlanDataSourceAt extends
			AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			ArrayList<PlanInfo> pis = new HttpClientUtil(PlanListActivity.this)
					.getAllCheckPlan(PlanListActivity.this);
			if (pis == null) {
				return 0l;
			} else {
				return localPlanService.save(pis);
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			planNumber = result;
			new RefreshResultDataSourceAt().execute("");
		}
	}

	private class RefreshResultDataSourceAt extends
			AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			ArrayList<LocalPlanResult> lprs = new HttpClientUtil(
					PlanListActivity.this)
					.getAllCheckPlanInfo(PlanListActivity.this);
			if (lprs == null) {
				return 0l;
			} else {
				return localPlanResultService.save(lprs);
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			planResultNumber = result;
			new RefreshRealNameSourceAt().execute("");
		}
	}

	private class RefreshRealNameSourceAt extends
			AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			ArrayList<RealName> lprs = new HttpClientUtil(PlanListActivity.this)
					.nameCompare(PlanListActivity.this);
			if (lprs == null) {
				return 0l;
			} else {
				return localRealNameService.save(lprs);
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
				pd = null;
			}
			String contentStr = "资产数据更新完成，共计" + assetNumber
					+ "条。\n盘点计划数据更新完成，共计" + planNumber + "条。\n盘点结果数据更新完成，共计"
					+ planResultNumber + "条。";
			initAd(contentStr);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		localStockService.close();
		localPlanService.close();
		localPlanResultService.close();
		//localPandianService.close();
	}

	private void initAd(String contentStr) {

		View dialogView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.dialog_datasource_finish, null);

		ad = new AlertDialog.Builder(this).create();
		ad.setCanceledOnTouchOutside(false);

		ImageView close = (ImageView) dialogView.findViewById(R.id.close);
		ImageView sure = (ImageView) dialogView.findViewById(R.id.sure);
		TextView content = (TextView) dialogView.findViewById(R.id.content);
		content.setText(contentStr);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ad.dismiss();
				ad = null;
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ad.dismiss();
				ad = null;
			}
		});
		ad.show();
		ad.getWindow().setContentView((RelativeLayout) dialogView);
	}

}
