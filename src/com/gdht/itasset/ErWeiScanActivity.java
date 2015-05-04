package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.db.service.RFIDSDBService;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ErWeiScanActivity extends Activity {
	private Intent intent;
	private ListView listView;
	private MyListAdapter rfidListAdapter;
	private ArrayList<String> rfidArray = new ArrayList<String>();
	private ArrayList<PlanAssetInfo> planAssetArrayList = new ArrayList<PlanAssetInfo>();
	private RFIDSDBService rfidsdbService;
	private ProgressDialog pd;
	private SaveRFIDAsyncTask asyncTask;
	private String checkStr;
	private LinearLayout complateBtn;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_erwei);
		//planAssetArrayList = (ArrayList<PlanAssetInfo>) getIntent().getSerializableExtra("assetInfoList");
		checkStr = this.getIntent().getStringExtra("check");
		Intent intent = getIntent();
		//获取需要盘点的资产列表
		//获取需要盘点的资产列表

		if(intent.hasExtra("dept")&&intent.hasExtra("warehouseArea")&&intent.hasExtra("goodsShelves")&&(!intent.getStringExtra("dept").equals(""))&&(!intent.getStringExtra("warehouseArea").equals(""))&&(!intent.getStringExtra("goodsShelves").equals(""))){
			String dept = intent.getStringExtra("dept");
			String area = intent.getStringExtra("warehouseArea");
			String shelve = intent.getStringExtra("goodsShelves");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getWarehouseArea().equals(area)&&GlobalParams.planAssetInfoList.get(i).getGoodsShelves().equals(shelve)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else if(intent.hasExtra("dept")&&intent.hasExtra("warehouseArea")&&(!intent.getStringExtra("dept").equals(""))&&(!intent.getStringExtra("warehouseArea").equals(""))){
			String dept = intent.getStringExtra("dept");
			String area = intent.getStringExtra("warehouseArea");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getWarehouseArea().equals(area)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else if(intent.hasExtra("dept")&&intent.hasExtra("office")&&(!intent.getStringExtra("dept").equals(""))&&(!intent.getStringExtra("office").equals(""))){
			String dept = intent.getStringExtra("dept");
			String office = intent.getStringExtra("office");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getOffice().equals(office)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else if(intent.hasExtra("dept")&&(!intent.getStringExtra("dept").equals(""))){
			String dept = intent.getStringExtra("dept");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else{
			planAssetArrayList = GlobalParams.planAssetInfoList;
		}
		rfidsdbService = new RFIDSDBService(this);
		pd = new ProgressDialog(this);
		pd.setMessage("数据保存中...");
		findViews();
		if(checkStr != null && "check".equals(checkStr)) {
			complateBtn.setVisibility(View.GONE);
		}
	}

	private void findViews() {
		listView = (ListView) this.findViewById(R.id.listView);
		complateBtn = (LinearLayout) this.findViewById(R.id.scan_complate);
		rfidListAdapter = new MyListAdapter();
		listView.setAdapter(rfidListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ErWeiScanActivity.this, RfidCheckActivity.class);
				intent.putExtra("code", rfidArray.get(arg2));
				intent.putExtra("assetInfoList", planAssetArrayList);
				startActivity(intent);
			}
		});
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.scan_start_btn:
			intent = new Intent(ErWeiScanActivity.this,
					ErWeiScanCaptureActivity.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.back:
			this.finish();
			break;
		case R.id.scan_clear:
			rfidArray.removeAll(rfidArray);
			rfidListAdapter.notifyDataSetChanged();
			break;
		case R.id.scan_complate:
			if(rfidArray != null && rfidArray.size() > 0) {
//				asyncTask = new SaveRFIDAsyncTask();
//				asyncTask.execute("");
				pd.show();
				for(String s : rfidArray) {
					Log.i("a", "rfid = " + s);
					rfidsdbService.saveRFID(s);
				}
				pd.dismiss();
				Intent intent = new Intent();
				intent.setClass(ErWeiScanActivity.this, ScanComplateActivity.class);
				intent.putStringArrayListExtra("rfidArray", rfidArray);
				intent.putExtra("assetInfoList", planAssetArrayList);
				ErWeiScanActivity.this.startActivity(intent);
			}
			break;
		}
	}
	
	private class SaveRFIDAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.i("a", "onPreExecutes");
//			pd.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			Log.i("a", "rfidArray.size = " + rfidArray.size());
			for(String s : rfidArray) {
				Log.i("a", "rfid = " + s);
				rfidsdbService.saveRFID(s);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			pd.dismiss();
			Intent intent = new Intent();
			intent.setClass(ErWeiScanActivity.this, ScanComplateActivity.class);
			intent.putStringArrayListExtra("rfidArray", rfidArray);
			intent.putExtra("assetInfoList", planAssetArrayList);
			ErWeiScanActivity.this.startActivity(intent);
		}
	}
	
	class MyListAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rfidArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return rfidArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.rfid_listitem, null);
			TextView numberTV = (TextView)convertView.findViewById(R.id.number);
			TextView rfidTitleTv = (TextView)convertView.findViewById(R.id.rfidTitle);
			TextView rfidTV = (TextView)convertView.findViewById(R.id.rfidCode);
			numberTV.setText(position+1+"");
			rfidTV.setText(rfidArray.get(position));
			for(int i = 0; i < planAssetArrayList.size(); i++){
				if(planAssetArrayList.get(i).getRfidnumber().equals(rfidArray.get(position))){
					rfidTitleTv.setText((planAssetArrayList.get(i).getAssetName()));
					convertView.setBackgroundColor(Color.parseColor("#d3fac7"));
					break;
				}
				if(i==planAssetArrayList.size()-1){
					convertView.setBackgroundColor(Color.parseColor("#fbc9cc"));
				}
				
			}
			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 100:
			Bundle bundle = data.getExtras();
			String code = bundle.getString("result");
			if(rfidArray.contains(code)){
				Toast.makeText(ErWeiScanActivity.this, "已经存在!", 0).show();
			}else {
				rfidArray.add(code + "0000000");
//				rfidArray.add(code);
				rfidListAdapter.notifyDataSetChanged();
			}
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		rfidsdbService.closeDB();
	}
}
