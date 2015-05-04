package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.db.service.RFIDSDBService;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalDBActivity extends Activity {
	private ListView listView;
	private MyListAdapter rfidListAdapter;
	private ArrayList<String> rfidArray = new ArrayList<String>();
	private ArrayList<PlanAssetInfo> planAssetArrayList = new ArrayList<PlanAssetInfo>();
	private RFIDSDBService rfidsdbService;
	private ProgressDialog pd, deletePd;
	private AlertDialog deleteAd;
	private String deleteTag = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_localdb);
		Intent intent = getIntent();
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
		findViews();
		rfidsdbService = new RFIDSDBService(this);
//		new LoadDataAsyncTask().execute("");
		new LoadDataAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private void findViews() {
		listView = (ListView) this.findViewById(R.id.listView);
		rfidListAdapter = new MyListAdapter();
		listView.setAdapter(rfidListAdapter);
		pd = new ProgressDialog(this);
		pd.setMessage("数据加载中...");
		deletePd = new ProgressDialog(this);
		deletePd.setMessage("数据删除中...");
		deleteAd = new AlertDialog.Builder(this).setTitle("警告").setMessage("您确定删除本地已经保存的数据资料吗？删除后不可恢复。")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deletePd.dismiss();
//						new DeleteAsyncTask().execute(deleteTag);
						new DeleteAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, deleteTag);
					}
					
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deletePd.dismiss();
					}
				})
				.create();
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				deleteTag = rfidArray.get(arg2);
				deleteAd.show();
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(LocalDBActivity.this, RfidCheckActivity.class);
				intent.putExtra("code", rfidArray.get(arg2));
				intent.putExtra("assetInfoList", planAssetArrayList);
				startActivity(intent);
			}
		});
	}
	
	private class LoadDataAsyncTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			rfidArray.clear();
			rfidArray.addAll(rfidsdbService.loadDatas());
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			rfidListAdapter.notifyDataSetChanged();
			pd.dismiss();
		}
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.scan_clear:
			deleteTag = "";
			if(rfidArray.size() > 0) {
				deleteAd.show();
			}
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
				intent.setClass(LocalDBActivity.this, ScanComplateActivity.class);
				intent.putStringArrayListExtra("rfidArray", rfidArray);
				intent.putExtra("assetInfoList", planAssetArrayList);
				LocalDBActivity.this.startActivity(intent);
			}
			break;
		}
	}
	
	private class DeleteAsyncTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			deletePd.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			if("".equals(params[0])) {
				rfidsdbService.deleteAll();
			}else {
				rfidsdbService.deleteByRFID(params[0]);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			deletePd.dismiss();
//			new LoadDataAsyncTask().execute("");
			new LoadDataAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
			TextView rfidTV = (TextView)convertView.findViewById(R.id.rfidCode);
			for(int i = 0; i < planAssetArrayList.size(); i++){
				if(planAssetArrayList.get(i).getRfidnumber().equals(rfidArray.get(position))){
					convertView.setBackgroundColor(Color.parseColor("#d3fac7"));
					break;
				}
				if(i==planAssetArrayList.size()-1){
					convertView.setBackgroundColor(Color.parseColor("#fbc9cc"));
				}
				
			}
			numberTV.setText(position+1+"");
			rfidTV.setText(rfidArray.get(position));
			return convertView;
		}
	}
}
