package com.gdht.itasset.adapter;

import com.gdht.itasset.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommenListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private String[] values;
	public CommenListAdapter(Context context, String[] values) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.values = values;
	}
	
	@Override
	public int getCount() {
		return values.length;
	}

	@Override
	public Object getItem(int arg0) {
		return values[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int postion, View contentView, ViewGroup arg2) {
		contentView = inflater.inflate(R.layout.common_item_listview, null);
		TextView nameTv = (TextView) contentView.findViewById(R.id.name);
		nameTv.setText(values[postion]);
		return contentView;
	}

}


















