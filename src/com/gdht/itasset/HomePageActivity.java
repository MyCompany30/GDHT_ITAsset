package com.gdht.itasset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.receiver.ConnectionChangeReceiver;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.utils.GlobalParams;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageActivity extends Activity {

	private ImageView saomiaoBtn = null;
	private ImageView pandianBtn = null;
	private ImageView shezhiBtn = null;
	public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private AlertDialog downloadAd;
	public static String ipStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		registerDateTransReceiver();
		findViews();
		downloadAd = new AlertDialog.Builder(this)
				.setTitle("版本更新")
				.setMessage("新版本已发布，是否进行更新!")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(HomePageActivity.this, "开始下载新版本!", 0)
								.show();
						// downloadAd.dismiss();

						Beginning();
						downloadAd.dismiss();

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						downloadAd.dismiss();
					}
				}).create();
		if(GlobalParams.LOGIN_TYPE == 1) {
			new EnquireAppVersionAsyncTask().execute("");
		}
	}

	private void findViews() {
		saomiaoBtn = (ImageView) findViewById(R.id.saomiao);
		pandianBtn = (ImageView) findViewById(R.id.pandian);
		shezhiBtn = (ImageView) findViewById(R.id.shezhi);

	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.saomiao:
			startActivity(new Intent(this, ScanActivityNew.class));
			break;
		case R.id.pandian:
			Intent intent = new Intent();
			intent.setClass(HomePageActivity.this, PlanListActivity.class);
			if (getIntent().hasExtra("name")) {
				intent.putExtra("name", getIntent().getStringExtra("name"));
			}
			if (getIntent().hasExtra("planList")) {
				intent.putExtra("planList",
						getIntent().getStringExtra("planList"));
			}
			HomePageActivity.this.startActivity(intent);
			break;
		case R.id.shezhi:
			Intent shezhiIntent = new Intent().setClass(HomePageActivity.this,
					OptionActivity.class);
			shezhiIntent.putExtra("model", "1");
			startActivity(shezhiIntent);
			break;
		}
	}

	ConnectionChangeReceiver ccr = null;

	private void registerDateTransReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(CONNECTIVITY_CHANGE_ACTION);
		filter.setPriority(1000);
		ccr = new ConnectionChangeReceiver();
		registerReceiver(ccr, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(ccr);
	}

	private class EnquireAppVersionAsyncTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			return new HttpClientUtil(HomePageActivity.this).lastAppVersion(
					HomePageActivity.this, "itasset_mcheck");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("0".equals(result)) {
				Toast.makeText(HomePageActivity.this, "获取版本号失败!", 0).show();
			} else {
				// Log.i("a", "版本 = " + result);
				// Toast.makeText(SettingActivity.this, "新版本号 : " + result,
				// 0).show();
				try {
					String version = getVersionName();
					result = result.replaceAll("\"", "");
//					Toast.makeText(HomePageActivity.this,
//							"当前应用版本号 : " + version + " 获取到的版本号 : " + result, 0)
//							.show();

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

	private ProgressBar pb;
	private TextView tv;

	public void Beginning() {
		LinearLayout ll = (LinearLayout) LayoutInflater.from(
				HomePageActivity.this).inflate(R.layout.layout_loadapk, null);
		pb = (ProgressBar) ll.findViewById(R.id.down_pb);
		tv = (TextView) ll.findViewById(R.id.tv);
		Builder builder = new Builder(HomePageActivity.this);
		builder.setView(ll);
		builder.setTitle("版本更新进度提示");
		builder.setNegativeButton("后台下载",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(HomePageActivity.this,
								VersionServiceIndex.class);
						startService(intent);
						dialog.dismiss();
					}
				}).setCancelable(false);

		builder.show();
		new Thread() {
			public void run() {
				// loadFile("http://www.ecloudcar.com:85/Manage/Apk/ecar.apk");
				if (ipStr.equals("")) {
					ipStr = new AppSharedPreferences(HomePageActivity.this,
							"gdht").getIP();
				}
				String downLoadUrl = ipStr + "/temp/app/itasset_mcheck.apk";
				Log.i("a", "downLoadUrl = " + downLoadUrl);
				loadFile(downLoadUrl);
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
					sendMsg(1, (int) (count * 100 / length));
				}
			}
			sendMsg(2, 0);
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			sendMsg(-1, 0);
		}
	}

	public static int loading_process;

	private void sendMsg(int flag, int c) {
		Message msg = new Message();
		msg.what = flag;
		msg.arg1 = c;
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
					Toast.makeText(HomePageActivity.this,
							" " + loading_process, 1).show();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
}
