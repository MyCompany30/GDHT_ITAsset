package com.gdht.itasset.adapter;

import java.util.ArrayList;

import com.gdht.itasset.R;
import com.gdht.itasset.pojo.StockItem;
import com.gdht.itasset.widget.CheckLinearLayout;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PdListAdapter extends BaseAdapter implements ListAdapter {
	private ArrayList<StockItem> itemArray = new ArrayList<StockItem>();
	private ArrayList<CheckLinearLayout> views = new ArrayList<CheckLinearLayout>();
	private Activity activity = null;
	private View color = null;
	private View colorIn = null;
	private int[] colors = null;
	private String[] colorsIn = null;
	
	public ArrayList<StockItem> getItemArray() {
		return itemArray;
	}

	public void setItemArray(ArrayList<StockItem> itemArray) {
		this.itemArray = itemArray;
	}

	public PdListAdapter(Activity activity,ArrayList<StockItem> array ) {
		this.activity = activity;
		itemArray = array;
		colorsIn = activity.getResources().getStringArray(R.array.colorIn_array);
		TypedArray ar = activity.getResources().obtainTypedArray(R.array.color_array);
		int len = ar.length();     
		colors = new int[len];     
		for (int i = 0; i < len; i++)     
			colors[i] = ar.getResourceId(i, 0);
		ar.recycle();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return views.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return views.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		convertView = (CheckLinearLayout)inflater.inflate(R.layout.item_yipan_panying_pankui, null);
		views.add(position, (CheckLinearLayout) convertView);
		TextView rfidTv = (TextView)convertView.findViewById(R.id.listitem_tv2);
		rfidTv.setText(itemArray.get(position).getRfidLabelnum());
		TextView assetNameTv = (TextView)convertView.findViewById(R.id.listitem_tv4);
		assetNameTv.setText(itemArray.get(position).getAssetName());
		TextView assetTypeTv = (TextView)convertView.findViewById(R.id.listitem_tv6);
		assetTypeTv.setText(itemArray.get(position).getAssetType());
		TextView keeperTv = (TextView)convertView.findViewById(R.id.listitem_tv8);
		keeperTv.setText(itemArray.get(position).getKeeper());
		color = convertView.findViewById(R.id.color);
		colorIn = convertView.findViewById(R.id.color_in);
		color.setBackgroundResource(colors[position%colors.length]);
		colorIn.setBackgroundColor(Color.parseColor(colorsIn[position%colorsIn.length]));
		return convertView;
}

}
