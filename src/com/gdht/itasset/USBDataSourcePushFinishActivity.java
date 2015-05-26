package com.gdht.itasset;

import java.io.File;

import com.gdht.itasset.db.service.LocalPandianService;
import com.gdht.itasset.db.service.LocalPlanResultService;
import com.gdht.itasset.db.service.LocalPlanService;
import com.gdht.itasset.db.service.LocalRealNameService;
import com.gdht.itasset.db.service.LocalStockService;
import com.gdht.itasset.receiver.ConnectionChangeReceiver;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.utils.ImportDBUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class USBDataSourcePushFinishActivity extends Activity {
	private ProgressDialog pd;
	private Long assetNumber = 0l, planNumber = 0l, planResultNumber = 0l;
	private LocalStockService localStockService;
	private LocalPlanService localPlanService;
	private LocalPlanResultService localPlanResultService;
	private AlertDialog ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_usb_push);

//		initAd(this);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.sure:
			String dbPath = Environment.getExternalStorageDirectory().getPath() + "/datasource.db";
			File file = new File(dbPath);
			if(file.exists()) {
				new RefreshUSBAssetDataSourceAt().execute("");
			}else {
				Toast.makeText(this, "没有找到数据源，请使用USB连接电脑，下载最新的数据源!", 0).show();
			}
			break;
		case R.id.close:
			this.finish();
			break;
		}
	}
	
	private class RefreshUSBAssetDataSourceAt extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(USBDataSourcePushFinishActivity.this);
			pd.setTitle("提示");
			pd.setMessage("资产信息数据库更新中...");
			pd.setCancelable(true);
			pd.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			ImportDBUtils dbUtils = new ImportDBUtils(USBDataSourcePushFinishActivity.this);
			dbUtils.copyDatabase();
			localPlanResultService = new LocalPlanResultService(USBDataSourcePushFinishActivity.this);
			localStockService = new LocalStockService(USBDataSourcePushFinishActivity.this);
			localPlanService = new LocalPlanService(USBDataSourcePushFinishActivity.this);
			assetNumber = localStockService.getCountNumber();
			planNumber = localPlanService.getCountNumber();
			planResultNumber = localPlanResultService.getCountNumber();
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
				pd = null;
			}
			String contentStr = "资产数据更新完成，共计" + assetNumber
					+ "条。\n盘点计划数据更新完成，共计" + planNumber + "条。\n盘点结果数据更新完成，共计"
					+ planResultNumber + "条。";
			initAd(contentStr);
		}
	}
	
	private void initAd(String contentStr) {

		View dialogView = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.dialog_datasource_finish, null);

		ad = new AlertDialog.Builder(this).create();
		ad.setCanceledOnTouchOutside(false);

		ImageView close = (ImageView) dialogView.findViewById(R.id.close);
		ImageView sure = (ImageView) dialogView.findViewById(R.id.sure);
		TextView content = (TextView) dialogView.findViewById(R.id.content);
		content.setText(contentStr);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ad.dismiss();
				ad = null;
				USBDataSourcePushFinishActivity.this.finish();
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ad.dismiss();
				ad = null;
				USBDataSourcePushFinishActivity.this.finish();
			}
		});
		ad.show();
		ad.getWindow().setContentView((RelativeLayout) dialogView);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		localPlanResultService.close();
		localPlanService.close();
		localStockService.close();
	}
	
}
