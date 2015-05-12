package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.StockItemNew;
import com.gdht.itasset.widget.WaitingDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AssetDetailActivity extends Activity {
	private ArrayList<StockItemNew> dataArray = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new AsyncTask<Void, Void, Void>() {
			WaitingDialog dialog = new WaitingDialog(AssetDetailActivity.this);
			protected void onCancelled() {
				dialog.show();
			};
			@Override
			protected Void doInBackground(Void... params) {
				String rfid = getIntent().getStringExtra("rfid");
				dataArray = new HttpClientUtil(AssetDetailActivity.this).checkAssetByRfidOnly(AssetDetailActivity.this, rfid);
				
				return null;
			}
			protected void onPostExecute(Void result) {
				dialog.dismiss();
				if(dataArray != null){
					StockItemNew item = dataArray.get(0);
					if(item.getUsetype().equals("1")){
						//库存
						AssetDetailActivity.this.setContentView(R.layout.activity_asset_detail_ck);
						TextView rfidTv = (TextView)AssetDetailActivity.this.findViewById(R.id.rfid_code);
						TextView tv1 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv1);
						TextView tv2 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv2);
						TextView tv3 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv3);
						TextView tv4 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv4);
						TextView tv5 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv5);
						TextView tv6 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv6);
						TextView tv7 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv7);
						TextView tv8 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv8);
						TextView tv9 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv9);
						TextView tv10 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv10);
						TextView tv11 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv11);
						TextView tv12 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv12);
						rfidTv.setText(item.getRfidnumber());
						tv1.setText(item.getClassify());
						tv2.setText(item.getName());
						tv3.setText(item.getType());
						tv4.setText("库存");
						String checkState = "";
						if(item.getCheckstate().equals("0")){
							checkState = "未盘";
						}else if(item.getCheckstate().equals("1")){
							checkState = "已盘";
						}else if(item.getCheckstate().equals("2")){
							checkState = "盘盈";
						}else if(item.getCheckstate().equals("3")){
							checkState = "盘亏";
						}
						tv5.setText(checkState);
						tv6.setText(item.getBrand());
						tv7.setText(item.getModel());
						tv8.setText(item.getDept());
						tv9.setText(item.getKeeper());
						tv10.setText(item.getWarehouseArea());
						tv12.setText(item.getGoodsShelves());
						tv11.setText(item.getRegisterdate());
						
					}else if(item.equals("2")){
						//在运
						AssetDetailActivity.this.setContentView(R.layout.activity_asset_detail_zy);
						TextView rfidTv = (TextView)AssetDetailActivity.this.findViewById(R.id.rfid_code);
						TextView tv1 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv1);
						TextView tv2 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv2);
						TextView tv3 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv3);
						TextView tv4 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv4);
						TextView tv5 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv5);
						TextView tv6 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv6);
						TextView tv7 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv7);
						TextView tv8 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv8);
						TextView tv9 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv9);
						TextView tv10 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv10);
						TextView tv11 = (TextView)AssetDetailActivity.this.findViewById(R.id.tv11);
						rfidTv.setText(item.getRfidnumber());
						tv1.setText(item.getClassify());
						tv2.setText(item.getName());
						tv3.setText(item.getType());
						tv4.setText("在运");
						String checkState = "";
						if(item.getCheckstate().equals("0")){
							checkState = "未盘";
						}else if(item.getCheckstate().equals("1")){
							checkState = "已盘";
						}else if(item.getCheckstate().equals("2")){
							checkState = "盘盈";
						}else if(item.getCheckstate().equals("3")){
							checkState = "盘亏";
						}
						tv5.setText(checkState);
						tv6.setText(item.getBrand());
						tv7.setText(item.getModel());
						tv8.setText(item.getDept());
						tv9.setText(item.getKeeper());
						tv10.setText(item.getOffice());
						tv11.setText(item.getRegisterdate());
					}else{
						AssetDetailActivity.this.setContentView(R.layout.activity_asset_detail_ck);
					}
				}else{
					Toast.makeText(AssetDetailActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
					AssetDetailActivity.this.setContentView(R.layout.activity_asset_detail_ck);
				}
			};
			
		}.execute();
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}
}
