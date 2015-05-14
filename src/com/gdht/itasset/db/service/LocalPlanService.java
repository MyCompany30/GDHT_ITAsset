package com.gdht.itasset.db.service;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.PlanInfo;
import com.gdht.itasset.pojo.StockItemNew;

public class LocalPlanService {
	SQLiteDatabase db;

	public LocalPlanService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public Long save(List<PlanInfo> lists) {
		delete();
		for (PlanInfo si : lists) {
			db.execSQL(
					"insert into local_plan (id, title, type, depts, number, planstate, detail, deptcode, qdtime, wctime, persons) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					new String[] { si.getId(), si.getTitle(), si.getType(), si.getDepts(), String.valueOf(si.getNumber()), si.getPlanstate(), si.getDetail(), si.getDeptcode(), si.getQdtime(), si.getWctime(), si.getPersons() });
		}
		return getCountNumber();
	}
	
	public Long getCountNumber() {
		Cursor cursor = db.rawQuery("select count(*) from local_plan", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count;
	}
	
	public void delete() {
		if(getCount()) {
			db.execSQL("delete from local_plan");
		}
	}
	
	public boolean getCount() {
		Cursor cursor = db.rawQuery("select count(*) from local_plan", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public void close() {
		db.close();
	}
	
}
