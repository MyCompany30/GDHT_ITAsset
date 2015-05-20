package com.gdht.itasset.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.LocalPandian;
import com.gdht.itasset.pojo.RealName;

public class LocalPandianService {
	SQLiteDatabase db;

	public LocalPandianService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	public List<String> save(String planid, String username, String rfids) {
		String[] keys = rfids.split(",");
		String result = "";
		List<String> lists = new ArrayList<String>();
		for(String s : keys) {
			String ss = s.replaceAll("'", "");
			String stockPlanId = getPlanIdFromStock(ss);
//			Log.i("a", "stockPlanId = " + stockPlanId + " planId = " + planid);
			if(stockPlanId.equals(planid)) {
				if(getCountByRfid(username, planid, s)) {
					
				}else {
					if(!"".equals(ss)) {
						db.execSQL("insert into local_pandian(username, planid, rfid) values (?, ?, ?)", new String[]{username, planid, s});
					}
				}
			}
			
		}
		Cursor cursor = db.rawQuery("select rfid from local_pandian where username = ? and planid = ?", new String[]{username, planid});
		while(cursor.moveToNext()){
			lists.add(cursor.getString(cursor.getColumnIndex("rfid")));
		}
		for(String s : lists) {
			Log.i("a", "s = " + s);
		}
		return lists;
	}

	public ArrayList<String> getRifdsByPlanId(String planid, String username) {
		ArrayList<String> rfids = null;
		Cursor cursor = db.rawQuery("select rfids from local_pandian where planid = ? and username = ?", new String[] {planid, username});
		String rfidStr = "";
		if(cursor.moveToFirst()) {
			rfidStr = cursor.getString(cursor.getColumnIndex("rfid"));
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
			db.execSQL("delete from local_pandian");
		}
	}
	
	public void deleteByUser(String username)  {
		if(getCountByUser(username)) {
			db.execSQL("delete from local_pandian  where username = ?", new String[]{username});
		}
	}
	
	
	public String getPlanIdFromStock(String rfid) {
		String result = "";
		Cursor cursor = db.rawQuery("select assetCheckplanId from local_stock where rfidnumber = ? ", new String[]{ rfid });
		if(cursor.moveToFirst()) {
			result = cursor.getString(cursor.getColumnIndex("assetCheckplanId"));
		}
		return result;
	}

	public boolean getCount() {
		Cursor cursor = db.rawQuery("select count(*) from local_pandian", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public boolean getCountByUser(String username) {
		Cursor cursor = db.rawQuery("select count(*) from local_pandian  where username = ? ", new String[]{username});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public boolean getCountByRfid(String username,String planid,String rfid) {
		Cursor cursor = db.rawQuery("select count(*) from local_pandian where username = ? and planid = ? and rfid = ?", new String[]{username, planid, rfid});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public boolean getCountByPlanId(String username,String planid) {
		Cursor cursor = db.rawQuery("select count(*) from local_pandian where username = ? and planid = ? ", new String[]{username, planid});
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public List<LocalPandian> getLocalPandian(String username) {
		List<LocalPandian> lps = new ArrayList<LocalPandian>();
		List<String> planIds = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select planid from  local_pandian where username = ? group by planid  ", new String[]{username});
		while(cursor.moveToNext()) {
			planIds.add(cursor.getString(cursor.getColumnIndex("planid")));
		}
		for(String s : planIds) {
			LocalPandian lp = new LocalPandian();
			lp.setPlanId(s);
			StringBuffer sb = new StringBuffer();
			cursor = db.rawQuery("select rfid from local_pandian where username = ? and planid = ?", new String[]{username, s});
			while(cursor.moveToNext()) {
				sb.append(cursor.getString(cursor.getColumnIndex("rfid"))).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			lp.setRfids(sb.toString());
			lps.add(lp);
		}
		return lps;
	}
	
	public void close() {
		db.close();
	}

}
