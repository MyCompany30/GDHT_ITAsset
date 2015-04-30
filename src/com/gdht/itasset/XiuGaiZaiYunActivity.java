package com.gdht.itasset;

import java.util.ArrayList;
import java.util.HashMap;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.utils.GlobalParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XiuGaiZaiYunActivity extends Activity {
	private TextView rfidTV = null;
	private View detp = null;
	private TextView deptTv = null;
	private View office = null;
	private TextView officeTv = null;
	private EditText keeperEdt = null;
	private EditText detilEdt = null;
	private View duihao = null;
	private View duihao_ = null;
	private View shanchu = null;
	private View shanchu_ = null;
	private ArrayList<String> deptKeyArray = null;
	private ArrayList<String> deptValueArray = null;
	private ArrayList<String> officeKeyArray = null;
	private ArrayList<String> officeValueArray = null;
	private String selectedDept = null; 
	private String selectedOffice = null;
	private String selectedDeptKey = null;
	private View mainView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainView = getLayoutInflater().inflate(R.layout.activity_xiu_gai_zai_yun, null);
		setContentView(mainView);
		rfidTV = (TextView)mainView.findViewById(R.id.rfid_code);
		detp = mainView.findViewById(R.id.dept);
		deptTv = (TextView)mainView.findViewById(R.id.dept_code);
		office = mainView.findViewById(R.id.office);
		officeTv = (TextView)mainView.findViewById(R.id.office_code);
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
		selectedDept = getIntent().getStringExtra("dept");
		selectedOffice = getIntent().getStringExtra("office");
		getArrayValues();
		deptTv.setText(selectedDept);
		//选择仓库
		detp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>(){
					@Override
					protected Void doInBackground(Void... params) {
						
						HashMap<String, ArrayList<String>> map = new HttpClientUtil(XiuGaiZaiYunActivity.this).getDept(XiuGaiZaiYunActivity.this);
						deptKeyArray = map.get("key");
						deptValueArray = map.get("value");
						return null;
					}
					protected void onPostExecute(Void result) {
						XiuGaiZaiYunActivity.this.setContentView(R.layout.select_cangku);
						ListView listView = (ListView) XiuGaiZaiYunActivity.this.findViewById(R.id.listView);
						String [] values = new String[deptValueArray.size()];
						 for(int i=0;i<deptValueArray.size();i++){  
							 values[i]=(String)deptValueArray.get(i);  
					     }
						 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(XiuGaiZaiYunActivity.this, android.R.layout.simple_list_item_1, values);
						 listView.setAdapter(arrayAdapter);
						 listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								selectedDept = deptValueArray.get(arg2);
								selectedDeptKey = deptKeyArray.get(arg2);
								setContentView(mainView);
								deptTv.setText(selectedDept);
							}
							 
						});
					};
					
				}.execute();
				
			}
		});
		
		officeTv.setText(selectedOffice);
		office.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>(){
					String officeKey = null;
					String deptKey = deptKeyArray.get(deptValueArray.indexOf(deptTv.getText().toString()));  //仓库key
					
					@Override
					protected Void doInBackground(Void... params) {
						HashMap<String, ArrayList<String>> officeMap = new HttpClientUtil(XiuGaiZaiYunActivity.this).getCode(XiuGaiZaiYunActivity.this, deptKey);
						officeKeyArray = officeMap.get("key");
						officeValueArray = officeMap.get("value");
						if(officeValueArray.indexOf(officeTv.getText().toString())!= -1){
							officeKey = officeKeyArray.get(officeValueArray.indexOf(officeTv.getText().toString()));
						}
						return null;
					}
					protected void onPostExecute(Void result) {
						XiuGaiZaiYunActivity.this.setContentView(R.layout.select_cangku);
						ListView listView = (ListView) XiuGaiZaiYunActivity.this.findViewById(R.id.listView);
						String [] values = new String[officeValueArray.size()];
						 for(int i=0;i<officeValueArray.size();i++){  
							 values[i]=(String)officeValueArray.get(i);  
					     }
						 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(XiuGaiZaiYunActivity.this, android.R.layout.simple_list_item_1, values);
						 listView.setAdapter(arrayAdapter);
						 listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								selectedOffice = officeValueArray.get(arg2);
								setContentView(mainView);
								officeTv.setText(selectedOffice);
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
				
				HashMap<String, ArrayList<String>> map = new HttpClientUtil(XiuGaiZaiYunActivity.this).getDept(XiuGaiZaiYunActivity.this);
				deptKeyArray = map.get("key");
				deptValueArray = map.get("value");
				return null;
			}
			
		}.execute();
	}

	class DuiHaoClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			final String deptKey = deptKeyArray.get(deptValueArray.indexOf(deptTv.getText().toString()));  //仓库key
			
			final String keeper = keeperEdt.getText().toString();  //负责人
			final String detil = detilEdt.getText().toString();  //描述
			final String usetype = XiuGaiZaiYunActivity.this.getIntent().getStringExtra("usetype"); //使用状态
			final String assetInfoId = getIntent().getStringExtra("assetInfoId");
			//发送处理请求
			new AsyncTask<Void, Void, String>() {
				String officeKey = null;
				protected void onPreExecute() {
					
				};
				@Override
				protected String doInBackground(Void... params) {
					
					HashMap<String, ArrayList<String>> officeMap = new HttpClientUtil(XiuGaiZaiYunActivity.this).getCode(XiuGaiZaiYunActivity.this, deptKey);
					officeKeyArray = officeMap.get("key");
					officeValueArray = officeMap.get("value");
					if(officeValueArray.indexOf(officeTv.getText().toString())!= -1){
						officeKey = officeKeyArray.get(officeValueArray.indexOf(officeTv.getText().toString()));
					}
					
					return new HttpClientUtil(XiuGaiZaiYunActivity.this).updateAssetInfoZY(XiuGaiZaiYunActivity.this, assetInfoId, deptKey, officeKey, keeper, usetype, detil);
				}
				protected void onPostExecute(String result) {
					if(result.equals("1")){
						Toast.makeText(XiuGaiZaiYunActivity.this, "处理成功", Toast.LENGTH_SHORT).show();
						//GlobalParams.planAssetInfoList = new HttpClientUtil(XiuGaiZaiYunActivity.this).getPlanInfoById(XiuGaiZaiYunActivity.this, GlobalParams.planId);
						XiuGaiZaiYunActivity.this.finish();
					}else{
						Toast.makeText(XiuGaiZaiYunActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
					}
					
				};
			}.execute();
		}
		
	}
	class ShanChuClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			XiuGaiZaiYunActivity.this.finish();
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
