package com.gdht.itasset;

import com.gdht.itasset.asynctask.LoginAsyncTask;
import com.gdht.itasset.utils.AppSharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private EditText ip = null;
	private CheckBox chkBox = null;
	private ImageView logo = null;
	private SharedPreferences loginSettings = null;
	public static String ipStr = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
		findViews();
		setOnClicks();
		
	}

	private void setOnClicks() {
		//登录按钮
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name,pwd;
				name = userName.getText().toString().trim();
				pwd = userPwd.getText().toString().trim();
				if(name.equals("")||pwd.equals("")){
					Toast.makeText(getApplicationContext(), "用户名或密码为空！", Toast.LENGTH_LONG).show();
					return;
				}
				/*
				if(TextUtils.isEmpty(ip.getText().toString())) {
					ipStr = new AppSharedPreferences(MainActivity.this, "gdht").getIP();
				}else {
					ipStr = "http://" + ip.getText().toString().trim();
				}
				*/
				if(ipStr.equals("")) {
					ipStr = new AppSharedPreferences(MainActivity.this, "gdht").getIP();
				}
				new LoginAsyncTask(MainActivity.this, ipStr).execute(name,pwd, ipStr);
				loginSettings = MainActivity.this.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
				//记住登录名
				Editor editor = loginSettings.edit();
				editor.putString(SETTING_NAME, name);
				//记住密码
				if(chkBox.isChecked()){
					editor.putString(SETTING_PWD, pwd);
				}else{
					editor.remove(SETTING_PWD);
				}
				editor.commit();
			}
		});
	
	}

	private void findViews() {
		loginBtn = (TextView)this.findViewById(R.id.loginBtn);
		userName = (EditText)this.findViewById(R.id.userName);
		userPwd = (EditText)this.findViewById(R.id.userPwd);
		ip = (EditText) this.findViewById(R.id.ip);
		chkBox = (CheckBox)this.findViewById(R.id.savePwd);
		logo = (ImageView)this.findViewById(R.id.logo);
		DisplayMetrics dm = new DisplayMetrics(); 
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		logo.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.widthPixels/2));
		//检测是否记住密码
		loginSettings = this.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
		String name = loginSettings.getString(SETTING_NAME,"");
		String pwd = loginSettings.getString(SETTING_PWD, "");
		userName.setText(name);
		userPwd.setText(pwd);
		if(!pwd.equals("")){
			chkBox.setChecked(true);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			//显示退出对话框
			new AlertDialog.Builder(this)
					.setTitle("确定要退出程序？")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,	int which) {
									dialog.dismiss();
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,	int which) {
									// 点击“返回”后的操作,这里不设置没有任何操作
									dialog.dismiss();
								}
							}).show();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
