package com.gdht.itasset.receiver;

import com.gdht.itasset.BlankActivity;
import com.gdht.itasset.utils.GlobalParams;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	String packnameString = null;
	private boolean flag = true;
	@Override
	public void onReceive(Context context, Intent intent) {
		packnameString = context.getPackageName();
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (flag && !mobNetInfo.isConnected() && !wifiNetInfo.isConnected() && GlobalParams.LOGIN_TYPE == 1) {
			// unconnect network
//			Toast.makeText(context, "unconnect network", Toast.LENGTH_LONG).show();
//			initAd(context);
			flag = false;
			Intent intent2 = new Intent(context, BlankActivity.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
			
		} else {
			// connect network
//			Toast.makeText(context, "connect network", Toast.LENGTH_LONG).show();
		}
	}
	
}