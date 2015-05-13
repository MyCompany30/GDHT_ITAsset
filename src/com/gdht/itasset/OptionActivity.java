package com.gdht.itasset;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.widget.WaitingDialog;

public class OptionActivity extends Activity {
	private EditText ipEdt = null;
	private EditText portEdt = null;
	private EditText PjEdt = null;
	private TextView jiaBtn, jianBtn;
	private EditText gonglvEt;
	private int maxGongLv = 30;
	private ProgressDialog pd;
	private AppSharedPreferences asp;
	private AlertDialog ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		asp = new AppSharedPreferences(this, "gdht");
		jiaBtn = (TextView) findViewById(R.id.jia);
		jianBtn = (TextView) findViewById(R.id.jian);
		gonglvEt = (EditText) findViewById(R.id.gonglv);
		ipEdt = (EditText)findViewById(R.id.ipEdt);
		portEdt = (EditText)findViewById(R.id.portEdt);
		PjEdt = (EditText)findViewById(R.id.PjEdt);
		String defaultAddr = new AppSharedPreferences(OptionActivity.this, "gdht").getIP();
		ipEdt.setText(defaultAddr.split("http://")[1].split(":")[0]);
		portEdt.setText(defaultAddr.split(":")[2].split("/")[0]);
		PjEdt.setText(defaultAddr.substring(defaultAddr.lastIndexOf("/")+1));
		gonglvEt.setText(asp.getGongLv() + "");
		gonglvEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				String gv = gonglvEt.getText().toString();
				if("".equals(gv) || "0".equals(gv)) {
					Toast.makeText(OptionActivity.this, "最小功率为1", 0).show();
					gonglvEt.setText("1");
				}else if(Integer.parseInt(gv) > 30) {
					Toast.makeText(OptionActivity.this, "最大功率为30", 0).show();
					gonglvEt.setText("30");
				}
			}
		});
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
		case R.id.quxiao:
			this.finish();
			break;
		
		case R.id.queding:
			String ip = ipEdt.getText().toString().trim();
			String port = portEdt.getText().toString().trim();
			String project = PjEdt.getText().toString().trim();
			MainActivity.ipStr = "http://"+ip+":"+port+"/"+project;
			new AppSharedPreferences(OptionActivity.this, "gdht").saveIP(MainActivity.ipStr);
			this.finish();
			break;
		case R.id.jia:
			String gv = gonglvEt.getText().toString().trim();
			if(Integer.parseInt(gv) > maxGongLv) {
				Toast.makeText(OptionActivity.this, "最大功率为30", 0).show();
				gonglvEt.setText("30");
			}else {
				gonglvEt.setText(String.valueOf(Integer.parseInt(gv) + 1));
			}
			break;
		case R.id.jian:
			gv = gonglvEt.getText().toString().trim();
			if("".equals(gv) || "1".equals(gv)) {
				Toast.makeText(OptionActivity.this, "最小功率为1", 0).show();
				gonglvEt.setText("1");
			}else {
				gonglvEt.setText(String.valueOf(Integer.parseInt(gv) - 1));
			}
			break;
		case R.id.gengxin:
//			Toast.makeText(this, "gengxin", 0).show();
//			new RefreshDataSourceAt().execute("");
//			initAd();
			new RefreshDataSourceAt().execute("");
			break;
		}
	}
	
	private class RefreshDataSourceAt extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(OptionActivity.this);
			pd.setTitle("提示");
			pd.setMessage("资产信息数据库更新中...");
			pd.setCancelable(true);
			pd.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return new HttpClientUtil(OptionActivity.this).getAssetInfos(OptionActivity.this);
//			return new HttpClientUtil(OptionActivity.this).getAllCheckPlan(OptionActivity.this);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		asp.setGongLv(Integer.parseInt(gonglvEt.getText().toString().trim()));
	}
	
	private void initAd() {
		ad = new AlertDialog.Builder(this).create();
		View convertView = LayoutInflater.from(this).inflate(R.layout.dialog_datasource_finish, null);
		ImageView close = (ImageView) convertView.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ad.dismiss();
			}
		});
		ad.setView(convertView, 0, 0, 0, 0);
		ad.show();
	}
	
}
