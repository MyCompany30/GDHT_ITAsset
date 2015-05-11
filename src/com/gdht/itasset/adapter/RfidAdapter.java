package com.gdht.itasset.adapter;

import java.util.List;

import com.gdht.itasset.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RfidAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<String> rfids;
	public RfidAdapter(Context context, List<String> rfids) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.rfids = rfids;
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.item_rfid_list, null);
			vh = new ViewHolder();
			vh.rfid = (TextView) convertView.findViewById(R.id.rfid);
			convertView.setTag(vh);
		}
		vh = (ViewHolder) convertView.getTag();
		vh.rfid.setText(rfids.get(position));
		return convertView;
	}
	
	static class ViewHolder {
		TextView rfid;
	}

}






















