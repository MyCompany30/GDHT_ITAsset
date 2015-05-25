package com.gdht.itasset.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GDHTOffLineDataOpenHelper extends SQLiteOpenHelper {

	
	
	public GDHTOffLineDataOpenHelper(Context context) {
		super(context, "offline.db", null, 1);
	}
	
	public GDHTOffLineDataOpenHelper(Context context, int versionId) {
		super(context, "offline.db", null, versionId);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists local_pandian("
				+ "idx integer primary key autoincrement, username text, planid text, rfid text,assetid text, type text"
				+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}








