package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gdht.itasset.adapter.SubAssetTypeListAdapter;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.ZiChanZiFenLeiInfo;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

public class XinZengSubActivity extends Activity {
	
	private TextView titleTV = null;
	private ListView subTypeListView = null;
	private SubAssetTypeListAdapter listAdapter = null;
//	private ArrayList<String> subTypeArray = null;
	private ArrayList<ZiChanZiFenLeiInfo> infos = new ArrayList<ZiChanZiFenLeiInfo>();
	private Intent intent;
	private String cangKuValue = "";
	private String ziChanFenLei = "";
	private WaitingDialog dialog;
	private String ziChanFenLeiValue = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xin_zeng_sub);
		cangKuValue = this.getIntent().getStringExtra("cangKuValue");
		ziChanFenLei = this.getIntent().getStringExtra("ziChanFenLei");
		ziChanFenLeiValue = this.getIntent().getStringExtra("ziChanFenLeiValue");
		//设置标题
		titleTV = (TextView)findViewById(R.id.title_tv);
		titleTV.setText(ziChanFenLeiValue);
		subTypeListView = (ListView)findViewById(R.id.assetType_sub_listView);
		listAdapter = new SubAssetTypeListAdapter(infos, XinZengSubActivity.this);
		subTypeListView.setAdapter(listAdapter);
		new getAssetTypesByPTypeAndUserAs().execute("");
		subTypeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				GlobalParams.zichanzifenlei = infos.get(position).getKey();
				
				if(GlobalParams.rfid != null && !"".equals(GlobalParams.rfid)) {
					intent = new Intent(XinZengSubActivity.this, XinZengHaveRfidActivity.class);
					startActivity(intent);
				}else {
					intent = new Intent(XinZengSubActivity.this, XinZengMainScanActivity.class);
					startActivity(intent);
				}
			}
		});
		
		
	}

	private class getAssetTypesByPTypeAndUserAs extends AsyncTask<String, Integer, List<ZiChanZiFenLeiInfo>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new WaitingDialog(XinZengSubActivity.this,R.style.TRANSDIALOG);
			dialog.show();
		}
		
		@Override
		protected List<ZiChanZiFenLeiInfo> doInBackground(String... arg0) {
			return new HttpClientUtil(XinZengSubActivity.this).getAssetTypesByPTypeAndUser(XinZengSubActivity.this, GlobalParams.username, cangKuValue, ziChanFenLei);
		}
		
		@Override
		protected void onPostExecute(List<ZiChanZiFenLeiInfo> result) {
			dialog.dismiss();
			if(result.size() > 0){
				infos.addAll(result);
				listAdapter.notifyDataSetChanged();
			}
		}
		
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






























