package com.gdht.itasset;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdht.itasset.eventbus.SelectClassifyAndTypeListener;
import com.gdht.itasset.http.HttpClientUtil;
import com.gdht.itasset.pojo.ZiChanFenLeiInfo;
import com.gdht.itasset.utils.GlobalParams;
import com.gdht.itasset.widget.WaitingDialog;

import de.greenrobot.event.EventBus;

public class SelectZCFLActivity extends Activity {

	private ImageView imgView = null;
	private TextView assetTypeNameTV = null;
	private ListView assetTypeListView = null;
	private ArrayList<String> assetTypeArray = null;
	private MyAdapter adapter = null;
	private String deptcode = "";
	private List<ZiChanFenLeiInfo> infos = new ArrayList<ZiChanFenLeiInfo>();
	private WaitingDialog dialog;
	private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xin_zeng);
		EventBus.getDefault().register(this);
		deptcode = this.getIntent().getStringExtra("deptcode");
		from = this.getIntent().getStringExtra("from");
		findViews();
		setOnClicks();
		new GetAssetTypesByUserAndDeptAs().execute("");
	}

	private class GetAssetTypesByUserAndDeptAs extends
			AsyncTask<String, Integer, List<ZiChanFenLeiInfo>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new WaitingDialog(SelectZCFLActivity.this,
					R.style.TRANSDIALOG);
			dialog.show();
		}

		@Override
		protected List<ZiChanFenLeiInfo> doInBackground(String... arg0) {
			return new HttpClientUtil(SelectZCFLActivity.this).getAssetTypesByUserAndDept(
					SelectZCFLActivity.this, GlobalParams.username, deptcode);
		}

		@Override
		protected void onPostExecute(List<ZiChanFenLeiInfo> result) {
			dialog.dismiss();
			if (result.size() > 0) {
				// for(ZiChanFenLeiInfo in : result) {
				// Log.i("a", "zichanfenlei = " + in.toString());
				// }
				infos.addAll(result);
				adapter.notifyDataSetChanged();
			}
		}

	}

	// 资产类型列表项点击事件
	private void setOnClicks() {
		assetTypeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if("zcfl".equals(from)) {
					Intent intent = new Intent();
					intent.putExtra("zcflName", infos.get(position).getValue());
					intent.putExtra("zcflCode", infos.get(position).getKey());
					setResult(99, intent);
					finish();
				}else {
					Intent intent = new Intent(SelectZCFLActivity.this, SelectZCLXActivity.class);
					intent.putExtra("deptcode", deptcode);
					intent.putExtra("zcflName", infos.get(position).getValue());
					intent.putExtra("zcflCode", infos.get(position).getKey());
					intent.putExtra("from", from);
					startActivity(intent);
				}
			}
		});
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			this.finish();
			break;
		default:
			break;
		}
	}

	private void findViews() {

		assetTypeListView = (ListView) findViewById(R.id.assetType_listView);
		adapter = new MyAdapter();
		assetTypeListView.setAdapter(adapter);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = getLayoutInflater().inflate(
					R.layout.asset_type_listitem, null);
			imgView = (ImageView) convertView.findViewById(R.id.img);
			assetTypeNameTV = (TextView) convertView
					.findViewById(R.id.assetType_tv);
			ZiChanFenLeiInfo zcfl = infos.get(position);
			assetTypeNameTV.setText(zcfl.getValue());
			// 设置图片
			if ("外部设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.waibushebei);
			} else if ("辅助设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.fuzhushebei);
			} else if ("设备配件".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.shebeipeijian);
			} else if ("网络设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.wangluoshebei);
			} else if ("存储设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.cunchushebei);
			} else if ("主机设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.zhujishebei);
			} else if ("终端设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.zhongduanshebei);
			} else if ("安全设备".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.anquanshebei);
			} else if ("IT资产".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.itzichan);
			} else if ("房屋建筑物".equals(zcfl.getValue())) {
				imgView.setImageResource(R.drawable.fangwujianzhuwu);
			}

			return convertView;
		}

	}
	
	public void onEvent(SelectClassifyAndTypeListener event) {
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
