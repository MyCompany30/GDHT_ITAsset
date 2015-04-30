package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.gdht.itasset.adapter.YingPanXinZenItemAdapter;
import com.gdht.itasset.eventbus.SelectCangKuListener;
import com.gdht.itasset.pojo.YingPanXinZengItem;
import com.gdht.itasset.utils.GlobalParams;

import de.greenrobot.event.EventBus;

public class XinZengHaveRfidActivity extends Activity {
	private Intent intent;
	private ListView listView;
	private YingPanXinZenItemAdapter adapter;
	private List<YingPanXinZengItem> items = new ArrayList<YingPanXinZengItem>();
	private YingPanXinZengItem item;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_haverfid_xinzeng);
		// pd = new ProgressDialog(this);
		// pd.setMessage("数据保存中...");
		EventBus.getDefault().register(this);
		findViews();
	}

	private void findViews() {
		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new YingPanXinZenItemAdapter(this, items);
		listView.setAdapter(adapter);
		YingPanXinZengItem item = new YingPanXinZengItem();
		item.setRfid_labelnum(GlobalParams.rfid);
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
	
	public void onEvent(SelectCangKuListener event) {
		YingPanXinZengItem item = items.get(event.getLocation());
		item.setDept(event.getDept());
		item.setIsck(event.getIsCk());
		item.setDeptName(event.getDeptName());
		adapter.notifyDataSetChanged();
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.scan_clear:
			items.clear();
			adapter.notifyDataSetChanged();
			break;
		case R.id.goHome:
			Intent intent = new Intent(XinZengHaveRfidActivity.this, PlanActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
	}


	private boolean isExitsAlready(String rfidCode) {
		for (YingPanXinZengItem item : items) {
			if (item.getRfid_labelnum().equals(rfidCode)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
