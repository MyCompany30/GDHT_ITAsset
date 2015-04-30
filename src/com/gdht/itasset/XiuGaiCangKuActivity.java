package com.gdht.itasset;

import java.util.ArrayList;
import java.util.HashMap;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.utils.GlobalParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XiuGaiCangKuActivity extends Activity {
	private TextView rfidTV = null;
	private View ck = null;
	private TextView ck_code = null;
	private EditText keeperEdt = null;
	private EditText detilEdt = null;
	private View duihao = null;
	private View duihao_ = null;
	private View shanchu = null;
	private View shanchu_ = null;
	private ArrayList<String> keyArray = null;
	private ArrayList<String> valueArray = null;
	private String selectedCk = null; 
	private View mainView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainView = getLayoutInflater().inflate(R.layout.activity_xiu_gai_cang_ku, null);
		setContentView(mainView);
		rfidTV = (TextView)mainView.findViewById(R.id.rfid_code);
		ck = mainView.findViewById(R.id.ck);
		ck_code = (TextView)mainView.findViewById(R.id.ck_code);
		keeperEdt = (EditText)mainView.findViewById(R.id.person_code);
		detilEdt = (EditText)mainView.findViewById(R.id.add_content);
		detilEdt.setText(getIntent().getStringExtra("detil"));
		duihao = mainView.findViewById(R.id.duihao);
		duihao_ = mainView.findViewById(R.id.duihao_);
		shanchu = mainView.findViewById(R.id.shanchu);
		shanchu_ = mainView.findViewById(R.id.shanchu_);
		//事件监听
		DuiHaoClickListener duiHaoClickListener = new DuiHaoClickListener();
		ShanChuClickListener shanChuClickListener = new ShanChuClickListener();
		duihao.setOnClickListener(duiHaoClickListener);
		duihao_.setOnClickListener(duiHaoClickListener);
		shanchu.setOnClickListener(shanChuClickListener);
		shanchu_.setOnClickListener(shanChuClickListener);
		//显示rfid编号 和 当前登录人作为负责人
		rfidTV.setText(getIntent().getStringExtra("rfid"));
		keeperEdt.setText(GlobalParams.username);
		selectedCk = getIntent().getStringExtra("dept");
		getArrayValues();
		ck_code.setText(selectedCk);
		//选择仓库
		ck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>(){
					@Override
					protected Void doInBackground(Void... params) {
						
						HashMap<String, ArrayList<String>> map = new HttpClientUtil(XiuGaiCangKuActivity.this).getWarehouse(XiuGaiCangKuActivity.this);
						keyArray = map.get("key");
						valueArray = map.get("value");
						return null;
					}
					protected void onPostExecute(Void result) {
						XiuGaiCangKuActivity.this.setContentView(R.layout.select_cangku);
						ListView listView = (ListView) XiuGaiCangKuActivity.this.findViewById(R.id.listView);
						String [] values = new String[valueArray.size()];
						 for(int i=0;i<valueArray.size();i++){  
							 values[i]=(String)valueArray.get(i);  
					     }
						 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(XiuGaiCangKuActivity.this, android.R.layout.simple_list_item_1, values);
						 listView.setAdapter(arrayAdapter);
						 listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								selectedCk = valueArray.get(arg2);
								setContentView(mainView);
								ck_code.setText(selectedCk);
							}
							 
						});
					};
					
				}.execute();
				
			}
		});
		
	}

	private void getArrayValues() {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				
				HashMap<String, ArrayList<String>> map = new HttpClientUtil(XiuGaiCangKuActivity.this).getWarehouse(XiuGaiCangKuActivity.this);
				keyArray = map.get("key");
				valueArray = map.get("value");
				return null;
			}
			
		}.execute();
	}

	class DuiHaoClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			final String ck = keyArray.get(valueArray.indexOf(ck_code.getText().toString()));  //仓库key
			final String keeper = keeperEdt.getText().toString();  //负责人
			final String detil = detilEdt.getText().toString();  //描述
			final String usetype = XiuGaiCangKuActivity.this.getIntent().getStringExtra("usetype"); //使用状态
			final String dept = XiuGaiCangKuActivity.this.getIntent().getStringExtra("dept");
			final String assetInfoId = getIntent().getStringExtra("assetInfoId");
			//发送处理请求
			new AsyncTask<Void, Void, String>() {
				
				protected void onPreExecute() {
					
				};
				@Override
				protected String doInBackground(Void... params) {
					return new HttpClientUtil(XiuGaiCangKuActivity.this).updateAssetInfoCK(XiuGaiCangKuActivity.this, assetInfoId, ck, keeper, usetype, detil);
				}
				protected void onPostExecute(String result) {
					if(result.equals("1")){
						Toast.makeText(XiuGaiCangKuActivity.this, "处理成功", Toast.LENGTH_SHORT).show();
						//GlobalParams.planAssetInfoList = new HttpClientUtil(XiuGaiCangKuActivity.this).getPlanInfoById(XiuGaiCangKuActivity.this, GlobalParams.planId);
						XiuGaiCangKuActivity.this.finish();
					}else{
						Toast.makeText(XiuGaiCangKuActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
					}
					
				};
			}.execute();
		}
		
	}
	class ShanChuClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			XiuGaiCangKuActivity.this.finish();
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
