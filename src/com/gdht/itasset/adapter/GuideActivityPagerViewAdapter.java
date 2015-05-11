package com.gdht.itasset.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class GuideActivityPagerViewAdapter extends PagerAdapter {
	private ArrayList<View> views = null;
	public GuideActivityPagerViewAdapter(ArrayList<View> views) {
		this.views = views;
	}
	
	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	};
}
