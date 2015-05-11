package com.gdht.itasset.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdht.itasset.R;
import com.gdht.itasset.pojo.PlanInfo;
import com.gdht.itasset.pojo.PlanInfoNew;

public class PlanListAdapterNew extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<PlanInfo> plans;
	public PlanListAdapterNew(Context context, List<PlanInfo> plans) {
		this.inflater = LayoutInflater.from(context);
		this.context =context;
		this.plans = plans;
	}
	
	@Override
	public int getCount() {
		return plans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return plans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder vh;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.item_plan_list, null);
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.name);
			vh.location = (TextView) convertView.findViewById(R.id.location);
			convertView.setTag(vh);
		}
		vh = (ViewHolder) convertView.getTag();
		PlanInfo pi = plans.get(position);
		vh.name.setText(pi.getTitle());
		vh.location.setText(pi.getDepts());
		return convertView;
	}
	
	static class ViewHolder {
		TextView name, location;
		
	}

}

















