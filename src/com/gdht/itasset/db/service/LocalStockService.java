package com.gdht.itasset.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.storage.StorageManager;
import android.provider.ContactsContract.DeletedContacts;

import com.gdht.itasset.db.GDHTOpenHelper;
import com.gdht.itasset.pojo.StockItemNew;
import com.senter.co;

public class LocalStockService {
	SQLiteDatabase db;

	public LocalStockService(Context context) {
		GDHTOpenHelper helper = new GDHTOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	public Long save(List<StockItemNew> lists) {
		delete();
		for (StockItemNew si : lists) {
			db.execSQL(
					"insert into local_stock (assetChecklistId, assetCheckplanId,assetInfoId,classify, type,rfidnumber,barnumber,qrnumber,brand,model,usetype,checkstate,dept,detil,id,keeper,name,office,warehouseArea,goodsShelves,registerdate,registrant) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?)",
					new String[] { si.getAssetChecklistId(), si.getAssetCheckplanId(), si.getAssetInfoId(), si.getClassify(), si.getType(), si.getRfidnumber(), si.getBarnumber(), si.getQrnumber(), si.getBrand(), si.getModel(), si.getUsetype(), si.getCheckstate(), si.getDept(), si.getDetil(), si.getId(), si.getKeeper(), si.getName(), si.getOffice(),si.getWarehouseArea(), si.getGoodsShelves(), si.getRegisterdate(), si.getRegistrant() });
		}
		return getCountNumber();
	}
	
	public Long getCountNumber() {
		Cursor cursor = db.rawQuery("select count(*) from local_stock", null);
		cursor.moveToNext();
		Long count = cursor.getLong(0);
		return count;
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
	
	public StockItemNew queryStockDetail(String rfid){
		StockItemNew stockItemNew = null;
		Cursor cursor = db
				.rawQuery("select assetChecklistId, assetCheckplanId, assetInfoId, classify, type, rfidnumber," +
						"barnumber, qrnumber, brand, model, usetype, checkstate, dept, detil, id, keeper, " +
						"name, office, warehouseArea, goodsShelves, registerdate, registrant from local_stock where rfidnumber = ?", new String[] { rfid });
		if(cursor.moveToNext()){
			stockItemNew = new StockItemNew();
			stockItemNew.setAssetChecklistId(cursor.getString(cursor.getColumnIndex("assetChecklistId")));
			stockItemNew.setAssetCheckplanId(cursor.getString(cursor.getColumnIndex("assetCheckplanId")));
			stockItemNew.setAssetInfoId(cursor.getString(cursor.getColumnIndex("assetInfoId")));
			stockItemNew.setClassify(cursor.getString(cursor.getColumnIndex("classify")));
			stockItemNew.setType(cursor.getString(cursor.getColumnIndex("type")));
			stockItemNew.setRfidnumber(cursor.getString(cursor.getColumnIndex("rfidnumber")));
			stockItemNew.setBarnumber(cursor.getString(cursor.getColumnIndex("barnumber")));
			stockItemNew.setQrnumber(cursor.getString(cursor.getColumnIndex("qrnumber")));
			stockItemNew.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
			stockItemNew.setModel(cursor.getString(cursor.getColumnIndex("model")));
			stockItemNew.setUsetype(cursor.getString(cursor.getColumnIndex("usetype")));
			stockItemNew.setCheckstate(cursor.getString(cursor.getColumnIndex("checkstate")));
			stockItemNew.setDept(cursor.getString(cursor.getColumnIndex("dept")));
			stockItemNew.setDetil(cursor.getString(cursor.getColumnIndex("detil")));
			stockItemNew.setId(cursor.getString(cursor.getColumnIndex("id")));
			stockItemNew.setKeeper(cursor.getString(cursor.getColumnIndex("keeper")));
			stockItemNew.setName(cursor.getString(cursor.getColumnIndex("name")));
			stockItemNew.setOffice(cursor.getString(cursor.getColumnIndex("office")));
			stockItemNew.setWarehouseArea(cursor.getString(cursor.getColumnIndex("warehouseArea")));
			stockItemNew.setGoodsShelves(cursor.getString(cursor.getColumnIndex("goodsShelves")));
			stockItemNew.setRegisterdate(cursor.getString(cursor.getColumnIndex("registerdate")));
			stockItemNew.setRegistrant(cursor.getString(cursor.getColumnIndex("registrant")));
		}
		return stockItemNew;
	}
	
	public void close() {
		db.close();
	}
	
	public int updateCheckState(ArrayList<String> rfids, String stateCode, String planId){
		int count = 0;
		ContentValues cv = new ContentValues();
		cv.put("checkstate", stateCode);
		for(String rfid : rfids){
			int result = 0;
			result = db.update("local_stock", cv, "rfidnumber = ?", new String[]{ rfid });
			count += result;
			if(result > 0){
				//成功，更新local_planresult表数据
				//更新未盘rfid字段
				Cursor cursor = db.rawQuery("select wprfids from local_planresult where id = ?", new String[]{ planId });
				if(cursor.moveToFirst()){
					String str = cursor.getString(cursor.getColumnIndex("wprfids"));
					String rfidStr = "";
					if(str.contains(rfid)){
						String str1 = str.replace("\"", "");
						str1 = str1.replace(rfid, "");
						String [] s = str1.split(",");
						for(int i = 0; i < s.length; i++){
							if(i==0 && s[0] != ""){
								rfidStr += "\"";
							}
							if(s[i].equals("")){
								continue;
							}
							rfidStr += s[i];
							if(i == s.length-1){
								rfidStr += "\"";
								continue;
							}
							rfidStr +="\",\"";
						}
					}
					int wpCount = rfidStr.split("\",\"")[0].equals("")?0:rfidStr.split("\",\"").length;
					ContentValues cv1 = new ContentValues();
					cv1.put("wprfids", rfidStr);
					cv1.put("wp", wpCount);
					db.update("local_planresult", cv1, "id = ?", new String[]{ planId });
				}
				//更新盘亏rfid字段
				Cursor cursor1 = db.rawQuery("select pkrfids from local_planresult where id = ?", new String[]{ planId });
				String rfidStrPk = "";
				if(cursor1.moveToFirst()){
					String str = cursor1.getString(cursor1.getColumnIndex("pkrfids"));
					rfidStrPk = str + ",\""+ rfid +"\"";
				}
				ContentValues cv2 = new ContentValues();
				cv2.put("pk", rfidStrPk.split("\",\"").length);
				cv2.put("pkrfids", rfidStrPk);
				db.update("local_planresult", cv2, "id = ?", new String[]{ planId });
				
			}
		}
		
		return count;
	}
	
}
