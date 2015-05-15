package com.gdht.itasset.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.RealName;

public class LocalPandianService {
	SQLiteDatabase db;

	public LocalPandianService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	public void save(String planid, String username, String rfids) {
		ArrayList<String> rfidList = getRifdsByPlanId(planid, username);
		if(rfidList == null) {
			db.execSQL(
					"insert into local_pandian (username, planid, rfids) values (?, ?, ?)",
					new String[] { username, planid, rfids });
		}else {
			Log.i("a", "local_pandian 数据库已经存在  " + rfidList.toString());
		}
	}

	public ArrayList<String> getRifdsByPlanId(String planid, String username) {
		ArrayList<String> rfids = null;
		Cursor cursor = db.rawQuery("select rfids from local_pandian where planid = ? and username = ?", new String[] {planid, username});
		String rfidStr = "";
		if(cursor.moveToFirst()) {
			rfidStr = cursor.getString(cursor.getColumnIndex("rfids"));
			String[] str = rfidStr.split(",");
			if(str.length > 0) {
				rfids = new ArrayList<String>();
				for(String s : str) {
					rfids.add(s);
				}
			}
		}
		return rfids;
	}

	public void delete() {
		if (getCount()) {
			db.execSQL("delete from realnames");
		}
	}

	public boolean getCount() {
		Cursor cursor = db.rawQuery("select count(*) from realnames", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public void close() {
		db.close();
	}

}
