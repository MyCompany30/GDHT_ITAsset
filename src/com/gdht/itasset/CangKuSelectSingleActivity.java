package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gdht.itasset.eventbus.SelectCangKuListener;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.CangKuInfo;
import com.gdht.itasset.pojo.DeptInfo;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import de.greenrobot.event.EventBus;

public class CangKuSelectSingleActivity extends Activity {
	// private String[] cangkus = new String[]{"仓库1","仓库2","仓库3","仓库4","仓库5"};
	private ListView listView;
	private MyAdapter adapter;
	private List<DeptInfo> cangKuInfos = new ArrayList<DeptInfo>();
	private WaitingDialog dialog;
	private String rfid = "";
	private int location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_cangku_select);
		rfid = this.getIntent().getStringExtra("rfid");
		location = this.getIntent().getIntExtra("postion", 0);
		GlobalParams.rfid = rfid;
		findViews();
		new GetStoresByUserAsyncTask().execute("");
	}

	private class GetStoresByUserAsyncTask extends
			AsyncTask<String, Integer, List<DeptInfo>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new WaitingDialog(CangKuSelectSingleActivity.this,	R.style.TRANSDIALOG);
			dialog.show();
		}

		@Override
		protected List<DeptInfo> doInBackground(String... arg0) {
			return new HttpClientUtil(CangKuSelectSingleActivity.this).getAllDepts(
					CangKuSelectSingleActivity.this, GlobalParams.username);
		}

		@Override
		protected void onPostExecute(List<DeptInfo> result) {
			dialog.dismiss();
			if (result.size() > 0) {
				// for(CangKuInfo in : result) {
				// Log.i("a", "info = " + in.toString());
				// }
				cangKuInfos.addAll(result);
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void findViews() {
		this.listView = (ListView) this.findViewById(R.id.listView);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String key = cangKuInfos.get(arg2).getKey();
				String value = cangKuInfos.get(arg2).getValue();
//				GlobalParams.cangKuValue = key;
//				GlobalParams.cangKuName = cangKuInfos.get(arg2).getValue();
				String isCk = "1";
				if (key.startsWith("asset_ck")) {
					isCk = "1";
				} else {
					isCk = "2";
				}
				EventBus.getDefault().post(new SelectCangKuListener(location, key, value, isCk));
				CangKuSelectSingleActivity.this.finish();
			}
		});
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cangKuInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return cangKuInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			convertView = getLayoutInflater().inflate(R.layout.item_cangku_listview, null);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(cangKuInfos.get(arg0).getValue());
			return convertView;
		}

	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}

}
