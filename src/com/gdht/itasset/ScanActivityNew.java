package com.gdht.itasset;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.gdht.itasset.adapter.RfidAdapter;
import com.gdht.itasset.db.service.ScanCheckRFIDSDBService;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.widget.WaitingDialog;
import com.gdht.itasset.xintong.Accompaniment;
import com.gdht.itasset.xintong.App;
import com.gdht.itasset.xintong.DataTransfer;
import com.senter.support.openapi.StUhf.UII;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.InterrogatorModelD2;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cRealTimeInventory;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdRssi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanActivityNew extends Activity {
	private final Accompaniment accompaniment = new Accompaniment(this,	R.raw.tag_inventoried);
	private Handler accompainimentsHandler;
	private Timer mTimer;
	private TimerTask mTask;
	private UII uii_change;
	private String uii;
	private ArrayList<String> rfidArray = new ArrayList<String>();
	private LinearLayout startBtn, stopBtn;
	private RfidAdapter adapter;
	private ListView listView;
	private TextView num1, num2;
	private Handler saveHandler = new Handler();
	private WaitingDialog dialog = null;
	private ScanCheckRFIDSDBService checkRFIDSDBService;
	private String user;
	//放入timertask内执行
	private final Runnable accompainimentRunnable = new Runnable() {
		@Override
		public void run() {
			accompaniment.start();
			accompainimentsHandler.removeCallbacks(this);
			//截取rfid编号
			uii = DataTransfer.xGetString(mUii.getBytes()).substring(6, 41).replace(" ", "");
			if(!rfidArray.contains(uii)){
				rfidArray.add(uii);
				Log.i("a", "uii = " + uii);
				adapter.notifyDataSetChanged();
			}
			if(rfidArray.size() > 0) {
				num1.setText(rfidArray.size() + "");
				num2.setText(rfidArray.size() + "");
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_new);
		startBtn = (LinearLayout) this.findViewById(R.id.start);
		stopBtn = (LinearLayout) this.findViewById(R.id.stop);
		listView = (ListView) this.findViewById(R.id.listView);
		num1 = (TextView) this.findViewById(R.id.num1);
		num1.setText("0");
		num2 = (TextView) this.findViewById(R.id.num2);
		num2.setText("0");
		adapter = new RfidAdapter(this, rfidArray, null);
		listView.setAdapter(adapter);
		checkRFIDSDBService = new ScanCheckRFIDSDBService(this);
		SharedPreferences loginSettings = this.getSharedPreferences("GDHT_ITASSET_SETTINGS", Context.MODE_PRIVATE);
		user = loginSettings.getString("LOGIN_NAME", "");
		dialog =  new WaitingDialog(ScanActivityNew.this);
		if (App.getRfid() == null)
		{
			Toast.makeText(this,R.string.MakeSurePDAexitRfid, Toast.LENGTH_LONG).show();
			finish();
		} else
		{
			switch (App.getRfid().getInterrogatorModel())
			{
				
				case InterrogatorModelD2:
				{
					AppSharedPreferences asp = new AppSharedPreferences(ScanActivityNew.this, "gdht");
					App.getRfid().setPower(asp.getGongLv());
					Toast.makeText(ScanActivityNew.this, "当前功率是  = " + App.getRfid().getPower(), 0).show();
//					findViews();
//					setOnClicks();
					break;
				}
			
				default:
					break;
			}
		}
		HandlerThread htHandlerThread = new HandlerThread("");
		htHandlerThread.start();
		accompainimentsHandler = new Handler(htHandlerThread.getLooper());
		accompaniment.init();
		
		startBtn.setVisibility(View.GONE);
		rfidArray.addAll(checkRFIDSDBService.loadDatas(user));
		
		start();
		
		saveHandler.postDelayed(saveRunnable, 10000);
		
	}
	
	Runnable saveRunnable = new Runnable() {
		
		@Override
		public void run() {
			Log.i("a", "save !!!!!!!!!!!!!!");
			checkRFIDSDBService.deleteAll(user);
			for(String s : rfidArray) {
				checkRFIDSDBService.saveRFID(s, user);
			}
			saveHandler.postDelayed(this, 10000);
		}
	};
	
	
	// 启动
		private void start() {
			if (mTimer == null) {
				mTimer = new Timer();
			}
				mTask = new TimerTask() {
					@Override
					public void run() {
						startInventory();
					}
				};
			mTimer.schedule(mTask, 1000, 10);

		}
		// 停止
		private void stop() {
			App.stop();
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}
		
		private UII mUii;
		//开启RFID扫描器
		protected void startInventory() {
			App.getRfid().getInterrogatorAs(InterrogatorModelD2.class).iso18k6cRealTimeInventory(1,
							new UmdOnIso18k6cRealTimeInventory() {

								@Override
								public void onTagInventory(UII uii,	UmdFrequencyPoint frequencyPoint, Integer antennaId, UmdRssi rssi) {
									mUii = uii;

								}
								@Override
								public void onFinishedSuccessfully(Integer arg0, int arg1, int arg2) {
									if (mUii != null) {
										addToplay(mUii);
									}

								}
								@Override
								public void onFinishedWithError(UmdErrorCode arg0) {

								}
							});

		}
		protected void addToplay(com.senter.support.openapi.StUhf.UII uii2) {
			uii_change = uii2;
			tagAccompainiment();

		}

		private void tagAccompainiment() {
			this.runOnUiThread(accompainimentRunnable);
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (resultCode) {
			case 100:
				Bundle bundle = data.getExtras();
				String code = bundle.getString("result") + "0000000";
				if(rfidArray.contains(code)){
					Toast.makeText(ScanActivityNew.this, "已经存在!", 0).show();
				}else {

					rfidArray.add(code);
					adapter.notifyDataSetChanged();
					if(rfidArray.size() > 0) {
						num1.setText(rfidArray.size() + "");
						num2.setText(rfidArray.size() + "");
					}
				}
				break;
			}
		}
		
		public void btnClick(View view) {
			switch (view.getId()) {
			case R.id.back:
				this.finish();
				break;
			case R.id.start:
				stopBtn.setVisibility(View.VISIBLE);
				startBtn.setVisibility(View.GONE);
				start();
				break;
			case R.id.stop:
				stopBtn.setVisibility(View.GONE);
				startBtn.setVisibility(View.VISIBLE);
				stop();
				break;
			case R.id.ewscanBtn:
				Intent intent = new Intent(this,
						ErWeiScanCaptureActivity.class);
				startActivityForResult(intent, 100);
				break;
			}
		}
		
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			saveHandler.removeCallbacks(saveRunnable);
			stop();
			checkRFIDSDBService.closeDB();
		}
}
