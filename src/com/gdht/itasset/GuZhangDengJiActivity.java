package com.gdht.itasset;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GuZhangDengJiActivity extends Activity {
	private TextView rfidTv = null;
	private TextView assetTypeTv = null;
	private TextView assetNameTv = null;
	private TextView depotNameTv = null;
	private TextView deptNameTv = null;
	private CheckBox switchChk = null;
	private EditText detil = null;
	private TextView duihao = null;
	private ImageView duihao_ = null;
	private TextView shanchu = null;
	private ImageView shanchu_ = null;
	private String assetInfoId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gu_zhang_deng_ji);
		Intent intent = getIntent();
		assetInfoId = intent.getStringExtra("assetInfoId");
		rfidTv = (TextView)findViewById(R.id.rfid_code);
		assetTypeTv = (TextView)findViewById(R.id.assetType_code);
		assetNameTv = (TextView)findViewById(R.id.assetName_code);
		depotNameTv = (TextView)findViewById(R.id.depotName_code);
		deptNameTv = (TextView)findViewById(R.id.deptName_code);
		switchChk = (CheckBox)findViewById(R.id.switch_btn);
		detil = (EditText)findViewById(R.id.add_content);
		duihao = (TextView)findViewById(R.id.duihao);
		duihao_ = (ImageView)findViewById(R.id.duihao_);
		shanchu = (TextView)findViewById(R.id.shanchu);
		shanchu_ = (ImageView)findViewById(R.id.shanchu_);
		rfidTv.setText(intent.getStringExtra("rfid"));
		assetTypeTv.setText(intent.getStringExtra("assetType"));
		assetNameTv.setText(intent.getStringExtra("assetName"));
		depotNameTv.setText(intent.getStringExtra("position"));
		deptNameTv.setText(intent.getStringExtra("dept"));
		DuiHaoClickListener duiHaoClickListener = new DuiHaoClickListener();
		ShanChuClickListener shanChuClickListener = new ShanChuClickListener();
		duihao.setOnClickListener(duiHaoClickListener);
		duihao_.setOnClickListener(duiHaoClickListener);
		shanchu.setOnClickListener(shanChuClickListener);
		shanchu_.setOnClickListener(shanChuClickListener);
		
	}

	
	
	
	class DuiHaoClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//发送处理请求
			new AsyncTask<Void, Void, String>() {
				WaitingDialog dialog = new WaitingDialog(GuZhangDengJiActivity.this);
				protected void onPreExecute() {
					dialog.show();
				};
				@Override
				protected String doInBackground(Void... params) {
					
					String repairtype = null;
					if(switchChk.isChecked()){
						repairtype = "2";    //隐患
					}else{
						repairtype = "1";    //故障
					}
					return new HttpClientUtil(GuZhangDengJiActivity.this).addRepairInfo(GuZhangDengJiActivity.this, assetInfoId, repairtype, detil.getText().toString(), GlobalParams.username);
				}
				protected void onPostExecute(String result) {
					if(result.equals("1")){
						Toast.makeText(GuZhangDengJiActivity.this, "处理成功", Toast.LENGTH_SHORT).show();
						GuZhangDengJiActivity.this.finish();
					}else{
						Toast.makeText(GuZhangDengJiActivity.this, "处理失败", Toast.LENGTH_SHORT).show();
					}
					dialog.dismiss();
				};
			}.execute();
		}
		
	}
	class ShanChuClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			GuZhangDengJiActivity.this.finish();
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
