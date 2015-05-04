package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.pojo.StockItem;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ScanComplateActivity extends Activity {
	
	private ListView listView = null;
	private ImageView xinZengBtn = null;
	private ImageView genggaiBtn = null;
	private RfidListAdapter adapter = new RfidListAdapter(); 
	private ArrayList<String> list = null;
	private ArrayList<StockItem> dataArray = new ArrayList<StockItem>();
	private static StockItem currentSelectItem = null; 
	private ArrayList<PlanAssetInfo> planAssetArrayList = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_complate);
		list = getIntent().getStringArrayListExtra("rfidArray");
		planAssetArrayList = (ArrayList<PlanAssetInfo>) getIntent().getSerializableExtra("assetInfoList");
		

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dataArray = new ArrayList<StockItem>();
		RFIDGetDataAsyncTask asyncTask = new RFIDGetDataAsyncTask(this, list);
		asyncTask.execute();
	}

	private void setOnClicks() {
		
		
	}

	private void findViews() {
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(adapter);
		xinZengBtn = (ImageView)findViewById(R.id.xinzeng_btn);
		genggaiBtn = (ImageView)findViewById(R.id.genggai_btn);
	}
    
	private class RfidListAdapter extends BaseAdapter {
		private ArrayList<Integer> openS = new ArrayList<Integer>();
		private ArrayList<Integer> closeS = new ArrayList<Integer>();
		@Override
		public int getCount() {
			return dataArray.size();
		}

		@Override
		public Object getItem(int position) {
			return dataArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void showMoreInfos(int selectId) {
			this.openS.add(selectId);
			this.notifyDataSetChanged();
		}
		
		public void closeMoreInfos(int selectId){
			this.closeS.add(selectId);
			this.notifyDataSetChanged();
		}
		

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = ScanComplateActivity.this.getLayoutInflater();
			View v  = inflater.inflate(R.layout.scan_complate_listitem, null);
			convertView = v;
			//findViews...
			final View l1 = (View) convertView.findViewById(R.id.l1);
			final View l2 = (View) convertView.findViewById(R.id.l2);
			final CheckBox checkBox  = (CheckBox)convertView.findViewById(R.id.chkBox);
			final CheckBox checkBox_  = (CheckBox)convertView.findViewById(R.id.chkBox_);
			final ImageView pointBtn = (ImageView)convertView.findViewById(R.id.pointBtn);
			final ImageView pointBtn2 = (ImageView)convertView.findViewById(R.id.pointBtn2);
			final ImageView panBtn = (ImageView)convertView.findViewById(R.id.panBtn);
			final TextView tv1 = (TextView)convertView.findViewById(R.id.tv1);
			final TextView tv1_ = (TextView)convertView.findViewById(R.id.tv1_);
			final TextView tv2 = (TextView)convertView.findViewById(R.id.tv2);
			final TextView tv3 = (TextView)convertView.findViewById(R.id.tv3);
			final TextView tv4 = (TextView)convertView.findViewById(R.id.tv4);
			final TextView tv5 = (TextView)convertView.findViewById(R.id.tv5);
			final TextView tv6 = (TextView)convertView.findViewById(R.id.tv6);
			final TextView tv7 = (TextView)convertView.findViewById(R.id.tv7);
			final TextView tv22 = (TextView)convertView.findViewById(R.id.assetType_);
			final TextView tv33= (TextView)convertView.findViewById(R.id.assetName_);
			final TextView tv44 = (TextView)convertView.findViewById(R.id.brand_);
			final TextView tv55 = (TextView)convertView.findViewById(R.id.dept_);
			final TextView tv66 = (TextView)convertView.findViewById(R.id.person_);
			final TextView tv77 = (TextView)convertView.findViewById(R.id.status_);
			//setText...
			for(int i = 0; i < planAssetArrayList.size(); i++){
				if(planAssetArrayList.get(i).getRfidnumber().equals(dataArray.get(position).getRfidLabelnum())){
					convertView.setBackgroundColor(Color.parseColor("#d3fac7"));
					break;
				}
				if(i==planAssetArrayList.size()-1){
					convertView.setBackgroundColor(Color.parseColor("#fbc9cc"));
				}
				
			}
			tv1.setText(dataArray.get(position).getRfidLabelnum());
			tv1_.setText(dataArray.get(position).getRfidLabelnum());
			String assetType = dataArray.get(position).getAssetType();
			if(assetType == null || "null".equals(assetType)) {
				assetType = "";
			}
			tv2.setText(assetType);
			String assetName = dataArray.get(position).getAssetName();
			if(assetName == null || "null".equals(assetName)) {
				assetName = "";
			}
			tv3.setText(assetName);
			String brandModel = dataArray.get(position).getBrandModel();
			if(brandModel == null || "null".equals(brandModel) || "null-null".endsWith(brandModel) || "null-".equals(brandModel)) {
				brandModel = "";
			}
			tv4.setText(brandModel);
			String bumenOrBangongshi = "";
			if(dataArray.get(position).getDeptOffice()!=null&&dataArray.get(position).getDeptOffice().length()>0){
				bumenOrBangongshi = dataArray.get(position).getDeptOffice();
			}else{
				bumenOrBangongshi = dataArray.get(position).getDeptQyHj();
			}
			if(bumenOrBangongshi != null) {
				bumenOrBangongshi = bumenOrBangongshi.replaceAll("null", "");
			}else {
				bumenOrBangongshi = "";
			}
			tv5.setText(bumenOrBangongshi);
			String keeper = dataArray.get(position).getKeeper();
			if(keeper == null || "null".equals(keeper) ) {
				keeper = "";
			}
			tv6.setText(keeper);
			tv7.setText(dataArray.get(position).getCheckstate());
			if(openS.contains(new Integer(position))) {
				l1.setVisibility(View.VISIBLE);
				l2.setVisibility(View.GONE);
			}
			if(closeS.contains(new Integer(position))) {
				l1.setVisibility(View.GONE);
				l2.setVisibility(View.VISIBLE);
			}
			final int location = position;
			pointBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					showMoreInfos(location);
					closeS.add(location);
					openS.remove(new Integer(location));
					closeMoreInfos(location);
				}
			});
			pointBtn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					openS.add(location);
					closeS.remove(new Integer(location));
					showMoreInfos(location);
				}
			});
			//checkBox...
			if(dataArray.get(position).isChecked()){
				checkBox.setChecked(true);
				checkBox_.setChecked(true);
			}else{
				checkBox.setChecked(false);
				checkBox_.setChecked(false);
			}
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					for(StockItem item : dataArray){
						item.setChecked(false);
					}
					currentSelectItem = dataArray.get(position);
					currentSelectItem.setChecked(true);
					adapter.notifyDataSetChanged();
				}
			});
			checkBox_.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					for(StockItem item : dataArray){
						item.setChecked(false);
					}
					currentSelectItem = dataArray.get(position);
					currentSelectItem.setChecked(true);
					adapter.notifyDataSetChanged();
				}
			});
			
			genggaiBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(currentSelectItem==null){
						Toast.makeText(ScanComplateActivity.this, "没有被选中的项目", Toast.LENGTH_SHORT).show();
						return;
					}
					if(currentSelectItem.getUseType().equals("1")){
						//库存
						Intent intent = new Intent();
						intent.setClass(ScanComplateActivity.this, XiuGaiCangKuActivity.class);
						intent.putExtra("rfid", currentSelectItem.getRfidLabelnum());
						intent.putExtra("keeper", currentSelectItem.getKeeper());
						intent.putExtra("assetInfoId", currentSelectItem.getAssetInfoId());
						intent.putExtra("dept", currentSelectItem.getDept());
						intent.putExtra("usetype",currentSelectItem.getUseType());
						intent.putExtra("detil",currentSelectItem.getDetil());
						ScanComplateActivity.this.startActivity(intent);
					}else if(currentSelectItem.getUseType().equals("2")){
						//在运
						Intent intent = new Intent();
						intent.setClass(ScanComplateActivity.this, XiuGaiZaiYunActivity.class);
						intent.putExtra("rfid", currentSelectItem.getRfidLabelnum());
						intent.putExtra("keeper", currentSelectItem.getKeeper());
						intent.putExtra("assetInfoId", currentSelectItem.getAssetInfoId());
						intent.putExtra("dept", currentSelectItem.getDept());
						intent.putExtra("office", currentSelectItem.getOffice());
						intent.putExtra("usetype",currentSelectItem.getUseType());
						intent.putExtra("detil",currentSelectItem.getDetil());
						ScanComplateActivity.this.startActivity(intent);
					}
				}
			});
			xinZengBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(currentSelectItem==null){
						Toast.makeText(ScanComplateActivity.this, "没有被选中的项目", Toast.LENGTH_SHORT).show();
						return;
					}
					if((!currentSelectItem.getCheckstate().equals("未盘"))&&(!currentSelectItem.getDept().equals(""))){
						return;
					}
					Intent intent = new Intent();
					intent.setClass(ScanComplateActivity.this, CangKuSelectActivity.class);
					intent.putExtra("rfid", currentSelectItem.getRfidLabelnum());
					ScanComplateActivity.this.startActivity(intent);
				}
			});
			
			//panBtn...
			if(dataArray.get(position).getCheckstate().equals("未盘")){
				//未盘
				panBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//发送已盘请求
						new AsyncTask<Void, Void, String>(){
							WaitingDialog dialog = new WaitingDialog(ScanComplateActivity.this);
							protected void onPreExecute() {
								dialog.show();
							};
							@Override
							protected String doInBackground(Void... params) {
								
								return new HttpClientUtil(ScanComplateActivity.this).updateAssetStatus(ScanComplateActivity.this, GlobalParams.planId, dataArray.get(position).getAssetInfoId(), "", "1", GlobalParams.username);
							}
							protected void onPostExecute(String result) {
								if(result.equals("1")){
									Toast.makeText(ScanComplateActivity.this, "盘点成功", Toast.LENGTH_SHORT).show();
									panBtn.setImageResource(R.drawable.yipan_pp);
									tv7.setText("已盘");
									panBtn.setClickable(false);
								}
								dialog.dismiss();
							};
							
						}.execute();
					}
				});
			}else if(dataArray.get(position).getCheckstate().equals("已盘")){
				//已盘
				panBtn.setImageResource(R.drawable.yipan_pp);
				panBtn.setClickable(false);
			}else if(dataArray.get(position).getCheckstate().equals("盘盈")){
				//盘盈
				tv7.setText("盘盈");
				panBtn.setVisibility(View.INVISIBLE);
			}else if(dataArray.get(position).getCheckstate().equals("盘亏")){
				//盘亏
				tv7.setText("盘亏");
			}
			return convertView;
		}
		
	} 
	
	
	public class RFIDGetDataAsyncTask extends AsyncTask<Void, Void, Void> {
		
		private Activity activity = null;
		private ArrayList<String> rfidCodeList = null;
		private WaitingDialog dialog;
		
		public RFIDGetDataAsyncTask(Activity context, ArrayList<String> list) {
			this.activity = context;
			this.rfidCodeList = list;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new WaitingDialog(activity);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String data = "";
			for(int i = 0; i<rfidCodeList.size();i++){
				data += rfidCodeList.get(i);
				if(i==rfidCodeList.size()-1)
					break;
				data+=",";
			}
			new HttpClientUtil(ScanComplateActivity.this).checkAssetByCodes(activity,GlobalParams.planId ,R.string.url_checkAssetByRfid,"Rfid", data, dataArray);
			sort(dataArray);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			adapter.showMoreInfos(0);
			dialog.cancel();
			findViews();
			setOnClicks();
		}

	}
	private void sort(ArrayList<StockItem> dataArray) {
		StockItem s1;
		StockItem s2;
		int index = 0;
		for(int i = 0; i<dataArray.size(); i++){
			for(int j = 0; j< planAssetArrayList.size(); j++){
				if(dataArray.get(i).getRfidLabelnum().equals(planAssetArrayList.get(j).getRfidnumber())){
					s1 = dataArray.get(i);
					s2 = dataArray.get(index);
					dataArray.set(index, s1);
					dataArray.set(i, s2);
					index++;
					continue;
				}
			}
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
