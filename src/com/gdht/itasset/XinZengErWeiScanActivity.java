package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.gdht.itasset.adapter.YingPanXinZenItemAdapter;
import com.gdht.itasset.pojo.YingPanXinZengItem;
import com.gdht.itasset.utils.GlobalParams;

public class XinZengErWeiScanActivity extends Activity {
	private Intent intent;
	private ListView listView;
	private YingPanXinZenItemAdapter adapter;
	private List<YingPanXinZengItem> items = new ArrayList<YingPanXinZengItem>();
	private YingPanXinZengItem item;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_erwei_xinzeng);
		// pd = new ProgressDialog(this);
		// pd.setMessage("数据保存中...");
		findViews();
	}
	
	

	private void findViews() {
		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new YingPanXinZenItemAdapter(this, items);
		listView.setAdapter(adapter);
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.scan_start_btn:
			intent = new Intent(XinZengErWeiScanActivity.this,
					ErWeiScanCaptureActivity.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.back:
			this.finish();
			break;
		case R.id.scan_clear:
			items.clear();
			adapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 100:
			Bundle bundle = data.getExtras();
			String code = bundle.getString("result");

			if (isExitsAlready(code)) {
				// 已经存在
				Toast.makeText(XinZengErWeiScanActivity.this, "已经存在!", 0)
						.show();
			} else {
				YingPanXinZengItem item = new YingPanXinZengItem();
				item.setRfid_labelnum(code);
				item.setAssetCheckplanId(GlobalParams.planId);
				item.setClassify(GlobalParams.zichanfenlei);
				item.setType(GlobalParams.zichanzifenlei);
				item.setRegistrant(GlobalParams.username);
				item.setDept(GlobalParams.cangKuValue);
				item.setOffice("");
				item.setIsck(GlobalParams.isck);
				item.setBuyDate("");
				items.add(item);
				adapter.notifyDataSetChanged();
			}
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
	}
}
