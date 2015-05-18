package com.gdht.itasset.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
					new String[] { si.getId(), si.getTitle(), si.getType(),
							si.getDepts(), String.valueOf(si.getNumber()),
							si.getPlanstate(), si.getDetail(),
							si.getDeptcode(), si.getQdtime(), si.getWctime(),
							si.getPersons() });
		}
		return getCountNumber();
	}

	// select persons from local_plan t where persons like 'asset,%' or persons
	// like '%,asset,%' or persons like 'asset' or persons like '%,assett'
	/**
	 * 	private String id;//计划id
	private String title;//计划名称
	private String type;//计划部门类型(1.仓库2.在运)
	private String depts;//仓库(部门)
	private int number;//计划盘点数
	private String planstate;//计划状态(0已完成；1执行中)
	private String detail;
	private String deptcode;//仓库部门的code
	private String qdtime;//启动日期
	private String wctime;//完成日期
	private String persons;  //所属人
	 * @param username
	 * @return
	 */
	public ArrayList<PlanInfo> getPlanInfoByUsername(String username) {
		ArrayList<PlanInfo> pis = new ArrayList<PlanInfo>();
		PlanInfo pi = null;
		String sql = "select * from local_plan where persons like '" + username +",%' or persons like '%," + username + 
				",%' or persons like '" + username + "' or persons like '%," + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext()) {
			pi = new PlanInfo();
			pi.setId(cursor.getString(cursor.getColumnIndex("id")));
			pi.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			pi.setType(cursor.getString(cursor.getColumnIndex("type")));
			pi.setDepts(cursor.getString(cursor.getColumnIndex("depts")));
			pi.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
			pi.setPlanstate(cursor.getString(cursor.getColumnIndex("planstate")));
			pi.setDetail(cursor.getString(cursor.getColumnIndex("detail")));
			pi.setDeptcode(cursor.getString(cursor.getColumnIndex("deptcode")));
			pi.setQdtime(cursor.getString(cursor.getColumnIndex("qdtime")));
			pi.setWctime(cursor.getString(cursor.getColumnIndex("wctime")));
			pi.setPersons(cursor.getString(cursor.getColumnIndex("persons")));
			pis.add(pi);
		}
		return pis;
	}

	public Long getCountNumber() {
		Cursor cursor = db.rawQuery("select count(*) from local_plan", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count;
	}

	public void delete() {
		if (getCount()) {
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
