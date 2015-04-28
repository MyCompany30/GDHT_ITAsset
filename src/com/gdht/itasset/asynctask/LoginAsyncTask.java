package com.gdht.itasset.asynctask;


import java.util.ArrayList;

import com.gdht.itasset.MainActivity;
import com.gdht.itasset.PlanActivity;
import com.gdht.itasset.R;
import com.gdht.itasset.adapter.PlanListAdapter;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.PlanInfo;
import com.gdht.itasset.utils.AppSharedPreferences;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginAsyncTask extends AsyncTask<String, String, String> {
	private MainActivity activity;
	private WaitingDialog dialog;
	private ListAdapter listAdapter = null;
	private ListView planListView = null;
	private String name = null;
	private String pwd = null;
	private ArrayList<PlanInfo> dataList = new ArrayList<PlanInfo>();
	private String ipStr;
	public LoginAsyncTask(MainActivity activity, String _ipStr) {
		this.activity = activity;
		this.ipStr = _ipStr;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new WaitingDialog(activity,R.style.TRANSDIALOG);
		dialog.show();
	}
	@Override
	protected String doInBackground(String... params) {
		String loginFlag = null;
		name = params[0];
		pwd = params[1];
		//获取设备串号、提交登录
		TelephonyManager tm = (TelephonyManager) this.activity.getSystemService(Context.TELEPHONY_SERVICE);
		loginFlag = new HttpClientUtil(activity).login(activity,name,pwd,tm.getDeviceId(),"IT资产移动巡检", params[2]);
		//登录成功获取查询盘点计划中指定的盘点人是自己的盘点计划
		if(loginFlag!=null && loginFlag.equals("登录成功")){
			
			dataList = new HttpClientUtil(activity).getPlans(activity, name);
			
			listAdapter = new PlanListAdapter(dataList, activity);
		}
		return loginFlag;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		//登陆成功进入主界面，失败返回登录界面
		dialog.dismiss();
		GlobalParams.username = name;
		if(result!=null){
			if(result.equals("登录成功")){
				//登录成功
				GlobalParams.isLogin = true;
				activity.setContentView(R.layout.plan_select_view);
				initPlanView();
				//new AppSharedPreferences(activity, "gdht").saveIP(ipStr);
			}else if(result.equals("用户名或密码错误")){
				Toast.makeText(activity, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}else if(result.equals("没有权限")){
				Toast.makeText(activity, "没有权限", Toast.LENGTH_SHORT).show();
			}else if(result.equals("网络异常")||result.equals("未知错误")){
				Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("planId","");
				intent.putExtra("operator",name);
				intent.setClass(activity, PlanActivity.class);
				activity.startActivity(intent);
				Toast.makeText(activity, "离线登录", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
			}
		}
		Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	private void initPlanView() {
		planListView = (ListView)activity.findViewById(R.id.plan_listView);
		View back = (View) activity.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//显示退出对话框
				new AlertDialog.Builder(activity)
						.setTitle("确定要退出程序？")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,	int which) {
										dialog.dismiss();
										activity.finish();
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
			
			}
		});
		planListView.setAdapter(listAdapter);
		planListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(activity, PlanActivity.class);
				intent.putExtra("planId", dataList.get(arg2).getId());
				intent.putExtra("operator", name);
				activity.startActivity(intent);
			}
			
		});
	}
}
