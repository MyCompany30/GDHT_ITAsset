package com.gdht.itasset;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.gdht.itasset.db.service.RFIDSDBService;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.xintong.Accompaniment;
import com.gdht.itasset.xintong.App;
import com.gdht.itasset.xintong.DataTransfer;
import com.senter.support.openapi.StPonTest;
import com.senter.support.openapi.StUhf.UII;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.InterrogatorModelD2;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cRealTimeInventory;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdRssi;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScanActivity extends Activity {
	private ImageView startBtn;
	private ImageView pauseBtn;
	private ListView scanListView;
	private MyListAdapter rfidListAdapter;
	private LinearLayout complateBtn;
	private LinearLayout clearBtn;
	private ArrayList<String> rfidArray = new ArrayList<String>();
	private ArrayList<PlanAssetInfo> planAssetArrayList = new ArrayList<PlanAssetInfo>();
	private Timer mTimer;
	private TimerTask mTask;
	private UII uii_change;
	private final Accompaniment accompaniment = new Accompaniment(this,	R.raw.tag_inventoried);
	private Handler accompainimentsHandler;
	private String uii;
	private RFIDSDBService rfidsdbService;
	private ProgressDialog pd;
	private TextView number;
	private String checkStr;
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
				rfidListAdapter.notifyDataSetChanged();
			}
			if(rfidArray.size() > 0) {
				number.setText("(" +rfidArray.size() + ")");
			}
		}
	};
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		checkStr = this.getIntent().getStringExtra("check");
		number = (TextView) this.findViewById(R.id.number);
		complateBtn = (LinearLayout)findViewById(R.id.scan_complate);
		if(SelectDeptActivity.instance!=null){
			SelectDeptActivity.instance.finish();
		}
		if(SelectOfficeActivity.instance!=null){
			SelectOfficeActivity.instance.finish();
		}
		if(SelectUseTypeActivity.instance!=null){
			SelectUseTypeActivity.instance.finish();
		}
		if(SelectWarehouseAreaActivity.instance!=null){
			SelectWarehouseAreaActivity.instance.finish();
		}
		if(SelectGoodsShelvesActivity.instance!=null){
			SelectGoodsShelvesActivity.instance.finish();
		}
		//planAssetArrayList = (ArrayList<PlanAssetInfo>) getIntent().getSerializableExtra("assetInfoList");
		Intent intent = getIntent();
		
		//获取需要盘点的资产列表

		if(intent.hasExtra("dept")&&intent.hasExtra("warehouseArea")&&intent.hasExtra("goodsShelves")&&(!intent.getStringExtra("dept").equals(""))&&(!intent.getStringExtra("warehouseArea").equals(""))&&(!intent.getStringExtra("goodsShelves").equals(""))){
			String dept = intent.getStringExtra("dept");
			String area = intent.getStringExtra("warehouseArea");
			String shelve = intent.getStringExtra("goodsShelves");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getWarehouseArea().equals(area)&&GlobalParams.planAssetInfoList.get(i).getGoodsShelves().equals(shelve)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else if(intent.hasExtra("dept")&&intent.hasExtra("warehouseArea")&&(!intent.getStringExtra("dept").equals(""))&&(!intent.getStringExtra("warehouseArea").equals(""))){
			String dept = intent.getStringExtra("dept");
			String area = intent.getStringExtra("warehouseArea");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getWarehouseArea().equals(area)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else if(intent.hasExtra("dept")&&intent.hasExtra("office")&&(!intent.getStringExtra("dept").equals(""))&&(!intent.getStringExtra("office").equals(""))){
			String dept = intent.getStringExtra("dept");
			String office = intent.getStringExtra("office");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)&&GlobalParams.planAssetInfoList.get(i).getOffice().equals(office)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else if(intent.hasExtra("dept")&&(!intent.getStringExtra("dept").equals(""))){
			String dept = intent.getStringExtra("dept");
			for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
				if(GlobalParams.planAssetInfoList.get(i).getDept().equals(dept)){
					planAssetArrayList.add(GlobalParams.planAssetInfoList.get(i));
				}
			}
			
		}else{
			planAssetArrayList = GlobalParams.planAssetInfoList;
		}
		rfidsdbService = new RFIDSDBService(this);
		pd = new ProgressDialog(this);
//		try {
//			StPonTest.getInstance().start(StPonTest.WaveLength.WL1300nm);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		pd.setMessage("数据保存中...");
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
		if(checkStr != null && "check".equals(checkStr)) {
			complateBtn.setVisibility(View.GONE);
		}
	}

	private void setOnClicks() {
		//开始扫描
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				number.setText("(" + rfidArray.size() + ")");
				number.setVisibility(View.VISIBLE);
				start();
			}
		});
		//暂停扫描
		pauseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stop();
			}
		});
		//完成扫描
		complateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(ScanActivity.this, ScanComplateActivity.class);
//				intent.putStringArrayListExtra("rfidArray", rfidArray);
//				ScanActivity.this.startActivity(intent);
				
				if(rfidArray != null && rfidArray.size() > 0) {
					pd.show();
					for(String s : rfidArray) {
						Log.i("a", "rfid = " + s);
						rfidsdbService.saveRFID(s);
					}
					pd.dismiss();
					Intent intent = new Intent();
					intent.setClass(ScanActivity.this, ScanComplateActivity.class);
					intent.putStringArrayListExtra("rfidArray", rfidArray);
					intent.putExtra("assetInfoList", planAssetArrayList);
					ScanActivity.this.startActivity(intent);
				}
				
			}
		});
		//清除扫描
		clearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rfidArray.removeAll(rfidArray);
				number.setText("(" + rfidArray.size() + ")");
				rfidListAdapter.notifyDataSetChanged();
			}
		});
		
	}

	private void findViews() {
		startBtn = (ImageView)findViewById(R.id.scan_start_btn);
		pauseBtn = (ImageView)findViewById(R.id.scan_pause_btn);
		scanListView = (ListView)findViewById(R.id.listView);
		complateBtn = (LinearLayout)findViewById(R.id.scan_complate);
		clearBtn = (LinearLayout)findViewById(R.id.scan_clear);
		rfidListAdapter = new MyListAdapter();
		scanListView.setAdapter(rfidListAdapter);
		scanListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ScanActivity.this, RfidCheckActivity.class);
				intent.putExtra("code", rfidArray.get(arg2));
				intent.putExtra("assetInfoList", planAssetArrayList);
				startActivity(intent);
			}
		});
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
	
	
	class MyListAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rfidArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return rfidArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.rfid_listitem, null);
			TextView numberTV = (TextView)convertView.findViewById(R.id.number);
			TextView rfidTitleTv = (TextView)convertView.findViewById(R.id.rfidTitle);
			TextView rfidTV = (TextView)convertView.findViewById(R.id.rfidCode);
			numberTV.setText(position+1+"");
			for(int i = 0; i < planAssetArrayList.size(); i++){
				if(planAssetArrayList.get(i).getRfidnumber().equals(rfidArray.get(position))){
					rfidTitleTv.setText((planAssetArrayList.get(i).getAssetName()));
					convertView.setBackgroundColor(Color.parseColor("#d3fac7"));
					break;
				}
				if(i==planAssetArrayList.size()-1){
					convertView.setBackgroundColor(Color.parseColor("#fbc9cc"));
				}
				
			}
			rfidTV.setText(rfidArray.get(position));
			return convertView;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stop();
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}
	
}
