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
