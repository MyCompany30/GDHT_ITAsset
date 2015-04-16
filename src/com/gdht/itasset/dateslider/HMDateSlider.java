package com.gdht.itasset.dateslider;

import java.util.Calendar;

import android.content.Context;

import com.gdht.itasset.R;


public class HMDateSlider extends DateSlider {

	public HMDateSlider(Context context, OnDateSetListener l, Calendar calendar, 
			Calendar minTime, Calendar maxTime, String titleStr) {
        super(context, R.layout.hmdateslider, l, calendar, minTime, maxTime, titleStr);
    }
	
    public HMDateSlider(Context context, OnDateSetListener l, Calendar calendar, String titleStr) {
        super(context, R.layout.hmdateslider, l, calendar, titleStr);
    }
	
}
