package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.db.service.RFIDSDBService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ErWeiScanActivity extends Activity {
	private Intent intent;
	private ListView listView;
	private MyListAdapter rfidListAdapter;
	private ArrayList<String> rfidArray = new ArrayList<String>();
	private RFIDSDBService rfidsdbService;
	private ProgressDialog pd;
	private SaveRFIDAsyncTask asyncTask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_erwei);
		rfidsdbService = new RFIDSDBService(this);
		pd = new ProgressDialog(this);
		pd.setMessage("数据保存中...");
		findViews();
	}

	private void findViews() {
		listView = (ListView) this.findViewById(R.id.listView);
		rfidListAdapter = new MyListAdapter();
		listView.setAdapter(rfidListAdapter);
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
			TextView rfidTV = (TextView)convertView.findViewById(R.id.rfidCode);
			numberTV.setText(position+1+"");
			rfidTV.setText(rfidArray.get(position));
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
				rfidArray.add(code);
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
