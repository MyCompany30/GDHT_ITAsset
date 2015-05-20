package com.gdht.itasset.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gdht.itasset.AssetDetailActivity;
import com.gdht.itasset.R;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class RfidSelectAdapter extends RfidAdapter {
	
	private ArrayList<String> checkedRfid = new ArrayList<String>();
	private ArrayList<Boolean> checkIndexArray = new ArrayList<Boolean>();  
	
	public RfidSelectAdapter(Context context, List<String> rfids, String planId) {
		super(context, rfids, planId);
		// TODO Auto-generated constructor stub
		for(String s : rfids){
			checkedRfid.add(s);
			checkIndexArray.add(true);
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.item_rfid_select_list, null);
		CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.cb);
		final TextView rfidTv = (TextView)convertView.findViewById(R.id.rfid);
		if(checkIndexArray.get(position)){
			checkBox.setChecked(true);
		}else{
			checkBox.setChecked(false);
		}
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!isChecked){
					checkedRfid.remove(rfidTv.getText().toString());
				}else{
					if(!checkedRfid.contains(rfidTv.getText().toString())){
						checkedRfid.add(rfidTv.getText().toString());
					}
				}
			}
		});
		rfidTv.setText(rfids.get(position));
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String rfid = rfids.get(position);
				Intent intent = new Intent();
				intent.setClass(context, AssetDetailActivity.class);
				intent.putExtra("rfid", rfid);
				if(planId != null){
					intent.putExtra("planId", planId);
				}
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	public String getCheckedRfid() {
		String strs = "";
		for(int i = 0; i < checkedRfid.size(); i++){
			if(i==0){
				strs +="'";
			}
			strs += checkedRfid.get(i);
			if(i== checkedRfid.size()-1){
				strs += "'";
			}else{
				strs += "','";
			}
		}
		return strs;
	}
	public ArrayList<String> getOriginalRfids(){
		return checkedRfid;
	}
}