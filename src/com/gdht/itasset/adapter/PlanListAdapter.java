package com.gdht.itasset.adapter;

import java.util.ArrayList;

import com.gdht.itasset.R;
import com.gdht.itasset.pojo.PlanInfo;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlanListAdapter extends BaseAdapter {
	private ArrayList<PlanInfo> planDateList = null;
	private Activity activity = null;
	private TextView planName = null;
	private TextView planAddr = null;
	private View color = null;
	private View colorIn = null;
	private int[] colors = null;
	private String[] colorsIn = null;
	public PlanListAdapter(ArrayList<PlanInfo> dataList, Activity activity) {
		this.planDateList = dataList;
		this.activity = activity;
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
		return planDateList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return planDateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		convertView = inflater.inflate(R.layout.plan_select_listitem, null);
		color = convertView.findViewById(R.id.color);
		colorIn = convertView.findViewById(R.id.color_in);
		planName = (TextView)convertView.findViewById(R.id.listitem_tv1);
		planAddr = (TextView)convertView.findViewById(R.id.listitem_tv2);
		color.setBackgroundResource(colors[position%colors.length]);
		colorIn.setBackgroundColor(Color.parseColor(colorsIn[position%colorsIn.length]));
		planName.setText(planDateList.get(position).getTitle());
		planAddr.setText(planDateList.get(position).getDepts());
		return convertView;
	}

}
