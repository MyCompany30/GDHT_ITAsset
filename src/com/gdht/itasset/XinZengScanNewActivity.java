package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gdht.itasset.adapter.YingPanXinZenItemAdapter;
import com.gdht.itasset.db.service.ScanCheckRFIDSDBService;
import com.gdht.itasset.eventbus.RefreshNumberListener;
import com.gdht.itasset.eventbus.SelectCangKuListener;
import com.gdht.itasset.eventbus.SelectClassifyAndTypeListener;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.YingPanXinZengItem;
import com.gdht.itasset.utils.GlobalParams;
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

import de.greenrobot.event.EventBus;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XinZengScanNewActivity extends Activity {
	private LinearLayout container;
	private List<YingPanXinZengItem> items = new ArrayList<YingPanXinZengItem>();
	private List<String> addUiis = new ArrayList<String>();
	private Timer mTimer;
	private TimerTask mTask;
	private UII uii_change;
	private final Accompaniment accompaniment = new Accompaniment(this,
			R.raw.tag_inventoried);
	private Handler accompainimentsHandler;
	private String uii;
	private ProgressDialog pd;
	private String planId, deptcode, isCk;
	private StringBuffer rfidSb = new StringBuffer();
	private boolean isStop = true;
	private LayoutInflater layoutInflater;
	private List<View> views = new ArrayList<View>();
	private int location = 0;
	private String userid;
	private Intent intent;
	private int currentIdx = 0;
	private WaitingDialog wd;
	// 放入timertask内执行
	private final Runnable accompainimentRunnable = new Runnable() {
		@Override
		public void run() {
//			if (!isStop) {
				accompaniment.start();
				accompainimentsHandler.removeCallbacks(this);
				// 截取rfid编号
				uii = DataTransfer.xGetString(mUii.getBytes()).substring(6, 41)
						.replace(" ", "");
				if (!isExitsAlready(uii) && !addUiis.contains(uii)) {
					// YingPanXinZengItem item = new YingPanXinZengItem();
					// item.setRfid_labelnum(uii);
					// items.add(item);
					// adapter.notifyDataSetChanged();
					View view = layoutInflater.inflate(R.layout.item_xinzengpanying_new, null);
					TextView rfid = (TextView) view.findViewById(R.id.rfidCode);
					rfid.setText(uii);
					final ImageView addBtn = (ImageView) view.findViewById(R.id.addBtn);
					final RelativeLayout zcflLayout = (RelativeLayout) view.findViewById(R.id.zcflLayout);
					final RelativeLayout zclxLayout = (RelativeLayout) view.findViewById(R.id.zclxLayout);
					final YingPanXinZengItem item = new YingPanXinZengItem();
					item.setAssetNameEt((EditText)view.findViewById(R.id.name));
					item.setRfidCodeTv((TextView)view.findViewById(R.id.rfidCode));
					item.setFuzerennameEt((EditText)view.findViewById(R.id.fuzerenname));
					item.setZclbTv((TextView)view.findViewById(R.id.zclx));
					item.setZcflTv((TextView)view.findViewById(R.id.zcfl));
					item.setAssetCheckplanId(planId);
					item.setRegistrant(userid);
					item.setIsck(isCk);
					if("1".equals(item.getIsck())) {
						item.setDept(deptcode);
					}else {
						item.setOffice(deptcode);
					}
					item.setRfid_labelnum(uii);
					items.add(item);
					views.add(view);
					view.setTag(location);
					container.addView(view);
					addBtn.setTag(location);
					addBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							currentIdx = (Integer)addBtn.getTag();
//							Toast.makeText(XinZengScanNewActivity.this, "currentIdx = " + currentIdx, 0).show();
//							Toast.makeText(XinZengScanNewActivity.this, " item =  " + items.get((Integer)addBtn.getTag()).toString(), 0).show();
							YingPanXinZengItem item = items.get((Integer)addBtn.getTag());
							String assetName = items.get((Integer)addBtn.getTag()).getAssetNameEt().getText().toString();
							item.setName(assetName);
							String fuzeren = items.get((Integer)addBtn.getTag()).getFuzerennameEt().getText().toString();
							item.setKeeper(fuzeren);

							if(validData(item)) {
								new PyxzAt().execute(item);
							}
							
							
							
							
						}
					});
					zcflLayout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							currentIdx = (Integer)addBtn.getTag();
							intent = new Intent(XinZengScanNewActivity.this, SelectZCFLActivity.class);
							intent.putExtra("from", "zcfl");
							intent.putExtra("deptcode", deptcode);
							startActivityForResult(intent, 100);
						}
					});
					zclxLayout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							currentIdx = (Integer) addBtn.getTag();
							if(items.get(currentIdx).getZcflCode() == null || "".equals(items.get(currentIdx).getZcflCode())) {
//								Toast.makeText(XinZengScanNewActivity.this, "先选择资产分类", 0).show();
								intent = new Intent(XinZengScanNewActivity.this, SelectZCFLActivity.class);
								intent.putExtra("from", "nozcfl");
								intent.putExtra("deptcode", deptcode);
								startActivity(intent);
							}else {
								intent = new Intent(XinZengScanNewActivity.this, SelectZCLXActivity.class);
								intent.putExtra("deptcode", deptcode);
								intent.putExtra("zcflCode", items.get(currentIdx).getZcflCode());
								intent.putExtra("zcflName", items.get(currentIdx).getZcflName());
								startActivityForResult(intent, 101);
							}
						}
					});
					location++;
				}
			}

