package com.gdht.itasset;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class HomePageActivity extends Activity {
	
	private ImageView saomiaoBtn = null;
	private ImageView pandianBtn = null;
	private ImageView shezhiBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		findViews();
	}

	private void findViews() {
		saomiaoBtn = (ImageView)findViewById(R.id.saomiao);
		pandianBtn = (ImageView)findViewById(R.id.pandian);
		shezhiBtn = (ImageView)findViewById(R.id.shezhi);
	    
	}
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.saomiao:
			startActivity(new Intent(this, ScanActivityNew.class));
			break;
		case R.id.pandian:
			Intent intent = new Intent();
			intent.setClass(HomePageActivity.this, PlanListActivity.class);
			if(getIntent().hasExtra("name")){
				intent.putExtra("name", getIntent().getStringExtra("name"));
			}
			if(getIntent().hasExtra("planList")){
				intent.putExtra("planList", getIntent().getStringExtra("planList"));
			}
			HomePageActivity.this.startActivity(intent);
			break;
		case R.id.shezhi:
			Intent shezhiIntent = new Intent().setClass(HomePageActivity.this, OptionActivity.class);
			shezhiIntent.putExtra("model", "1");
			startActivity(shezhiIntent);
			break;
		}
	}
}
