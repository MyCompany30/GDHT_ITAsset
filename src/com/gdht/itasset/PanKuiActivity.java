package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.adapter.PdListAdapter;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.StockItem;
import com.gdht.itasset.widget.CheckLinearLayout;
import com.gdht.itasset.widget.WaitingDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PanKuiActivity extends Activity {
	private ImageView cheXiaoBtn = null;
	private ListView listView = null;
	private PdListAdapter adapter = null;
	private EditText searchEdt = null;
	private ArrayList<StockItem> itemArray = new ArrayList<StockItem>();
	private ArrayList<StockItem> itemArrayTemp = new ArrayList<StockItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		itemArray = new ArrayList<StockItem>();
		itemArrayTemp = new ArrayList<StockItem>();
		setContentView(R.layout.activity_pan_kui);
		cheXiaoBtn = (ImageView)findViewById(R.id.chexiao_btn_);
		cheXiaoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i = 0; i< listView.getChildCount(); i++){
					final int position = i;
					CheckLinearLayout itemLayout = (CheckLinearLayout)listView.getChildAt(i);
					if(itemLayout.isChecked()){
						final StockItem item = itemArray.get(position);
						new AsyncTask<Void, Void, String>(){
							WaitingDialog dialog = new WaitingDialog(PanKuiActivity.this);
							
							protected void onPreExecute() {
								dialog.show();
							};
							@Override
							protected String doInBackground(Void... params) {
								return new HttpClientUtil(PanKuiActivity.this).updateAssetStatus(PanKuiActivity.this, PlanActivity.PLAN_ID, itemArray.get(itemArray.indexOf(item)).getAssetInfoId(), "", "0", PlanActivity.operator);
								
							}
							protected void onPostExecute(String result) {
								dialog.dismiss();
								if(result.equals("1")){
									itemArray.remove(item);
								}
							}
						}.execute();
					}
					
				}
			}
		});
		
		listView = (ListView)findViewById(R.id.pankui_listView);
		searchEdt = (EditText) findViewById(R.id.search_edt);
		adapter = new PdListAdapter(PanKuiActivity.this, itemArray); 
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Object obj = listView.getChildAt(position);
				CheckLinearLayout itemLayout = (CheckLinearLayout) obj;
				if(itemLayout.isChecked()){
					itemLayout.setChecked(false);
				}else{
					itemLayout.setChecked(true);
				}
			}
		});
searchEdt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				Log.i("a", "值: " + arg0.toString());
				if(TextUtils.isEmpty(arg0.toString())) {
					itemArray.clear();
					itemArray.addAll(itemArrayTemp);
					adapter.notifyDataSetChanged();
				}else {
					for(int i=0; i<itemArray.size(); i++) {
						StockItem si = itemArray.get(i);
						if(si.getRfidLabelnum().equals(arg0.toString())){
							itemArray.clear();
							itemArray.add(si);
							adapter.notifyDataSetChanged();
						}
					}
				}
			}
		});
		new PanKuiTask().execute();
	}
	
	class PanKuiTask extends AsyncTask<Void, Void, String>{
		WaitingDialog dialog = new WaitingDialog(PanKuiActivity.this);
		
		protected void onPreExecute() {
			dialog.show();
		};
		@Override
		protected String doInBackground(Void... params) {
			new HttpClientUtil(PanKuiActivity.this).getDataByStatus(PanKuiActivity.this, itemArray, PlanActivity.PLAN_ID, "3");
			return null;
		}
		protected void onPostExecute(String result) {
			itemArrayTemp.clear();
			itemArrayTemp.addAll(itemArray);
			dialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;

		default:
			break;
		}
	}
	
}
