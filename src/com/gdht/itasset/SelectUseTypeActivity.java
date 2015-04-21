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

public class SelectUseTypeActivity extends Activity {
	private ListView listView = null;
	private String dept = null;
	public static SelectUseTypeActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_use_type);
		instance = this;
		dept = getIntent().getStringExtra("dept");
		ArrayList<String> useType = new ArrayList<String>();
		ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		//deptList
		for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
			if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)){
				deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		//筛选资产状态
		for(int i = 0; i< deptArrayList.size(); i++){
			if(useType.contains(deptArrayList.get(i).getUsetype()))
				continue;
			useType.add(deptArrayList.get(i).getUsetype());
		}
		//设置listView显示数据
		final String [] values = new String[useType.size()];
		for(int i=0;i<useType.size();i++){  
			values[i]=(String)useType.get(i);  
	    }
		String [] keys = new String[values.length];
		for(int i=0;i<values.length;i++){  
			if(values[i].equals("1")){ //库存
				keys[i] = "库存";
			}else if(values[i].equals("2")){  //在运
				keys[i] = "在运";
			}else if(values[i].equals("3")){   //废弃
				keys[i] = "废弃";
			}
	    }
		listView = (ListView)findViewById(R.id.listView);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectUseTypeActivity.this, android.R.layout.simple_list_item_1, keys);
	    listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				Intent intent =  new Intent();
				String usetype = values[arg2];
				intent.putExtra("dept", dept);
				intent.putExtra("usetype", usetype);
				if(usetype.equals("1")){
					//库存备用
					intent.setClass(SelectUseTypeActivity.this, SelectWarehouseAreaActivity.class);
					startActivity(intent);
				}else if(usetype.equals("2")){
					//在运
					intent.setClass(SelectUseTypeActivity.this, SelectOfficeActivity.class);
					startActivity(intent);
				}
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
