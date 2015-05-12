package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.StockItemNew;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class AssetDetailActivity extends Activity {
	private ArrayList<StockItemNew> dataArray = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				String rfid = getIntent().getStringExtra("rfid");
				dataArray = new HttpClientUtil(AssetDetailActivity.this).checkAssetByRfidOnly(AssetDetailActivity.this, rfid);
				
				return null;
			}
			
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
