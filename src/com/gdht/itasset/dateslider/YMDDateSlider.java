package com.gdht.itasset.dateslider;

import java.util.Calendar;

import android.content.Context;

import com.gdht.itasset.R;


public class YMDDateSlider extends DateSlider {

	public YMDDateSlider(Context context, OnDateSetListener l, Calendar calendar, 
			Calendar minTime, Calendar maxTime, String titleStr) {
        super(context, R.layout.ymddateslider, l, calendar, minTime, maxTime, titleStr);
    }
	
    public YMDDateSlider(Context context, OnDateSetListener l, Calendar calendar, String titleStr) {
        super(context, R.layout.ymddateslider, l, calendar, titleStr);
    }
	
}
