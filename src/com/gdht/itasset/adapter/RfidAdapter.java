package com.gdht.itasset.adapter;

import java.util.List;

import com.gdht.itasset.AssetDetailActivity;
import com.gdht.itasset.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RfidAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<String> rfids;
	private String planId;
	public RfidAdapter(Context context, List<String> rfids, String planId) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.rfids = rfids;
		if(planId != null){
			this.planId = planId;
		}
	}
	
	@Override
	public int getCount() {
		return rfids.size();
	}

	@Override
	public Object getItem(int arg0) {
		return rfids.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.item_rfid_list, null);
			vh = new ViewHolder();
			vh.rfid = (TextView) convertView.findViewById(R.id.rfid);
			convertView.setTag(vh);
		}
		vh = (ViewHolder) convertView.getTag();
		vh.rfid.setText(rfids.get(position));
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
	
	static class ViewHolder {
		TextView rfid;
	}

}






















