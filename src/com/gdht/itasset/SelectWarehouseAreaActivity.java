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

public class SelectWarehouseAreaActivity extends Activity {
	private ListView listView = null;
	private String dept = null;
	private String usetype = null;
	public static SelectWarehouseAreaActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_warehouse_area);
		instance = this;
		dept = getIntent().getStringExtra("dept");
		usetype = getIntent().getStringExtra("usetype");
		ArrayList<String> warehouseArea = new ArrayList<String>();
		ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		//deptList
		for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
			if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)){
				deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		//筛选资产状态
		for(int i = 0; i< deptArrayList.size(); i++){
			if(warehouseArea.contains(deptArrayList.get(i).getWarehouseArea()))
				continue;
			warehouseArea.add(deptArrayList.get(i).getWarehouseArea());
		}
		//设置listView显示数据
		final String [] values = new String[warehouseArea.size()];
		 for(int i=0;i<warehouseArea.size();i++){
			 values[i]=(String)warehouseArea.get(i);  
	     }
		listView = (ListView)findViewById(R.id.listView);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectWarehouseAreaActivity.this, android.R.layout.simple_list_item_1, values);
	    listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				Intent intent =  new Intent();
				String warehouseArea = values[arg2];
				intent.putExtra("dept", dept);
				intent.putExtra("usetype", usetype);
				intent.putExtra("warehouseArea", warehouseArea);
				//切换页面
				intent.setClass(SelectWarehouseAreaActivity.this, SelectGoodsShelvesActivity.class);
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
