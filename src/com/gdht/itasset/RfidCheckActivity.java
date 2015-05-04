package com.gdht.itasset;

import java.util.ArrayList;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.pojo.StockItem;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RfidCheckActivity extends Activity {
	private String code;
	private ArrayList<StockItem> dataArray = new ArrayList<StockItem>();
	private ListView listView = null;
	private RfidListAdapter adapter = new RfidListAdapter(); 
	private ArrayList<PlanAssetInfo> planAssetArrayList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_check);
		this.code = this.getIntent().getStringExtra("code");
		planAssetArrayList = (ArrayList<PlanAssetInfo>) getIntent().getSerializableExtra("assetInfoList");
		if(code != null && !code.equals("")){
			listView = (ListView)findViewById(R.id.listView);
			adapter = new RfidListAdapter();
			listView.setAdapter(adapter);
			new RFIDGetDataAsyncTask().execute();
		}else {
			Toast.makeText(RfidCheckActivity.this, "查看详情失败!", 0).show();
			this.finish();
		}
		
	}
	
public class RFIDGetDataAsyncTask extends AsyncTask<Void, Void, Void> {
		private WaitingDialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new WaitingDialog(RfidCheckActivity.this);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			new HttpClientUtil(RfidCheckActivity.this).checkAssetByCodes(RfidCheckActivity.this,GlobalParams.planId ,R.string.url_checkAssetByRfid,"Rfid", code, dataArray);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			adapter.showMoreInfos(0);
			dialog.cancel();
//			setOnClicks();
		}

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
		LayoutInflater inflater = RfidCheckActivity.this.getLayoutInflater();
		View v  = inflater.inflate(R.layout.scan_complate_listitem_check, null);
		convertView = v;
		//findViews...
		final View l1 = (View) convertView.findViewById(R.id.l1);
		final View l2 = (View) convertView.findViewById(R.id.l2);
		final ImageView pointBtn = (ImageView)convertView.findViewById(R.id.pointBtn);
		final ImageView pointBtn2 = (ImageView)convertView.findViewById(R.id.pointBtn2);
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
		pointBtn.setVisibility(View.GONE);
		pointBtn2.setVisibility(View.GONE);
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
		
		//panBtn...
		if(dataArray.get(position).getCheckstate().equals("未盘")){
			//未盘
		}else if(dataArray.get(position).getCheckstate().equals("已盘")){
			//已盘
//			panBtn.setImageResource(R.drawable.yipan_pp);
//			panBtn.setClickable(false);
		}else if(dataArray.get(position).getCheckstate().equals("盘盈")){
			//盘盈
			tv7.setText("盘盈");
//			panBtn.setVisibility(View.INVISIBLE);
		}else if(dataArray.get(position).getCheckstate().equals("盘亏")){
			//盘亏
			tv7.setText("盘亏");
		}
		return convertView;
	}
	
} 
	
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		}
	}
}
