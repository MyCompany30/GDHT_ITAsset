package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectOfficeActivity extends Activity {
	private ListView listView = null;
	private String dept = null;
	private String usetype = null;
	public static SelectOfficeActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_goods_shelves);
		instance = this;
		dept = getIntent().getStringExtra("dept");
		usetype = getIntent().getStringExtra("usetype");
		ArrayList<String> offices = new ArrayList<String>();
		ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		//deptList
		for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
			if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)){
				deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		//筛选资产状态
		for(int i = 0; i< deptArrayList.size(); i++){
			if(offices.contains(deptArrayList.get(i).getOffice()))
				continue;
			offices.add(deptArrayList.get(i).getOffice());
		}
		//设置listView显示数据
		final String [] values = new String[offices.size()];
		 for(int i=0;i<offices.size();i++){  
			 values[i]=(String)offices.get(i);  
	     }
		listView = (ListView)findViewById(R.id.listView);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectOfficeActivity.this, android.R.layout.simple_list_item_1, values);
	    listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				Intent intent =  new Intent();
				String office = values[arg2];
				intent.putExtra("dept", dept);
				intent.putExtra("usetype", usetype);
				intent.putExtra("office", office);
				//切换页面
				intent.setClass(SelectOfficeActivity.this, MainScanActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;

		default:
			break;
		}
	}
}
