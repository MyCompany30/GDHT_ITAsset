package com.gdht.itasset.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GDHTCommonOpenHelper extends SQLiteOpenHelper {

	
	
	public GDHTCommonOpenHelper(Context context) {
		super(context, "common.db", null, 1);
	}
	
	public GDHTCommonOpenHelper(Context context, int versionId) {
		super(context, "common.db", null, versionId);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists scan_check_rfids("
				+ "idx integer primary key autoincrement, rfid text, user text"
				+ ")");
		db.execSQL("create table if not exists loginLog("
				+ "idx integer primary key autoincrement, login_name text, login_time text"
				+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("ALTER TABLE dbversion ADD COLUMN marktes integer");
	}

}








