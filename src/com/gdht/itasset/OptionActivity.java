package com.gdht.itasset;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.db.service.LocalPandianService;
import com.gdht.itasset.db.service.LocalPlanResultService;
import com.gdht.itasset.db.service.LocalPlanService;
import com.gdht.itasset.db.service.LocalRealNameService;
import com.gdht.itasset.db.service.LocalStockService;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.LocalPlanResult;
import com.gdht.itasset.pojo.PlanInfo;
import com.gdht.itasset.pojo.RealName;
import com.gdht.itasset.pojo.StockItemNew;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.utils.ImportDBUtils;
import com.gdht.itasset.xintong.App;

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
	private View optionView = null;
	private View ipconfigView =  null;
	private String model;
	private Long assetNumber = 0l, planNumber = 0l, planResultNumber = 0l;
	private LocalStockService localStockService;
	private LocalPlanService localPlanService;
	private LocalPlanResultService localPlanResultService;
	private LocalRealNameService localRealNameService;
	private LocalPandianService localPandianService;
	private TextView titleTv = null;
	private View view1,view2;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		if(GlobalParams.LOGIN_TYPE == 2){
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
		}
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		localStockService = new LocalStockService(this);
		localPlanService = new LocalPlanService(this);
		localPlanResultService = new LocalPlanResultService(this);
		localRealNameService = new LocalRealNameService(this);
		localPandianService = new LocalPandianService(this);
		view1 = findViewById(R.id.view2_1);
		view2 = findViewById(R.id.view2_2);
		optionView = findViewById(R.id.option);
		ipconfigView = findViewById(R.id.ipconfig);
		titleTv = (TextView) findViewById(R.id.title);
		model = getIntent().getStringExtra("model");
		if(model.equals("0")){
			optionView.setVisibility(View.GONE);
			titleTv.setText("ＩＰ设置");
		}else if(model.equals("1")){
			ipconfigView.setVisibility(View.GONE);
			titleTv.setText("设置");
		}
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
		case R.id.gengxinUSB:
			String dbPath = Environment.getExternalStorageDirectory().getPath() + "/datasource.db";
			File file = new File(dbPath);
			if(file.exists()) {
				new RefreshUSBAssetDataSourceAt().execute("");
			}else {
				Toast.makeText(this, "没有找到数据源，请使用USB连接电脑，下载最新的数据源!", 0).show();
			}
			break;
		case R.id.gengxinDB:
			new RefreshAssetDataSourceAt().execute("");
			break;
		}
	}
	
	private void changeGongLv() {
		if (App.getRfid() == null) {
			Toast.makeText(this, R.string.MakeSurePDAexitRfid,
					Toast.LENGTH_LONG).show();
			finish();
		} else {
			switch (App.getRfid().getInterrogatorModel()) {

			case InterrogatorModelD2: {
				AppSharedPreferences asp = new AppSharedPreferences(
						OptionActivity.this, "gdht");
				if(App.getRfid() != null && asp != null) {
					App.getRfid().setPower(asp.getGongLv());
				}
				break;
			}

			default:
				break;
			}
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
//			return new HttpClientUtil(OptionActivity.this).getAssetInfos(OptionActivity.this);
//			return new HttpClientUtil(OptionActivity.this).getAllCheckPlan(OptionActivity.this);
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
//			for(StockItemNew s : result) {
//				Log.i("a", "s = " + s.toString());
//			}
			pd.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		localStockService.close();
		localPlanService.close();;
		localPlanResultService.close();;
		localRealNameService.close();;
		asp.setGongLv(Integer.parseInt(gonglvEt.getText().toString().trim()));
		changeGongLv();
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
	
	
	private class RefreshUSBAssetDataSourceAt extends AsyncTask<String, Integer, String> {
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
			ImportDBUtils dbUtils = new ImportDBUtils(OptionActivity.this);
			dbUtils.copyDatabase();
			localStockService = new LocalStockService(OptionActivity.this);
			localPlanService = new LocalPlanService(OptionActivity.this);
			localPlanResultService = new LocalPlanResultService(OptionActivity.this);
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

	private class RefreshAssetDataSourceAt extends AsyncTask<String, Integer, Long> {
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
		protected Long doInBackground(String... arg0) {
			//删除当前local_pandian表的数据
			
			ArrayList<StockItemNew> sis = new HttpClientUtil(
					OptionActivity.this).getAssetInfos(OptionActivity.this);
			if (sis == null) {
				return 0l;
			} else {
				return localStockService.save(sis);
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			assetNumber = result;
			new RefreshPlanDataSourceAt().execute("");
		}
	}
	private class RefreshPlanDataSourceAt extends AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Long doInBackground(String... arg0) {
			ArrayList<PlanInfo> pis = new HttpClientUtil(OptionActivity.this)
					.getAllCheckPlan(OptionActivity.this);
			if (pis == null) {
				return 0l;
			} else {
				return localPlanService.save(pis);
			}
		}
		
		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			planNumber = result;
			new RefreshResultDataSourceAt().execute("");
		}
	}
	private class RefreshResultDataSourceAt extends AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Long doInBackground(String... arg0) {
			ArrayList<LocalPlanResult> lprs = new HttpClientUtil(
					OptionActivity.this)
					.getAllCheckPlanInfo(OptionActivity.this);
			if (lprs == null) {
				return 0l;
			} else {
				return localPlanResultService.save(lprs);
			}
		}
		
		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			planResultNumber = result;
			new RefreshRealNameSourceAt().execute("");
		}
	}
	private class RefreshRealNameSourceAt extends AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Long doInBackground(String... arg0) {
			ArrayList<RealName> lprs = new HttpClientUtil(
					OptionActivity.this)
					.nameCompare(OptionActivity.this);
			if (lprs == null) {
				return 0l;
			} else {
				return localRealNameService.save(lprs);
			}
		}
		
		@Override
		protected void onPostExecute(Long result) {
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
			}
		});
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ad.dismiss();
				ad = null;
			}
		});
		ad.show();
		ad.getWindow().setContentView((RelativeLayout) dialogView);
	}
}
