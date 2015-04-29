package com.gdht.itasset.version;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class VersionUpdateUtils {

	public static int getVerCode(Context _context,String _package) {
		int verCode = -1;
		try {
			verCode = _context.getPackageManager().getPackageInfo(
					_package, 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}
	
}
