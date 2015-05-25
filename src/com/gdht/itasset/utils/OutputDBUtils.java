package com.gdht.itasset.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class OutputDBUtils {
	private final int BUFFER_SIZE = 10000;

	public static final String DB_NAME = "offline.db"; // 保存的数据库文件名

	public static final String PACKAGE_NAME = "com.gdht.itasset";// 工程包名

	public static final String DB_PATH = "/data"

	+ Environment.getDataDirectory().getAbsolutePath() + "/"

	+ PACKAGE_NAME + "/databases"; // 在手机里存放数据库的位置
	
	public static final String LOCAL_DB_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/itasset";
	
	private Context context;

	public OutputDBUtils(Context context) {

		this.context = context;

	}

	public void copyDatabase() {
		Log.i("a", "执行数据库导出");
		String dbfile = DB_PATH + "/" + DB_NAME;
		
		File file = new File(DB_PATH);
		if(!file.exists()) file.mkdirs();
		try {

			// 执行数据库导入
			Log.i("a", "local_db = " +LOCAL_DB_PATH);
			InputStream is = new FileInputStream(new File(dbfile));
			File srcFileDir = new File(LOCAL_DB_PATH);
			srcFileDir.mkdir();
			File srcFile = new File(srcFileDir, DB_NAME);
			srcFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(srcFile);

			byte[] buffer = new byte[BUFFER_SIZE];

			int count = 0;

			while ((count = is.read(buffer)) > 0) {

				fos.write(buffer, 0, count);

			}

			fos.close();// 关闭输出流
			is.close();// 关闭输入流
			fos = null;
			is = null;
			file = null;
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
