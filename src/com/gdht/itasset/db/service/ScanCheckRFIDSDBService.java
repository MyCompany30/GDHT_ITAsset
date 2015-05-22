package com.gdht.itasset.db.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gdht.itasset.db.GDHTCommonOpenHelper;
import com.gdht.itasset.db.GDHTDataSourceOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScanCheckRFIDSDBService {
	SQLiteDatabase db;
	public ScanCheckRFIDSDBService(Context context) {
		GDHTCommonOpenHelper helper = new GDHTCommonOpenHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void saveRFID(String rfid, String user) {
		if(existByName(rfid, user)) {
			
		}else {
			db.execSQL("insert into scan_check_rfids (rfid, user) values (?, ?)", new String[]{rfid, user});
		}
	}
	
	public boolean existByName(String name, String user) {
		Cursor cursor = db.rawQuery("select count(*) from scan_check_rfids where rfid = ? and user = ?", new String[]{name, user});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public boolean existByUser(String user) {
		Cursor cursor = db.rawQuery("select count(*) from scan_check_rfids where user = ?", new String[]{user});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public List<String> loadDatas(String user) {
		List<String> results = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select * from scan_check_rfids where user = ?", new String[]{user});
		while(cursor.moveToNext()) {
			results.add(cursor.getString(cursor.getColumnIndex("rfid")));
		}
		return results;
	}
	
	public void deleteAll(String user) {
		if(existByUser(user)) {
			db.execSQL("delete from scan_check_rfids where user = ?", new String[]{user});
		}

	}
	
	public void deleteByRFID(String rfid) {
		db.execSQL("delete from scan_check_rfids where rfid = ?",
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

	



















