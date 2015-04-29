package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.adapter.PlanListAdapter;
import com.gdht.itasset.pojo.PlanInfo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PlanListActivity extends Activity {
	
	private ListAdapter listAdapter = null;
	private ListView planListView = null;
	private Activity activity = null;
	private ArrayList<PlanInfo> dataList = null;
	private String name = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.plan_select_view);
		initPlanView();
	}

	private void initPlanView() {
		planListView = (ListView)activity.findViewById(R.id.plan_listView);
		View back = (View) activity.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PlanListActivity.this.finish();
			}
		});
		dataList = (ArrayList<PlanInfo>) getIntent().getSerializableExtra("planList");
		listAdapter = new PlanListAdapter(dataList, activity);
		planListView.setAdapter(listAdapter);
		planListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(activity, PlanActivity.class);
				intent.putExtra("planId", dataList.get(arg2).getId());
				intent.putExtra("operator", name);
				activity.startActivity(intent);
			}
			
		});
	}

}
