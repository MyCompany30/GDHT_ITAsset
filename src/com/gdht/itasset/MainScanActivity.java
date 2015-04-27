package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainScanActivity extends Activity {
	private Intent intent;
	private ArrayList<PlanAssetInfo> planAssetArrayList = new ArrayList<PlanAssetInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_main);
		Intent intent = getIntent();
		planAssetArrayList = GlobalParams.planAssetInfoList;
		//获取需要盘点的资产列表
		if(intent.hasExtra("dept")){
			String dept = intent.getStringExtra("dept");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}
		if(intent.hasExtra("dept")&&intent.hasExtra("office")){
			String dept = intent.getStringExtra("dept");
			String office = intent.getStringExtra("office");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getOffice().equals(office)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}
		if(intent.hasExtra("dept")&&intent.hasExtra("warehouseArea")){
			String dept = intent.getStringExtra("dept");
			String area = intent.getStringExtra("warehouseArea");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getWarehouseArea().equals(area)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}
		if(intent.hasExtra("dept")&&intent.hasExtra("warehouseArea")&&intent.hasExtra("goodsShelves")){
			String dept = intent.getStringExtra("dept");
			String area = intent.getStringExtra("warehouseArea");
			String shelve = intent.getStringExtra("goodsShelves");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getWarehouseArea().equals(area)&&GlobalParams.planAssetInfoList.get(i).getGoodsShelves().equals(shelve)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}
		
	}
	
	public void btnClick(View view) {
		intent = new Intent();
		intent.putExtra("assetInfoList", planAssetArrayList);
		switch (view.getId()) {
		case R.id.rfidScan:
			intent.setClass(MainScanActivity.this, ScanActivity.class);
			this.startActivity(intent);
			break;
		case R.id.erweiScan:
			intent.setClass(MainScanActivity.this, ErWeiScanActivity.class);
			startActivity(intent);
			break;
		case R.id.localDB:
			intent.setClass(MainScanActivity.this, LocalDBActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}
}
