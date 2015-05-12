package com.gdht.itasset.adapter;

import java.util.List;

import com.gdht.itasset.R;
import com.gdht.itasset.eventbus.RefreshNumberListener;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class RfidPanDianAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<String> rfids;
	private List<String> selectRifds;
	public RfidPanDianAdapter(Context context, List<String> rfids, List<String> selectRifds) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.rfids = rfids;
		this.selectRifds = selectRifds;
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
//		ViewHolder vh;
//		if(convertView == null) {
//			convertView = inflater.inflate(R.layout.item_rfid_pandian_list, null);
//			vh = new ViewHolder();
//			vh.rfid = (TextView) convertView.findViewById(R.id.rfid);
//			vh.cb = (CheckBox) convertView.findViewById(R.id.cb);
//			convertView.setTag(vh);
//		}
//		vh = (ViewHolder) convertView.getTag();
//		vh.rfid.setText(rfids.get(position));
		convertView = inflater.inflate(R.layout.item_rfid_pandian_list, null);
		TextView rfid = (TextView) convertView.findViewById(R.id.rfid);
		rfid.setText(rfids.get(position));
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
		final int location = position;
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1) {
					if(!selectRifds.contains(rfids.get(location))) {
						selectRifds.add(rfids.get(location));
					}
				}else {
					selectRifds.remove(rfids.get(location));
				}
				EventBus.getDefault().post(new RefreshNumberListener());
			}
		});
		if(selectRifds.contains(rfids.get(position))) {
			cb.setChecked(true);
		}else {
			cb.setChecked(false);
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView rfid;
		CheckBox cb;
	}

}






















