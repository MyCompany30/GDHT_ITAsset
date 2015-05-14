package com.gdht.itasset.db.service;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.LocalPlanResult;
import com.gdht.itasset.pojo.RealName;

public class LocalRealNameService {
	SQLiteDatabase db;

	public LocalRealNameService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public Long save(List<RealName> lists) {
		delete();
		for (RealName lpr : lists) {
			db.execSQL(
					"insert into realnames (username, realname) values (?, ?)",
					new String[] { lpr.getUsername(), lpr.getRealname()});
		}
		return getCountNumber();
	}
	
	public Long getCountNumber() {
		Cursor cursor = db.rawQuery("select count(*) from realnames",
				null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count;
	}
	
	public void delete() {
		if (getCount()) {
			db.execSQL("delete from realnames");
		}
	}

	public boolean getCount() {
		Cursor cursor = db.rawQuery("select count(*) from realnames",
				null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}

	public void close() {
		db.close();
	}
}
