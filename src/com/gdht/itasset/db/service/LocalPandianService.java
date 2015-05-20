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
		for (String s : keys) {
			String ss = s.replaceAll("'", "");
			String stockPlanId = getPlanIdFromStock(ss);
			// Log.i("a", "stockPlanId = " + stockPlanId + " planId = " +
			// planid);
			if (stockPlanId.equals(planid)) {
				if (getCountByRfid(username, planid, s)) {

				} else {
					if (!"".equals(ss)) {
						db.execSQL(
								"insert into local_pandian(username, planid, rfid) values (?, ?, ?)",
								new String[] { username, planid, s });
					}
				}
			}

		}
		Cursor cursor = db
				.rawQuery(
						"select rfid from local_pandian where username = ? and planid = ?",
						new String[] { username, planid });
		Cursor ypC = db.rawQuery(
				"select yprfids from local_planresult where id = ?",
				new String[] { planid });
		Cursor wpC = db.rawQuery(
				"select wprfids from local_planresult where id = ?",
				new String[] { planid });
		Cursor pkC = db.rawQuery(
				"select pkrfids from local_planresult where id = ?",
				new String[] { planid });
		String ypRfids = "";
		if (ypC.moveToFirst()) {
			ypRfids = ypC.getString(ypC.getColumnIndex("yprfids"));
		}
		String wpRfids = "";
		if (wpC.moveToFirst()) {
			wpRfids = wpC.getString(wpC.getColumnIndex("wprfids"));
		}
		String pkRfids = "";
		if (pkC.moveToFirst()) {
			pkRfids = pkC.getString(pkC.getColumnIndex("pkrfids"));
		}
		while (cursor.moveToNext()) {
			// lists.add(cursor.getString(cursor.getColumnIndex("rfid")));

			String rfidTemp = cursor.getString(cursor.getColumnIndex("rfid"));
			rfidTemp = rfidTemp.replaceAll("'", "\"");

				if (ypRfids.equals("")) {
					ypRfids = rfidTemp;
				} else {
					if (!ypRfids.contains(rfidTemp)) {
						ypRfids = ypRfids + "," + rfidTemp;
					}
				}
				if (wpRfids.contains(rfidTemp)) {
					wpRfids = wpRfids.replace(rfidTemp, "");
					wpRfids.replaceAll(",,", ",");
					if (wpRfids.startsWith(",")) {
						wpRfids = wpRfids.substring(1, wpRfids.length());
					}
					if (wpRfids.endsWith(",")) {
						wpRfids = wpRfids.substring(0, wpRfids.length() - 2);
					}
				}
				if (pkRfids.contains(rfidTemp)) {
					pkRfids = pkRfids.replace(rfidTemp, "");
					pkRfids.replaceAll(",,", ",");
					if (pkRfids.startsWith(",")) {
						pkRfids = pkRfids.substring(1, pkRfids.length());
					}
					if (pkRfids.endsWith(",")) {
						pkRfids = pkRfids.substring(0, pkRfids.length() - 2);
					}
				}
				
//				Log.i("a", "rfidTemp = " + rfidTemp);
		}

		int ypNum = 0;
		if(!ypRfids.equals("")) {
			String[] ypArrays = ypRfids.split(",");
			ypNum = ypArrays.length;
			
		}
		int wpNum = 0;
		if(!wpRfids.equals("")) {
			String[] wpArrays = wpRfids.split(",");
			wpNum = wpArrays.length;
		}
		int pkNum = 0;
		if(!pkRfids.equals("")) {
			String[] pkArrays = wpRfids.split(",");
			pkNum = pkArrays.length;
		}
		
		Log.i("a", "ypRfids = " + ypRfids + " size = " + ypNum);
		Log.i("a", "wpRfids = " + wpRfids + " size = " + wpNum);
		Log.i("a", "pkRfids = " + pkRfids + " size = " + pkNum);
		db.execSQL(
				"update local_planresult set yp = ?, wp = ?, pk = ?, yprfids = ?, wprfids = ?, pkrfids = ? where id = ?",
				new String[] { String.valueOf(ypNum), String.valueOf(wpNum),String.valueOf(pkNum),
						ypRfids, wpRfids,pkRfids, planid });

		// 跟新已盘未盘rfids

		return lists;
	}

	public ArrayList<String> getRifdsByPlanId(String planid, String username) {
		ArrayList<String> rfids = null;
		Cursor cursor = db
				.rawQuery(
						"select rfids from local_pandian where planid = ? and username = ?",
						new String[] { planid, username });
		String rfidStr = "";
		if (cursor.moveToFirst()) {
			rfidStr = cursor.getString(cursor.getColumnIndex("rfid"));
			String[] str = rfidStr.split(",");
			if (str.length > 0) {
				rfids = new ArrayList<String>();
				for (String s : str) {
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

	public void deleteByUser(String username) {
		if (getCountByUser(username)) {
			db.execSQL("delete from local_pandian  where username = ?",
					new String[] { username });
		}
	}

	public String getPlanIdFromStock(String rfid) {
		String result = "";
		Cursor cursor = db
				.rawQuery(
						"select assetCheckplanId from local_stock where rfidnumber = ? ",
						new String[] { rfid });
		if (cursor.moveToFirst()) {
			result = cursor
					.getString(cursor.getColumnIndex("assetCheckplanId"));
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
		Cursor cursor = db.rawQuery(
				"select count(*) from local_pandian  where username = ? ",
				new String[] { username });
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}

	public boolean getCountByRfid(String username, String planid, String rfid) {
		Cursor cursor = db
				.rawQuery(
						"select count(*) from local_pandian where username = ? and planid = ? and rfid = ?",
						new String[] { username, planid, rfid });
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}

	public boolean getCountByPlanId(String username, String planid) {
		Cursor cursor = db
				.rawQuery(
						"select count(*) from local_pandian where username = ? and planid = ? ",
						new String[] { username, planid });
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}

	public List<LocalPandian> getLocalPandian(String username) {
		List<LocalPandian> lps = new ArrayList<LocalPandian>();
		List<String> planIds = new ArrayList<String>();
		Cursor cursor = db
				.rawQuery(
						"select planid from  local_pandian where username = ? group by planid  ",
						new String[] { username });
		while (cursor.moveToNext()) {
			planIds.add(cursor.getString(cursor.getColumnIndex("planid")));
		}
		for (String s : planIds) {
			LocalPandian lp = new LocalPandian();
			lp.setPlanId(s);
			StringBuffer sb = new StringBuffer();
			cursor = db
					.rawQuery(
							"select rfid from local_pandian where username = ? and planid = ?",
							new String[] { username, s });
			while (cursor.moveToNext()) {
				sb.append(cursor.getString(cursor.getColumnIndex("rfid")))
						.append(",");
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
