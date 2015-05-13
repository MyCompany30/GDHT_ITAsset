package com.gdht.itasset.db.service;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.DeletedContacts;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.StockItemNew;

public class LocalStockService {
	SQLiteDatabase db;

	public LocalStockService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	public void save(List<StockItemNew> lists) {
		delete();
		for (StockItemNew si : lists) {
			db.execSQL(
					"insert into local_stock (assetChecklistId, assetCheckplanId,assetInfoId,classify, type,rfidnumber,barnumber,qrnumber,brand,model,usetype,checkstate,dept,detil,id,keeper,name,office,warehouseArea,goodsShelves,registerdate,registrant) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?)",
					new String[] { si.getAssetChecklistId(), si.getAssetCheckplanId(), si.getAssetInfoId(), si.getClassify(), si.getType(), si.getRfidnumber(), si.getBarnumber(), si.getQrnumber(), si.getBrand(), si.getModel(), si.getUsetype(), si.getCheckstate(), si.getDept(), si.getDetil(), si.getId(), si.getKeeper(), si.getName(), si.getOffice(),si.getWarehouseArea(), si.getGoodsShelves(), si.getRegisterdate(), si.getRegistrant() });
		}
	}
	
	public void delete() {
		if(getCount()) {
			db.execSQL("delete from local_stock");
		}
	}
	
	public boolean getCount() {
		Cursor cursor = db.rawQuery("select count(*) from local_stock", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count > 0 ? true : false;
	}
	
	public void close() {
		db.close();
	}
}