//		}
	};
	
	private class PyxzAt extends AsyncTask<YingPanXinZengItem, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			wd.show();
		}
		
		@Override
		protected String doInBackground(YingPanXinZengItem... params) {
			// TODO Auto-generated method stub
			return new HttpClientUtil(XinZengScanNewActivity.this).pyxzNew(XinZengScanNewActivity.this, params[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			wd.dismiss();
			if("1".equals(result)) {
				Toast.makeText(XinZengScanNewActivity.this, "盘盈新增成功!", 0).show();
				addUiis.add(items.get(currentIdx).getRfid_labelnum());
				container.getChildAt(currentIdx).setVisibility(View.GONE);
//				views.remove(currentIdx);
//				items.remove(currentIdx);
			}else if("-3".equals(result)) {
				Toast.makeText(XinZengScanNewActivity.this, "Rfid标签已经存在!", 0).show();
			}else {
				Toast.makeText(XinZengScanNewActivity.this, "盘盈新增失败!", 0).show();
			}
		}
		
		
	}
	
	private boolean validData(YingPanXinZengItem item) {
		if(TextUtils.isEmpty(item.getName())) {
			Toast.makeText(XinZengScanNewActivity.this, "请填写资产名称!", 0).show();
			item.getAssetNameEt().requestFocus();
			return false;
		}else if (TextUtils.isEmpty(item.getKeeper())) {
			Toast.makeText(XinZengScanNewActivity.this, "请填写责任人!", 0).show();
			item.getFuzerennameEt().requestFocus();
			return false;
		}else if(TextUtils.isEmpty(item.getZcflCode())) {
			Toast.makeText(XinZengScanNewActivity.this, "请选择资产分类!", 0).show();
			return false;
		}else if(TextUtils.isEmpty(item.getZclxCode())) {
			Toast.makeText(XinZengScanNewActivity.this, "请选择资产类别!", 0).show();
			return false;
		}
		return true;
	}

	private boolean isExitsAlready(String rfidCode) {
		for (YingPanXinZengItem item : items) {
			if (item.getRfidCodeTv().getText().toString().equals(rfidCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_xinzeng_new);
		wd = new WaitingDialog(this);
		planId = this.getIntent().getStringExtra("planId");
		userid = this.getIntent().getStringExtra("userid");
		deptcode = this.getIntent().getStringExtra("deptcode");
		isCk = this.getIntent().getStringExtra("isCk");
		container = (LinearLayout) this.findViewById(R.id.container);
		layoutInflater = LayoutInflater.from(this);
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
		start();
	}

	public void onEvent(SelectClassifyAndTypeListener event) {
		YingPanXinZengItem item = items.get(currentIdx);
		item.getZclbTv().setText(event.getZclxName());
		item.getZcflTv().setText(event.getZcflName());
		item.setZcflCode(event.getZcflCode());
		item.setZcflName(event.getZcflName());
		item.setZclxCode(event.getZclxCode());
		item.setZclxName(event.getZclxName());
	}
	
	public void onEvent(RefreshNumberListener event) {
//		Toast.makeText(this, "items size = " + items.size(), 0).show();
	}

	private void setOnClicks() {

	}


	private void findViews() {

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
								mUii = uii;

							}

							@Override
							public void onFinishedSuccessfully(Integer arg0,
									int arg1, int arg2) {
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
	protected void onDestroy() {
		super.onDestroy();
		stop();
		EventBus.getDefault().unregister(this);
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.goHome:
			Intent intent = new Intent(XinZengScanNewActivity.this, PlanActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 99) {
			String zcflName = data.getStringExtra("zcflName");
			String zcflCode = data.getStringExtra("zcflCode");
			items.get(currentIdx).getZcflTv().setText(zcflName);
			items.get(currentIdx).setZcflCode(zcflCode);
			items.get(currentIdx).setZcflName(zcflName);
			items.get(currentIdx).getZclbTv().setText("");
			items.get(currentIdx).setZclxCode("");
			items.get(currentIdx).setZclxName("");
		}else if(resultCode == 98) {
			String zclxName = data.getStringExtra("zclxName");
			String zclxCode = data.getStringExtra("zclxCode");
			items.get(currentIdx).getZclbTv().setText(zclxName);
			items.get(currentIdx).setZclxCode(zclxCode);
			items.get(currentIdx).setZclxName(zclxName);
		}
	}
	
}
