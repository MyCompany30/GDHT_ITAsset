package com.gdht.itasset.db.service;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.LocalPlanResult;
import com.gdht.itasset.pojo.StockItemNew;

public class LocalPlanResultService {
	SQLiteDatabase db;

	public LocalPlanResultService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	public Long save(List<LocalPlanResult> lists) {
		delete();
		for (LocalPlanResult lpr : lists) {
			db.execSQL(
					"insert into local_planresult (id, title, depts, persons, number, detail, planstate, qdtime, wctime, wcr, yp, wp, pk, py, deptcode, type, yprfids, wprfids, pkrfids, pyrfids) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?)",
					new String[] { lpr.getId(), lpr.getTitle(), lpr.getDepts(),
							lpr.getPersons(), lpr.getNumber(), lpr.getDetail(),
							lpr.getPlanstate(), lpr.getQdtime(),
							lpr.getWctime(), lpr.getWcr(), lpr.getYp(),
							lpr.getWp(), lpr.getPk(), lpr.getPy(),
							lpr.getDeptcode(), lpr.getType(), lpr.getYpRfids(),
							lpr.getWpRfids(), lpr.getPkRfids(),
							lpr.getPyRfids() });
		}
		return getCountNumber();
	}
	
	public LocalPlanResult getLocalPlanResultByPlanId(String planId, String username) {
		LocalPlanResult lpr = null;
		Cursor cursor = db.rawQuery("select * from realnames where username = ?", new String[]{ username });
		String realname = "未知";
		if(cursor.moveToFirst()) {
			realname = cursor.getString(cursor.getColumnIndex("realname"));
		}
		cursor = db.rawQuery("select * from local_planresult where id = ?", new String[]{planId});
		if(cursor.moveToFirst()) {
			lpr = new LocalPlanResult();
			lpr.setId(cursor.getString(cursor.getColumnIndex("id")));
			lpr.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			lpr.setDepts(cursor.getString(cursor.getColumnIndex("depts")));
//			lpr.setPersons(cursor.getString(cursor.getColumnIndex("persons")));
			lpr.setPersons(realname); 
			lpr.setNumber(cursor.getString(cursor.getColumnIndex("number")));
			lpr.setDetail(cursor.getString(cursor.getColumnIndex("detail")));
			lpr.setPlanstate(cursor.getString(cursor.getColumnIndex("planstate")));
			lpr.setQdtime(cursor.getString(cursor.getColumnIndex("qdtime")));
			lpr.setWctime(cursor.getString(cursor.getColumnIndex("wctime")));
			lpr.setWcr(cursor.getString(cursor.getColumnIndex("wcr")));
			lpr.setYp(cursor.getString(cursor.getColumnIndex("yp")));
			lpr.setWp(cursor.getString(cursor.getColumnIndex("wp")));
			lpr.setPy(cursor.getString(cursor.getColumnIndex("py")));
			lpr.setPk(cursor.getString(cursor.getColumnIndex("pk")));
			lpr.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
			lpr.setType(cursor.getString(cursor.getColumnIndex("type")));
			lpr.setYpRfids(cursor.getString(cursor.getColumnIndex("yprfids")));
			lpr.setWpRfids(cursor.getString(cursor.getColumnIndex("wprfids")));
			lpr.setPyRfids(cursor.getString(cursor.getColumnIndex("pyrfids")));
			lpr.setPkRfids(cursor.getString(cursor.getColumnIndex("pkrfids")));
		}
		return lpr;
	}
	

	public Long getCountNumber() {
		Cursor cursor = db.rawQuery("select count(*) from local_planresult",
				null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count;
	}
	
	public void delete() {
		if (getCount()) {
			db.execSQL("delete from local_planresult");
		}
	}

	public boolean getCount() {
		Cursor cursor = db.rawQuery("select count(*) from local_planresult",
				null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}

	public void close() {
		db.close();
	}
}
