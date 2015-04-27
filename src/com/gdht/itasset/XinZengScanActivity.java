package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gdht.itasset.adapter.YingPanXinZenItemAdapter;
import com.gdht.itasset.db.service.RFIDSDBService;
import com.gdht.itasset.eventbus.SelectCangKuListener;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.YingPanXinZengItem;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.xintong.Accompaniment;
import com.gdht.itasset.xintong.App;
import com.gdht.itasset.xintong.DataTransfer;
import com.senter.support.openapi.StUhf.UII;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.InterrogatorModelD2;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cRealTimeInventory;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdRssi;

import de.greenrobot.event.EventBus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XinZengScanActivity extends Activity {
	private ImageView startBtn;
	private ImageView pauseBtn;
	private ListView listView;
	private LinearLayout complateBtn;
	private LinearLayout clearBtn;
	private YingPanXinZenItemAdapter adapter;
	private List<YingPanXinZengItem> items = new ArrayList<YingPanXinZengItem>();
	private Timer mTimer;
	private TimerTask mTask;
	private UII uii_change;
	private final Accompaniment accompaniment = new Accompaniment(this,
			R.raw.tag_inventoried);
	private Handler accompainimentsHandler;
	private String uii;
	private RFIDSDBService rfidsdbService;
	private ProgressDialog pd;
	private StringBuffer rfidSb = new StringBuffer();
	// 放入timertask内执行
	private final Runnable accompainimentRunnable = new Runnable() {
		@Override
		public void run() {
			accompaniment.start();
			accompainimentsHandler.removeCallbacks(this);
			// 截取rfid编号
			uii = DataTransfer.xGetString(mUii.getBytes()).substring(6, 31)
					.replace(" ", "");
			if (!isExitsAlready(uii)) {
				// YingPanXinZengItem item = new YingPanXinZengItem();
				// item.setRfid_labelnum(uii);
				// items.add(item);
				// adapter.notifyDataSetChanged();
				YingPanXinZengItem item = new YingPanXinZengItem();
				item.setRfid_labelnum(uii);
				item.setAssetCheckplanId(GlobalParams.planId);
				item.setClassify(GlobalParams.zichanfenlei);
				item.setType(GlobalParams.zichanzifenlei);
				item.setRegistrant(GlobalParams.username);
				item.setDept(GlobalParams.cangKuValue);
				item.setOffice("");
				item.setDeptName(GlobalParams.cangKuName);
				item.setIsck(GlobalParams.isck);
				items.add(item);
				adapter.notifyDataSetChanged();
			}

		}
	};

	private boolean isExitsAlready(String rfidCode) {
		for (YingPanXinZengItem item : items) {
			if (item.getRfid_labelnum().equals(rfidCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_xinzeng);
		rfidsdbService = new RFIDSDBService(this);
		EventBus.getDefault().register(this);
		pd = new ProgressDialog(this);
		pd.setMessage("数据保存中...");
		if (App.getRfid() == null) {
			Toast.makeText(this, R.string.MakeSurePDAexitRfid,
					Toast.LENGTH_LONG).show();
			finish();
		} else {
			switch (App.getRfid().getInterrogatorModel()) {

			case InterrogatorModelD2: {
				findViews();
				setOnClicks();
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

	}
	
	public void onEvent(SelectCangKuListener event) {
		YingPanXinZengItem item = items.get(event.getLocation());
		item.setDept(event.getDept());
		item.setDeptName(event.getDeptName());
		item.setIsck(event.getIsCk());
		adapter.notifyDataSetChanged();
	}
	

	private void setOnClicks() {
		// 开始扫描
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				start();
			}
		});
		// 暂停扫描
		pauseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stop();
				rfidSb = new StringBuffer();
				for(YingPanXinZengItem item : items){
					rfidSb.append(item.getRfid_labelnum()).append(",");
				}
				rfidSb.deleteCharAt(rfidSb.length() - 1);
//				Log.i("a", "rfidSb.String = " + rfidSb.toString());
//				Toast.makeText(XinZengScanActivity.this, "rfidSb = " + rfidSb.toString(), 0).show();
				new rfidfilterAsyncTask().execute(rfidSb.toString());
			}
		});
		// 清除扫描
		clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				items.clear();
				adapter.notifyDataSetChanged();
			}
		});

	}
	
	private class rfidfilterAsyncTask extends AsyncTask<String, Integer, String> {
		
		
		
		@Override
		protected String doInBackground(String... params) {
			return new HttpClientUtil(XinZengScanActivity.this).rfidfilter(XinZengScanActivity.this, params[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}

	private void findViews() {
		startBtn = (ImageView) findViewById(R.id.scan_start_btn);
		pauseBtn = (ImageView) findViewById(R.id.scan_pause_btn);
		listView = (ListView) findViewById(R.id.listView);
		complateBtn = (LinearLayout) findViewById(R.id.scan_complate);
		clearBtn = (LinearLayout) findViewById(R.id.scan_clear);
		adapter = new YingPanXinZenItemAdapter(this, items);
		listView.setAdapter(adapter);

	}

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
		mTimer.schedule(mTask, 1000, 100);

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

	// 开启RFID扫描器
	protected void startInventory() {
		App.getRfid()
				.getInterrogatorAs(InterrogatorModelD2.class)
				.iso18k6cRealTimeInventory(1,
						new UmdOnIso18k6cRealTimeInventory() {

							@Override
							public void onTagInventory(UII uii,
									UmdFrequencyPoint frequencyPoint,
									Integer antennaId, UmdRssi rssi) {
								System.out
										.println("----begin onTagInventory------");
								mUii = uii;

							}

							@Override
							public void onFinishedSuccessfully(Integer arg0,
									int arg1, int arg2) {
								System.out
										.println("-----onFinishedSuccessfully-----");
								if (mUii != null) {
									addToplay(mUii);
								}

							}

							@Override
							public void onFinishedWithError(UmdErrorCode arg0) {

								System.out
										.println("------onFinishedWithError-------");

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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stop();
		EventBus.getDefault().unregister(this);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}

}
