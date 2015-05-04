package com.gdht.itasset;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.adapter.CommenListAdapter;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.utils.GlobalParams;

public class MainScanActivity extends Activity {
	private Intent intent;
	private ArrayList<PlanAssetInfo> planAssetArrayList = new ArrayList<PlanAssetInfo>();
	private String bumen = "";
	private String zhuangtai = "";
	private String bangongsi = "";
	private String quyu = "";
	private String huojia = "";
	private CommenListAdapter bumenAdapter;
	private PopupWindow pw;
	private ListView pwListView;
	private RelativeLayout parentView;
	private View pwContentView;
	private TextView tv1,tv2,tv3;
	private RelativeLayout selectALayout, selectBLayout, selectCLayout,
			selectDLayout;
	private TextView selectAText,selectBText,selectCText,selectDText;
	private ImageView ivA, ivB, ivC, ivD;
	private String checkStr;
	private LinearLayout dbLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_scan_main);
		tv1 = (TextView) this.findViewById(R.id.tv1);
		tv2 = (TextView) this.findViewById(R.id.tv2);
		tv3 = (TextView) this.findViewById(R.id.tv3);
		refreshSelectTitle();
		dbLayout = (LinearLayout) this.findViewById(R.id.localDB);
		checkStr = this.getIntent().getStringExtra("check");
		if(checkStr != null && "check".equals(checkStr)){
			dbLayout.setVisibility(View.GONE);
		}
		
	}
	
	private void initPopupWindow() {
		int height = this.getResources().getDisplayMetrics().heightPixels / 2;
		LayoutInflater inflater = LayoutInflater.from(this);
		pwContentView = inflater.inflate(R.layout.popupwindow1, null);
		pwListView = (ListView) pwContentView.findViewById(R.id.listView);
		pw = new PopupWindow(pwContentView, LayoutParams.MATCH_PARENT, height);
		parentView = (RelativeLayout) this.findViewById(R.id.topBar);
		selectALayout = (RelativeLayout) pwContentView
				.findViewById(R.id.selectALayout);
		selectBLayout = (RelativeLayout) pwContentView
				.findViewById(R.id.selectBLayout);
		selectCLayout = (RelativeLayout) pwContentView
				.findViewById(R.id.selectCLayout);
		selectDLayout = (RelativeLayout) pwContentView
				.findViewById(R.id.selectDLayout);
		ivA = (ImageView) pwContentView.findViewById(R.id.imageA);
		ivB = (ImageView) pwContentView.findViewById(R.id.imageB);
		ivC = (ImageView) pwContentView.findViewById(R.id.imageC);
		ivD = (ImageView) pwContentView.findViewById(R.id.imageD);
		selectAText = (TextView) pwContentView.findViewById(R.id.selectAText);
		selectBText = (TextView) pwContentView.findViewById(R.id.selectBText);
		selectCText = (TextView) pwContentView.findViewById(R.id.selectCText);
		selectDText = (TextView) pwContentView.findViewById(R.id.selectDText);
//		if(bumen != null && !"".equals(bumen)) {
//			selectBLayout.setClickable(true);
//		}else {
//			selectBLayout.setClickable(false);
//		}
//		if(zhuangtai != null && !"".equals(zhuangtai)) {
//			selectCLayout.setClickable(true);
//		}else {
//			selectCLayout.setClickable(false);
//		}
//		selectALayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				initBumens();
//			}
//		});
//		selectBLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				initZhuangTai();
//			}
//		});
//		pw.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss() {
//				Log.i("a", "bumen = " + bumen + " zhuangtai = " + zhuangtai + " bangongshi = " + bangongsi + " quyu = " + quyu + " huojia =" + huojia);
//			}
//		});
		initBumens();
	}

	public void btnClick(View view) {
		intent = new Intent();
		intent.putExtra("assetInfoList", planAssetArrayList);
		intent.putExtra("dept", bumen);
		intent.putExtra("useType", zhuangtai);
		intent.putExtra("office", bangongsi);
		intent.putExtra("warehouseArea", quyu);
		intent.putExtra("goodsShelves", huojia);
		switch (view.getId()) {
		case R.id.rfidScan:
			intent.setClass(MainScanActivity.this, ScanActivity.class);
			intent.putExtra("check", checkStr);
			this.startActivity(intent);
			break;
		case R.id.erweiScan:
			intent.setClass(MainScanActivity.this, ErWeiScanActivity.class);
			intent.putExtra("check", checkStr);
			startActivity(intent);
			break;
		case R.id.localDB:
			intent.setClass(MainScanActivity.this, LocalDBActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			GlobalParams.bumen = "";
			GlobalParams.zhuangtai = "";
			GlobalParams.bangongsi = "";
			GlobalParams.quyu = "";
			GlobalParams.huojia = "";
			this.finish();
			break;
		case R.id.more:
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
				pw = null;
			} else {
				initPopupWindow();
			}
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			GlobalParams.bumen = "";
			GlobalParams.zhuangtai = "";
			GlobalParams.bangongsi = "";
			GlobalParams.quyu = "";
			GlobalParams.huojia = "";
			this.finish();
			break;
		}
		return true;

	}
	private void initBumens() {
		ArrayList<String> deptValueArray = new ArrayList<String>();
		// ArrayList<PlanAssetInfo> deptArrayList = new
		// ArrayList<PlanAssetInfo>();
		// 筛选dept
		for (int i = 0; i < GlobalParams.planAssetInfoList.size(); i++) {
			if (deptValueArray.contains(GlobalParams.planAssetInfoList.get(i)
					.getDept()))
				continue;
			deptValueArray.add(GlobalParams.planAssetInfoList.get(i).getDept());
			// deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
		}
		// 设置listView显示数据
		final String[] values = new String[deptValueArray.size()];
		for (int i = 0; i < deptValueArray.size(); i++) {
			values[i] = (String) deptValueArray.get(i);
		}
		bumenAdapter = new CommenListAdapter(this, values);
		pwListView.setAdapter(bumenAdapter);
		pwListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(MainScanActivity.this, values[arg2],
				// 0).show();
				bumen = values[arg2];
				GlobalParams.bumen = bumen;
				if(bumen.equals(null)){
					tv1.setText("部门：");
				}else{
					tv1.setText("部门："+bumen);
				}
				tv2.setText("资产状态：");
				tv3.setText("位置：");
				zhuangtai = "";
				bangongsi = "";
				quyu = "";
				huojia = "";
				GlobalParams.zhuangtai = "";
				GlobalParams.bangongsi = "";
				GlobalParams.quyu = "";
				GlobalParams.huojia = "";
				initZhuangTai();
			}
		});
		ivA.setImageResource(R.drawable.sanjiaoshang);
		ivB.setImageResource(R.drawable.sanjiaoxia);
		ivC.setImageResource(R.drawable.sanjiaoxia);
		ivD.setImageResource(R.drawable.sanjiaoxia);
		selectAText.setTextColor(this.getResources().getColor(R.color.orange_txt));
		selectBText.setTextColor(Color.BLACK);
		selectCText.setTextColor(Color.BLACK);
		selectDText.setTextColor(Color.BLACK);
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.update();
		pw.showAsDropDown(parentView);
	}

	private void initZhuangTai() {
		ivA.setImageResource(R.drawable.sanjiaoxia);
		ivB.setImageResource(R.drawable.sanjiaoshang);
		ivC.setImageResource(R.drawable.sanjiaoxia);
		ivD.setImageResource(R.drawable.sanjiaoxia);
		selectAText.setTextColor(Color.BLACK);
		selectBText.setTextColor(this.getResources().getColor(R.color.orange_txt));
		selectCText.setTextColor(Color.BLACK);
		selectDText.setTextColor(Color.BLACK);
		ArrayList<String> useType = new ArrayList<String>();
		ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		// deptList
		for (int i = 0; i < GlobalParams.planAssetInfoList.size(); i++) {
			if (GlobalParams.planAssetInfoList.get(i).getDept().equals(bumen)) {
				deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		// 筛选资产状态
		for (int i = 0; i < deptArrayList.size(); i++) {
			if (useType.contains(deptArrayList.get(i).getUsetype()))
				continue;
			useType.add(deptArrayList.get(i).getUsetype());
		}
		// 设置listView显示数据
		final String[] values = new String[useType.size()];
		for (int i = 0; i < useType.size(); i++) {
			values[i] = (String) useType.get(i);
		}
		final String[] keys = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals("1")) { // 库存
				keys[i] = "库存";
			} else if (values[i].equals("2")) { // 在运
				keys[i] = "在运";
			} else if (values[i].equals("3")) { // 废弃
				keys[i] = "废弃";
			}
		}
		bumenAdapter = new CommenListAdapter(this, keys);
		pwListView.setAdapter(bumenAdapter);
		pwListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(MainScanActivity.this, values[arg2],
				// 0).show();
				zhuangtai = values[arg2];
				GlobalParams.zhuangtai = zhuangtai;
				if(zhuangtai.equals("null")){
					tv2.setText("资产状态：");
				}
				else if (zhuangtai.equals("2")) {
					tv2.setText("资产状态：在运");
				}else if (zhuangtai.equals("1")) {
					tv2.setText("资产状态：库存");
				}
				
				bangongsi = "";
				quyu = "";
				huojia = "";
				GlobalParams.bangongsi = "";
				GlobalParams.quyu = "";
				GlobalParams.huojia = "";
				if (zhuangtai.equals("2")) {
					// 3
					selectCText.setText("办公室");
					selectDLayout.setVisibility(View.GONE);
					initBangongshi();
				} else if (zhuangtai.equals("1")) {
					// 4
					selectCText.setText("区域");
					selectDLayout.setVisibility(View.VISIBLE);
					initQuYu();
				}
			}
		});
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.update();
		pw.showAsDropDown(parentView);
	}

	private void initQuYu() {
		ivA.setImageResource(R.drawable.sanjiaoxia);
		ivB.setImageResource(R.drawable.sanjiaoxia);
		ivC.setImageResource(R.drawable.sanjiaoshang);
		ivD.setImageResource(R.drawable.sanjiaoxia);
		selectAText.setTextColor(Color.BLACK);
		selectBText.setTextColor(Color.BLACK);
		selectCText.setTextColor(this.getResources().getColor(R.color.orange_txt));
		selectDText.setTextColor(Color.BLACK);
		ArrayList<String> warehouseArea = new ArrayList<String>();
		ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		// deptList
		for (int i = 0; i < GlobalParams.planAssetInfoList.size(); i++) {
			if (GlobalParams.planAssetInfoList.get(i).getDept().equals(bumen)) {
				deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		// 筛选资产状态
		for (int i = 0; i < deptArrayList.size(); i++) {
			if (warehouseArea.contains(deptArrayList.get(i).getWarehouseArea()))
				continue;
			warehouseArea.add(deptArrayList.get(i).getWarehouseArea());
		}
		// 设置listView显示数据
		final String[] values = new String[warehouseArea.size()];
		for (int i = 0; i < warehouseArea.size(); i++) {
			values[i] = (String) warehouseArea.get(i);
		}
		bumenAdapter = new CommenListAdapter(this, values);
		pwListView.setAdapter(bumenAdapter);
		pwListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(MainScanActivity.this, values[arg2],
				// 0).show();
				// zhuangtai = values[arg2];
				// if(zhuangtai.equals("2")) {
				// //3
				// selectCText.setText("办公室");
				// selectDLayout.setVisibility(View.GONE);
				// }else if(zhuangtai.equals("1")) {
				// //4
				// selectCText.setText("区域");
				// selectDLayout.setVisibility(View.VISIBLE);
				// }
				quyu = values[arg2];
				GlobalParams.quyu = quyu;
				if(quyu.equals("null")){
					tv3.setText("位置：");
				}else{
					tv3.setText("位置："+quyu);
				}
				
				huojia = "";
				GlobalParams.huojia = "";
				initHuoJia();
			}
		});
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.update();
		pw.showAsDropDown(parentView);
	}

	private void initHuoJia() {
		ivA.setImageResource(R.drawable.sanjiaoxia);
		ivB.setImageResource(R.drawable.sanjiaoxia);
		ivC.setImageResource(R.drawable.sanjiaoxia);
		ivD.setImageResource(R.drawable.sanjiaoshang);
		selectAText.setTextColor(Color.BLACK);
		selectBText.setTextColor(Color.BLACK);
		selectCText.setTextColor(Color.BLACK);
		selectDText.setTextColor(this.getResources().getColor(R.color.orange_txt));
		ArrayList<String> goodsShelves = new ArrayList<String>();
		ArrayList<PlanAssetInfo> areaArrayList = new ArrayList<PlanAssetInfo>();
		// deptList
		for (int i = 0; i < GlobalParams.planAssetInfoList.size(); i++) {
			if (GlobalParams.planAssetInfoList.get(i).getDept().equals(bumen)
					&& GlobalParams.planAssetInfoList.get(i).getWarehouseArea()
							.equals(quyu)) {
				areaArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		// 筛选资产状态
		for (int i = 0; i < areaArrayList.size(); i++) {
			if (goodsShelves.contains(areaArrayList.get(i).getGoodsShelves()))
				continue;
			goodsShelves.add(areaArrayList.get(i).getGoodsShelves());
		}
		// 设置listView显示数据
		final String[] values = new String[goodsShelves.size()];
		for (int i = 0; i < goodsShelves.size(); i++) {
			values[i] = (String) goodsShelves.get(i);
		}
		bumenAdapter = new CommenListAdapter(this, values);
		pwListView.setAdapter(bumenAdapter);
		pwListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				huojia = values[arg2];
				if(!huojia.equals("null")){
					tv3.setText(tv3.getText().toString()+" "+huojia);
				}
				if(huojia == null || "null".equals(huojia)) {
					huojia = "";
				}
				GlobalParams.huojia = huojia;
				pw.dismiss();
				
			}
		});
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setBackgroundDrawable(new BitmapDrawable());
		pw.update();
		pw.showAsDropDown(parentView);
	}

	private void initBangongshi() {
		ivA.setImageResource(R.drawable.sanjiaoxia);
		ivB.setImageResource(R.drawable.sanjiaoxia);
		ivC.setImageResource(R.drawable.sanjiaoshang);
		ivD.setImageResource(R.drawable.sanjiaoxia);
		selectAText.setTextColor(Color.BLACK);
		selectBText.setTextColor(Color.BLACK);
		selectCText.setTextColor(this.getResources().getColor(R.color.orange_txt));
		selectDText.setTextColor(Color.BLACK);
		ArrayList<String> offices = new ArrayList<String>();
		ArrayList<PlanAssetInfo> deptArrayList = new ArrayList<PlanAssetInfo>();
		//deptList
		for(int i = 0; i< GlobalParams.planAssetInfoList.size(); i++){
			if(GlobalParams.planAssetInfoList.get(i).getDept().equals(bumen)){
				deptArrayList.add(GlobalParams.planAssetInfoList.get(i));
			}
		}
		//筛选资产状态
		for(int i = 0; i< deptArrayList.size(); i++){
			if(offices.contains(deptArrayList.get(i).getOffice()))
				continue;
			offices.add(deptArrayList.get(i).getOffice());
		}
		//设置listView显示数据
		final String [] values = new String[offices.size()];
		 for(int i=0;i<offices.size();i++){  
			 values[i]=(String)offices.get(i);  
	     }
		bumenAdapter = new CommenListAdapter(this, values);
		pwListView.setAdapter(bumenAdapter);
		pwListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0,
					View arg1, int arg2, long arg3) {
				bangongsi = values[arg2];
				GlobalParams.bangongsi = bangongsi;
				if(bangongsi.equals("null")){
					tv3.setText("位置：");
				}else{
					tv3.setText("位置："+bangongsi);
				}
				
				pw.dismiss();
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
		
	private void refreshSelectTitle(){
		if(GlobalParams.bumen.equals(null)){
			tv1.setText("部门：");
		}else{
			tv1.setText("部门："+GlobalParams.bumen);
		}
		
		if(GlobalParams.zhuangtai.equals("null")){
			tv2.setText("资产状态：");
		}
		else if (GlobalParams.zhuangtai.equals("2")) {
			tv2.setText("资产状态：在运");
			if(GlobalParams.bangongsi.equals("null")){
				tv3.setText("位置：");
			}else{
				tv3.setText("位置："+GlobalParams.bangongsi);
			}
		}else if (GlobalParams.zhuangtai.equals("1")) {
			tv2.setText("资产状态：库存");
			if(GlobalParams.quyu.equals("null")){
				tv3.setText("位置：");
			}else{
				tv3.setText("位置："+GlobalParams.quyu);
			}
			
			if(!GlobalParams.huojia.equals("null")){
				tv3.setText(tv3.getText().toString()+" "+GlobalParams.huojia);
			}
		}
	}
	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		GlobalParams.bumen = "";
//		GlobalParams.zhuangtai = "";
//		GlobalParams.bangongsi = "";
//		GlobalParams.quyu = "";
//		GlobalParams.huojia = "";
//	}
	
}
