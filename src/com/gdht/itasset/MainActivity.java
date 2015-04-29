package com.gdht.itasset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.gdht.itasset.asynctask.LoginAsyncTask;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.version.VersionServiceIndex;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String SETTINGS = "GDHT_ITASSET_SETTINGS";
	public static final String SETTING_NAME = "LOGIN_NAME";
	public static final String SETTING_PWD = "LOGIN_PWD";
	public static final String SETTING_AUTOLOGIN = "AUTO_LOGIN";
	private TextView loginBtn = null;
	private EditText userName = null;
	private EditText userPwd = null;
	private CheckBox chkBox = null;
	private ImageView logo = null;
	private ImageView optionImg = null;
	private TextView optionTv = null;
	private SharedPreferences loginSettings = null;
	public static String ipStr = "";
	private AlertDialog downloadAd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		findViews();
		setOnClicks();
		downloadAd = new AlertDialog.Builder(this)
		.setTitle("版本更新")
		.setMessage("新版本已发布，是否进行更新!")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "开始下载新版本!", 0)
						.show();
//				downloadAd.dismiss();
				
				Beginning();
				downloadAd.dismiss();
				
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				downloadAd.dismiss();
			}
		}).create();
//		new EnquireAppVersionAsyncTask().execute("");
	}

	private void setOnClicks() {
		// 登录按钮
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name, pwd;
				name = userName.getText().toString().trim();
				pwd = userPwd.getText().toString().trim();
				if (name.equals("") || pwd.equals("")) {
					Toast.makeText(getApplicationContext(), "用户名或密码为空！",
							Toast.LENGTH_LONG).show();
					return;
				}
				/*
				 * if(TextUtils.isEmpty(ip.getText().toString())) { ipStr = new
				 * AppSharedPreferences(MainActivity.this, "gdht").getIP();
				 * }else { ipStr = "http://" + ip.getText().toString().trim(); }
				 */
				if (ipStr.equals("")) {
					ipStr = new AppSharedPreferences(MainActivity.this, "gdht")
							.getIP();
				}
				new LoginAsyncTask(MainActivity.this, ipStr).execute(name, pwd,
						ipStr);
				loginSettings = MainActivity.this.getSharedPreferences(
						SETTINGS, Context.MODE_PRIVATE);
				// 记住登录名
				Editor editor = loginSettings.edit();
				editor.putString(SETTING_NAME, name);
				// 记住密码
				if (chkBox.isChecked()) {
					editor.putString(SETTING_PWD, pwd);
				} else {
					editor.remove(SETTING_PWD);
				}
				editor.commit();
			}
		});

	}

	private void findViews() {
		loginBtn = (TextView) this.findViewById(R.id.loginBtn);
		userName = (EditText) this.findViewById(R.id.userName);
		userPwd = (EditText) this.findViewById(R.id.userPwd);
		chkBox = (CheckBox) this.findViewById(R.id.savePwd);
		logo = (ImageView) this.findViewById(R.id.logo);
		optionImg = (ImageView) this.findViewById(R.id.optionImg);
		optionTv = (TextView) this.findViewById(R.id.optionTv);
		optionImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, OptionActivity.class);
				startActivity(intent);
			}
		});
		optionTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, OptionActivity.class);
				startActivity(intent);
			}
		});
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		logo.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels,
//				dm.widthPixels / 2));
		// 检测是否记住密码
		loginSettings = this.getSharedPreferences(SETTINGS,
				Context.MODE_PRIVATE);
		String name = loginSettings.getString(SETTING_NAME, "");
		String pwd = loginSettings.getString(SETTING_PWD, "");
		userName.setText(name);
		userPwd.setText(pwd);
		if (!pwd.equals("")) {
			chkBox.setChecked(true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 显示退出对话框
			new AlertDialog.Builder(this)
					.setTitle("确定要退出程序？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 点击“返回”后的操作,这里不设置没有任何操作
									dialog.dismiss();
								}
							}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	private class EnquireAppVersionAsyncTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
//			return CarHttpUtils.EnquireAppVersion(URLAddress.BASE_URL,
//					URLMethodNames.EnquireAppVersion,
//					userAsp.getCurrentLoginer(), "Android");
			return "2.0";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("0".equals(result)) {
//				Toast.makeText(TheIndexActivity.this, "获取版本号失败!", 0).show();
			} else {
				// Log.i("a", "版本 = " + result);
				// Toast.makeText(SettingActivity.this, "新版本号 : " + result,
				// 0).show();
				try {
					String version = getVersionName();
					// Toast.makeText(SettingActivity.this, "当前应用版本号 : " +
					// version + " 获取到的版本号 : " + result, 0).show();

					if (result.equals(version)) {
						// Toast.makeText(TheIndexActivity.this, "已经是最新版本!", 0)
						// .show();
					} else {
						downloadAd.show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;
	}
	
	private ProgressBar pb;private TextView tv;
	public void Beginning(){
		LinearLayout ll = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(
				R.layout.layout_loadapk, null);
		pb = (ProgressBar) ll.findViewById(R.id.down_pb);
		tv = (TextView) ll.findViewById(R.id.tv);
		Builder builder = new Builder(MainActivity.this);
		builder.setView(ll);builder.setTitle("版本更新进度提示");
		builder.setNegativeButton("后台下载",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent=new Intent(MainActivity.this, VersionServiceIndex.class);  
						startService(intent);
						dialog.dismiss();
					}
				}).setCancelable(false);
		
		builder.show();
		new Thread() {
			public void run() {
//				loadFile("http://www.ecloudcar.com:85/Manage/Apk/ecar.apk");
				loadFile("URLAddress.LOADFILE_URL");
			}
		}.start();
	}
	
	public void loadFile(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			
			HttpEntity entity = response.getEntity();
			float length = entity.getContentLength();

			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File file = new File(Environment.getExternalStorageDirectory(),
						"ecar.apk");
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				float count = 0;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					sendMsg(1,(int) (count*100/length));
				}
			}
			sendMsg(2,0);
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			sendMsg(-1,0);
		}
	}
	
	public static int loading_process;
	private void sendMsg(int flag,int c) {
		Message msg = new Message();
		msg.what = flag;msg.arg1=c;
		handler.sendMessage(msg);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 1:
					pb.setProgress(msg.arg1);
					loading_process = msg.arg1;
					tv.setText("已为您加载了：" + loading_process + "%");
					break;
				case 2:
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "ecar.apk")),
							"application/vnd.android.package-archive");
					startActivity(intent);
					break;
				case -1:
					String error = msg.getData().getString("error");
					loading_process = 1;
					Toast.makeText(MainActivity.this, " " + loading_process, 1).show();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	};
	

}
