package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.adapter.PdListAdapter;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.StockItem;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.CheckLinearLayout;
import com.gdht.itasset.widget.WaitingDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

public class PanYingActivity extends Activity {
	private ImageView guzhangBtn = null;
	private ListView listView = null;
	private PdListAdapter adapter = null;
	private EditText searchEdt = null;
	private ArrayList<StockItem> itemArray = null;
	private CheckLinearLayout currSectItem = null;
	private ArrayList<StockItem> dataArray = null;
	private ArrayList<StockItem> itemArrayTemp = new ArrayList<StockItem>();
	private ImageView clearBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		itemArray = new ArrayList<StockItem>();
		dataArray = new ArrayList<StockItem>();
		setContentView(R.layout.activity_pan_ying);
		//故障登记
		guzhangBtn = (ImageView)findViewById(R.id.guzhangdengji_btn);
		clearBtn = (ImageView) findViewById(R.id.clear);
		guzhangBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>(){
					protected void onPreExecute() {
						
					};
					@Override
					protected Void doInBackground(Void... params) {
						// TODO Auto-generated method stub
						if(currSectItem==null)
							return null;
						String rfid = ((TextView)currSectItem.findViewById(R.id.listitem_tv2)).getText().toString();
						new HttpClientUtil(PanYingActivity.this).checkAssetByCodes(PanYingActivity.this, GlobalParams.planId, R.string.url_checkAssetByRfid, "Rfid", rfid, dataArray);
						return null;
					}
					protected void onPostExecute(Void result) {
						if(dataArray.size()>0){
							StockItem stockItem = dataArray.get(0);
							Intent intent = new Intent();
							intent.putExtra("rfid", stockItem.getRfidLabelnum());
							intent.putExtra("assetType", stockItem.getAssetType());
							intent.putExtra("assetName", stockItem.getAssetName());
							intent.putExtra("assetInfoId", stockItem.getAssetInfoId());
							if(stockItem.getUseType().equals("1")){
								intent.putExtra("position", stockItem.getQy()+stockItem.getHj());
							}else if(stockItem.getUseType().equals("2")){
								intent.putExtra("position", stockItem.getOffice());
							}
							intent.putExtra("dept", stockItem.getDept());
							intent.setClass(PanYingActivity.this, GuZhangDengJiActivity.class);
							PanYingActivity.this.startActivity(intent);
						}
					};
				}.execute();
			}
		});
		
		listView = (ListView)findViewById(R.id.panying_listView);
		searchEdt = (EditText) findViewById(R.id.search_edt);
		adapter = new PdListAdapter(PanYingActivity.this, itemArray); 
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
					for(int i = 0; i<listView.getChildCount(); i++){
						if(i==position)
							continue;
						((CheckLinearLayout)listView.getChildAt(i)).setChecked(false);
					}
					itemLayout.setChecked(true);
					currSectItem = itemLayout;
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
					clearBtn.setVisibility(View.GONE);
					itemArray.clear();
					itemArray.addAll(itemArrayTemp);
					adapter.notifyDataSetChanged();
				}else {
					clearBtn.setVisibility(View.VISIBLE);
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
		
		new PanYingTask().execute();
	}
	class PanYingTask extends AsyncTask<Void, Void, String>{
		WaitingDialog dialog = new WaitingDialog(PanYingActivity.this);
		
		protected void onPreExecute() {
			dialog.show();
		};
		@Override
		protected String doInBackground(Void... params) {
			new HttpClientUtil(PanYingActivity.this).getDataByStatus(PanYingActivity.this, itemArray, GlobalParams.planId, "2");
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
		case R.id.clear:
			searchEdt.setText("");
			break;
		default:
			break;
		}
	}
	
}
