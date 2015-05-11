package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gdht.itasset.adapter.GuideActivityPagerViewAdapter;
import com.gdht.itasset.adapter.PlanListAdapterNew;
import com.gdht.itasset.pojo.PlanInfoNew;

public class PlanListActivity extends Activity {
	private ViewPager viewPager;
	private List<PlanInfoNew> zzPlanInfos = new ArrayList<PlanInfoNew>();
	private List<PlanInfoNew> ypPlanInfos = new ArrayList<PlanInfoNew>();
	private ArrayList<View> views;
	private LayoutInflater inflater;
	private ListView zzListView, ypListView;
	private PlanListAdapterNew zzAdapter, ypAdapter;
	private GuideActivityPagerViewAdapter viewAdapter;
	private LinearLayout zzBtn, ypBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_plan_list);
		viewPager = (ViewPager) this.findViewById(R.id.viewPager);
		zzBtn = (LinearLayout) this.findViewById(R.id.zhengzai);
		ypBtn = (LinearLayout) this.findViewById(R.id.yipan);
		inflater = LayoutInflater.from(this);
		initPagerView();
	}
	
	private void initPagerView() {
		views = new ArrayList<View>();
		View view = inflater.inflate(R.layout.activity_plan_views,
				null);
		zzListView = (ListView) view.findViewById(R.id.listView);
		zzListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
		views.add(view);
		View view2 = inflater.inflate(R.layout.activity_plan_views,
				null);
		ypListView = (ListView) view2.findViewById(R.id.listView);
		ypListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
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
					zzBtn.setBackgroundResource(R.drawable.tab_selected);
					ypBtn.setBackgroundResource(R.drawable.tab_normal);
					break;
				case 1:
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
		new GetPlanListAt().execute("");
	}
	
	private class GetPlanListAt extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... arg0) {
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			PlanInfoNew pi = new PlanInfoNew();
			pi.setName("未盘点计划1");
			pi.setZk(true);
			pi.setCangku("仓库1");
			zzPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("未盘点计划2");
			pi.setZk(true);
			pi.setCangku("仓库2");
			zzPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("未盘点计划3");
			pi.setZk(false);
			pi.setCangku("仓库3");
			zzPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("未盘点计划4");
			pi.setZk(true);
			pi.setCangku("仓库4");
			zzPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("未盘点计划5");
			pi.setZk(true);
			pi.setCangku("仓库5");
			zzPlanInfos.add(pi);
			zzAdapter = new PlanListAdapterNew(PlanListActivity.this, zzPlanInfos);
			zzListView.setAdapter(zzAdapter);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划1");
			pi.setZk(true);
			pi.setCangku("仓库1");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划2");
			pi.setZk(true);
			pi.setCangku("仓库2");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划3");
			pi.setZk(false);
			pi.setCangku("仓库3");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划4");
			pi.setZk(true);
			pi.setCangku("仓库4");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划5");
			pi.setZk(true);
			pi.setCangku("仓库5");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划6");
			pi.setZk(true);
			pi.setCangku("仓库6");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划7");
			pi.setZk(true);
			pi.setCangku("仓库7");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划8");
			pi.setZk(false);
			pi.setCangku("仓库8");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划9");
			pi.setZk(true);
			pi.setCangku("仓库9");
			ypPlanInfos.add(pi);
			pi = new PlanInfoNew();
			pi.setName("已盘点计划10");
			pi.setZk(true);
			pi.setCangku("仓库10");
			ypPlanInfos.add(pi);
			ypAdapter = new PlanListAdapterNew(PlanListActivity.this, ypPlanInfos);
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
		}
	}
	
}
































