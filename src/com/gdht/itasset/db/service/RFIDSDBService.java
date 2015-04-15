package com.gdht.itasset.db.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gdht.itasset.db.GDHTOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RFIDSDBService {
	SQLiteDatabase db;
	public RFIDSDBService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void saveRFID(String rfid) {
		if(existByName(rfid)) {
			
		}else {
			db.execSQL("insert into rfids (rfid) values (?)", new String[]{rfid});
		}
	}
	
	public boolean existByName(String name) {
		Cursor cursor = db.rawQuery("select count(*) from rfids where rfid = ?", new String[]{name});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public List<String> loadDatas() {
		List<String> results = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select * from rfids", null);
		while(cursor.moveToNext()) {
			results.add(cursor.getString(cursor.getColumnIndex("rfid")));
		}
		return results;
	}
	
	public void deleteAll() {
		db.execSQL("delete from rfids");
	}
	
	public void deleteByRFID(String rfid) {
		db.execSQL("delete from rfids where rfid = ?",
				new String[]{rfid});
	}
	
	public void closeDB() {
		db.close();
		db = null;
	}
	
	public void saveloginInfo(String loginName) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			db.execSQL("insert into loginLog(login_name, login_time) values (?,?)", new String[]{loginName,dateFormat.format(new Date())});
	}
	
}

	



















