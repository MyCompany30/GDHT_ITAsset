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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectDeptActivity extends Activity {
	private ListView listView = null;
	public static SelectDeptActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_dept);
		instance = this;
		ArrayList<String> deptValueArray = new ArrayList<String>();
		//ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		//筛选dept
		for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
			if(deptValueArray.contains(GlobalParams.planAssetInfoList.get(i).getDept()))
				continue;
			deptValueArray.add(GlobalParams.planAssetInfoList.get(i).getDept());
			//deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
		}
		//设置listView显示数据
		final String [] values = new String[deptValueArray.size()];
		 for(int i=0;i<deptValueArray.size();i++){  
			 values[i]=(String)deptValueArray.get(i);  
	     }
		listView = (ListView)findViewById(R.id.listView);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectDeptActivity.this, android.R.layout.simple_list_item_1, values);
	    listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				Intent intent =  new Intent();
				String dept = values[arg2];
				intent.putExtra("dept", dept);
				intent.setClass(SelectDeptActivity.this, SelectUseTypeActivity.class);
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
