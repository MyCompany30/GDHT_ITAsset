package com.gdht.itasset.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gdht.itasset.R;
import com.gdht.itasset.pojo.BuMenInfo;
import com.gdht.itasset.pojo.CangKuInfo;
import com.gdht.itasset.pojo.DeptInfo;
import com.gdht.itasset.pojo.PlanAssetInfo;
import com.gdht.itasset.pojo.PlanInfo;
import com.gdht.itasset.pojo.StockItem;
import com.gdht.itasset.pojo.YingPanXinZengItem;
import com.gdht.itasset.pojo.ZiChanFenLeiInfo;
import com.gdht.itasset.pojo.ZiChanZiFenLeiInfo;
import com.gdht.itasset.utils.AppSharedPreferences;

public class HttpClientUtil {
	private Context context;
	private String ip = "";
	public HttpClientUtil(Context context) {
		this.context = context;
		this.ip = new AppSharedPreferences(context, "gdht").getIP();
	}
	
	private static HttpClient httpClient = null;
	public static synchronized HttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}
	//获取rfid对应的详细信息
	/**
	 * 
	 * @param activity
	 * @param urlResoucesId  strings文件里对应的url地址id
	 * @param paramName     rfid标签号：Rfid  条码：barCode  二维码：qrCode
	 * @param assetCheckplanId 盘点计划id
	 * @param data			标签号字符串，用逗号间隔
	 * @param dataArray     listView显示需要的list对象
	 * @return
	 */
	public synchronized ArrayList<StockItem> checkAssetByCodes(Activity activity,String planId, int urlResoucesId, String paramName, String data, ArrayList<StockItem> dataArray){
		String uri = null;
		data = data.replaceAll(" ", "%20");
		uri = activity.getResources().getString(urlResoucesId);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair(paramName, data));
		formparams.add(new BasicNameValuePair("assetCheckplanId", planId));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);		
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String result = EntityUtils.toString(httpResponse.getEntity());
				//处理返回结果
				strToJsonList(result, dataArray);
			}else{
				Toast.makeText(activity, "消息异常，状态码："+httpResponse.getStatusLine().getStatusCode(), Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(activity, "网络异常", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return null;
		}
		return dataArray;
	}
	/** 根据计划id和盘点状态获取资产
	 * @param assetCheckplanId 盘点计划主键
	 * @param checkstate 盘点状态  0未盘、1已盘、2盘盈、3盘亏
	 * @return 盘点计划中的资产清单集合
	*/
	public ArrayList<StockItem> getDataByStatus(Activity activity, ArrayList<StockItem> arrayList, String assetCheckplanId, String statusCode){
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_getAssetList);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("assetCheckplanId", assetCheckplanId));
		formparams.add(new BasicNameValuePair("checkstate", statusCode));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = EntityUtils.toString(httpResponse.getEntity());
				JSONArray jsonArray = new JSONArray(result);
				for(int i = 0; i< jsonArray.length(); i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					StockItem stockItem = new StockItem();
					stockItem.setChecked(false);
					stockItem.setAssetName(jsonObject.has("name")?jsonObject.getString("name"):"");
					stockItem.setAssetType(jsonObject.has("type")?jsonObject.getString("type"):"");
					stockItem.setBrandModel((jsonObject.has("brand")?jsonObject.getString("brand"):"")+"-"+(jsonObject.has("model")?jsonObject.getString("model"):""));
					stockItem.setCheckstate(jsonObject.has("checkstate")?jsonObject.getString("checkstate"):"");
					stockItem.setKeeper(jsonObject.has("keeper")?jsonObject.getString("keeper"):"");
					//stockItem.setName(jsonObject.getString("name"));
					stockItem.setUseType(jsonObject.has("usetype")?jsonObject.getString("usetype"):"");
					if(stockItem.getUseType().equals("1")){
						//库存
						stockItem.setDeptQyHj((jsonObject.has("dept")?jsonObject.getString("dept"):"")+(jsonObject.has("warehouseArea")?jsonObject.getString("warehouseArea"):"")+(jsonObject.has("goodsShelves")?jsonObject.getString("goodsShelves"):""));
						stockItem.setDept(jsonObject.has("dept")?jsonObject.getString("dept"):"");
						stockItem.setQy(jsonObject.has("warehouseArea")?jsonObject.getString("warehouseArea"):"");
						stockItem.setHj(jsonObject.has("goodsShelves")?jsonObject.getString("goodsShelves"):"");
					}else if(stockItem.getUseType().equals("2")){
						//在运
						stockItem.setDeptOffice((jsonObject.has("dept")?jsonObject.getString("dept"):"")+(jsonObject.has("office")?jsonObject.getString("office"):""));
						stockItem.setDept(jsonObject.has("dept")?jsonObject.getString("dept"):"");
						stockItem.setOffice(jsonObject.has("office")?jsonObject.getString("office"):"");
					}
					stockItem.setRfidLabelnum(jsonObject.has("rfidnumber")?jsonObject.getString("rfidnumber"):"");
					stockItem.setBarNumber(jsonObject.has("barnumber")?jsonObject.getString("barnumber"):"");
					stockItem.setQrNumber(jsonObject.has("qrnumber")?jsonObject.getString("qrnumber"):"");
					stockItem.setAssetInfoId(jsonObject.has("assetInfoId")?jsonObject.getString("assetInfoId"):"");
					stockItem.setAssetChecklistId(jsonObject.has("assetChecklistId")?jsonObject.getString("assetChecklistId"):"");
					stockItem.setAssetCheckplanId(jsonObject.has("assetCheckplanId")?jsonObject.getString("assetCheckplanId"):"");
					arrayList.add(stockItem);
				}

			}
		} catch (Exception e) {
			//网络异常
			e.printStackTrace();
			return null;
		}
	
		return arrayList;
	}
	
	
	
	
	public ArrayList<StockItem> strToJsonList(String result, ArrayList<StockItem> dataArray) throws JSONException {
		JSONArray jsonArray = new JSONArray(result);
		for(int i = 0; i< jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			StockItem stockItem = new StockItem();
			stockItem.setChecked(false);
			stockItem.setAssetName(jsonObject.has("name")?jsonObject.getString("name"):"");
			stockItem.setAssetType(jsonObject.has("type")?jsonObject.getString("type"):"");
			stockItem.setBrandModel((jsonObject.has("brand")?jsonObject.getString("brand"):"")+"-"+(jsonObject.has("model")?jsonObject.getString("model"):""));
			stockItem.setCheckstate(jsonObject.has("checkstate")?jsonObject.getString("checkstate"):"");
			stockItem.setUseType(jsonObject.has("usetype")?jsonObject.getString("usetype"):"");
			if(stockItem.getUseType().equals("1")){
				//库存
				stockItem.setDeptQyHj((jsonObject.has("dept")?jsonObject.getString("dept"):"")+(jsonObject.has("warehouseArea")?jsonObject.getString("warehouseArea"):"")+(jsonObject.has("goodsShelves")?jsonObject.getString("goodsShelves"):""));
				stockItem.setDept(jsonObject.has("dept")?jsonObject.getString("dept"):"");
				stockItem.setQy(jsonObject.has("warehouseArea")?jsonObject.getString("warehouseArea"):"");
				stockItem.setHj(jsonObject.has("goodsShelves")?jsonObject.getString("goodsShelves"):"");
			}else if(stockItem.getUseType().equals("2")){
				//在运
				stockItem.setDeptOffice((jsonObject.has("dept")?jsonObject.getString("dept"):"")+(jsonObject.has("office")?jsonObject.getString("office"):""));
				stockItem.setDept(jsonObject.has("dept")?jsonObject.getString("dept"):"");
				stockItem.setOffice(jsonObject.has("office")?jsonObject.getString("office"):"");
			}
			stockItem.setKeeper(jsonObject.has("keeper")?jsonObject.getString("keeper"):"");
			//stockItem.setName(jsonObject.getString("name"));
			stockItem.setRfidLabelnum(jsonObject.has("rfidnumber")?jsonObject.getString("rfidnumber"):"");
			stockItem.setBarNumber(jsonObject.has("barnumber")?jsonObject.getString("barnumber"):"");
			stockItem.setQrNumber(jsonObject.has("qrnumber")?jsonObject.getString("qrnumber"):"");
			stockItem.setAssetInfoId(jsonObject.has("id")?jsonObject.getString("id"):"");
			stockItem.setDetil(jsonObject.has("detil")?jsonObject.getString("detil"):"");
			stockItem.setAssetChecklistId(jsonObject.has("assetChecklistId")?jsonObject.getString("assetChecklistId"):"");
			stockItem.setAssetCheckplanId(jsonObject.has("assetCheckplanId")?jsonObject.getString("assetCheckplanId"):"");
			dataArray.add(stockItem);
		}
		for(StockItem si : dataArray) {
			Log.i("a", "si = " + si.toString());
		}
		return dataArray;
	}
	//登录
	public synchronized String login(Activity activity, String name, String pwd, String deviceId, String appName, String ip) {
		String uri = null;
		uri = activity.getResources().getString(R.string.url_login);
		Log.i("a", ip + uri);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("userName", name));
		formparams.add(new BasicNameValuePair("password", pwd));
		formparams.add(new BasicNameValuePair("imei", deviceId));
		formparams.add(new BasicNameValuePair("appname", appName));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String result = EntityUtils.toString(httpResponse.getEntity());
				if(result.equals("1")){
					return "登录成功";
					//jsonObject = new JSONObject(result);
				}else if(result.equals("2")){
					  return "没有权限"; 
				}else if(result.equals("0")){
					return "用户名或密码错误";
				}
			}
		} catch (Exception e) {
			//网络异常
			e.printStackTrace();
			return "网络异常";
		}
		return "未知错误";
	}
	
	//获取盘点计划
	public synchronized ArrayList<PlanInfo> getPlans(Activity activity, String userId){
		ArrayList<PlanInfo> arrayList = new ArrayList<PlanInfo>();
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_getPlans)+"?userId="+userId;
		HttpPost get = new HttpPost(ip + uri);
		try {
			HttpResponse httpResponse = getHttpClient().execute(get);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
				jsonArray = new JSONArray(result);
				for(int i = 0; i<jsonArray.length(); i++){
					PlanInfo planInfo = new PlanInfo();
					jsonObject = jsonArray.getJSONObject(i);
					planInfo.setTitle(jsonObject.getString("title"));
					planInfo.setDepts(jsonObject.getString("depts"));
					planInfo.setId(jsonObject.getString("id"));
					arrayList.add(planInfo);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return arrayList;
		}
		return arrayList;
	}
	
	//获取盘点计划详细信息
	public synchronized ArrayList<PlanAssetInfo> getPlanInfoById(Activity activity, String planid){
		ArrayList<PlanAssetInfo> arrayList = new ArrayList<PlanAssetInfo>();
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_getPlanInfoById)+"?planid="+planid;
		HttpPost get = new HttpPost(ip + uri);
		try {
			HttpResponse httpResponse = getHttpClient().execute(get);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
				jsonArray = new JSONArray(result);
				for(int i = 0; i<jsonArray.length(); i++){
					PlanAssetInfo planAssetInfo = new PlanAssetInfo();
					jsonObject = jsonArray.getJSONObject(i);
					planAssetInfo.setAssetName(jsonObject.getString("assetName"));// 资产名称
					planAssetInfo.setRfidnumber(jsonObject.getString("rfidnumber"));// rfid标签号
					planAssetInfo.setBarnumber(jsonObject.getString("barnumber"));// 条形码标签号
					planAssetInfo.setQrnumber(jsonObject.getString("qrnumber"));// 二维码标签号
					planAssetInfo.setUsetype(jsonObject.getString("usetype"));// 资产状态(1库存备用 2.在运)
					planAssetInfo.setDept(jsonObject.getString("dept"));// 部门
					planAssetInfo.setOffice(jsonObject.getString("office"));// 办公室
					planAssetInfo.setWarehouseArea(jsonObject.getString("warehouseArea"));//仓库区域
					planAssetInfo.setGoodsShelves(jsonObject.getString("goodsShelves"));//区域货架
					//planAssetInfo.setId(jsonObject.getString("id"));// 资产id主键

					arrayList.add(planAssetInfo);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return arrayList;
		}
		return arrayList;
	}
	
	/**
	 * 
	 * 设置盘点状态
	 * @param assetCheckplanId 盘点计划主键
	 * @param assetInfoId 资产基本信息主键
	 * @param detil 原因(选填)
	 * @param checkstate 盘点状态 0未盘、1已盘、2盘盈、3盘亏
	 * @param registrant 盘点人
	 * @return 1为盘点成功，其他为失败
	 */
	public synchronized String updateAssetStatus(Activity activity, String planId, String assetId, String detail, String checkstate, String registrant){
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_updateassetstatus);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("assetCheckplanId", planId));
		formparams.add(new BasicNameValuePair("assetInfoId", assetId));
		formparams.add(new BasicNameValuePair("detil", detail));
		formparams.add(new BasicNameValuePair("checkstate", checkstate));
		formparams.add(new BasicNameValuePair("registrant", registrant));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * 增加使用记录(更新标签号设备系信息)
	 * @param assetInfoId 资产主键id
	 * @param dept 部门仓库key
	 * @param warehouseArea 仓库区域(选填)
	 * @param goodsShelves 所在货架(选填)
	 * @param office 办公室key
	 * @param keeper 责任人(手填)
	 * @param usetype 资产状态(1库存备用 2.在运 3.退役)
	 * @param detail 备注
	 * @param isretrun 1归还2不归还
	 * @param retrundate 归还日期(根据上面决定)
	 * @return 1是成功，其他都是出错
	 */
	public synchronized String updateAssetInfoCK(Activity activity, String assetInfoId, String dept, String keeper,String usetype, String detail){
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_updateAssetInfo);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("assetInfoId", assetInfoId));
		formparams.add(new BasicNameValuePair("dept", dept));
		formparams.add(new BasicNameValuePair("keeper", keeper));
		formparams.add(new BasicNameValuePair("usetype", usetype));
		formparams.add(new BasicNameValuePair("detail", detail));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	public synchronized String updateAssetInfoZY(Activity activity, String assetInfoId, String dept,String office, String keeper,String usetype, String detail){
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_updateAssetInfo);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("assetInfoId", assetInfoId));
		formparams.add(new BasicNameValuePair("dept", dept));
		if(office!=null){
			formparams.add(new BasicNameValuePair("office", office));
		}
		formparams.add(new BasicNameValuePair("keeper", keeper));
		formparams.add(new BasicNameValuePair("usetype", usetype));
		formparams.add(new BasicNameValuePair("detail", detail));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	/**
	 * 获取所有的仓库
	 * @return 返回一个map 包含 两个ArrayList<String> 分别是仓库名 和 仓库对应的key	
	 */
	public synchronized HashMap<String,ArrayList<String>> getWarehouse(Activity activity){
		HashMap<String,ArrayList<String>> arrayMap = new HashMap<String,ArrayList<String>>();
		ArrayList<String> keyArray = new ArrayList<String>();
		ArrayList<String> valueArray = new ArrayList<String>();
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_getWarehouse);
		HttpPost get = new HttpPost(ip + uri);
		try {
			HttpResponse httpResponse = getHttpClient().execute(get);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
				jsonArray = new JSONArray(result);
				for(int i = 0; i< jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					keyArray.add(jsonObject.getString("key"));
					valueArray.add(jsonObject.getString("value"));
				}
				arrayMap.put("key", keyArray);
				arrayMap.put("value", valueArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return arrayMap;
		}
		return arrayMap;
	}
	
	/**
	 *获取所有的部门
	 *@return 返回一个key和value的集合	
	 */
	public synchronized HashMap<String,ArrayList<String>> getDept(Activity activity){
		HashMap<String,ArrayList<String>> arrayMap = new HashMap<String,ArrayList<String>>();
		ArrayList<String> keyArray = new ArrayList<String>();
		ArrayList<String> valueArray = new ArrayList<String>();
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_getDept);
		HttpPost get = new HttpPost(ip + uri);
		try {
			HttpResponse httpResponse = getHttpClient().execute(get);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
				jsonArray = new JSONArray(result);
				for(int i = 0; i< jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					keyArray.add(jsonObject.getString("key"));
					valueArray.add(jsonObject.getString("value"));
				}
				arrayMap.put("key", keyArray);
				arrayMap.put("value", valueArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return arrayMap;
		}
		return arrayMap;
	}
	
	/**
	 *根据父节点获取下一级子节点
	 *@param code 父节点的key
	 *@return 返回一个key和value的集合	
	 */
	public synchronized HashMap<String, ArrayList<String>> getCode(Activity activity, String code){
		HashMap<String,ArrayList<String>> arrayMap = new HashMap<String,ArrayList<String>>();
		ArrayList<String> keyArray = new ArrayList<String>();
		ArrayList<String> valueArray = new ArrayList<String>();
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		String result = null;
		String uri = null;
		uri = activity.getResources().getString(R.string.url_getCode);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("code", code));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);

		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
				jsonArray = new JSONArray(result);
				for(int i = 0; i< jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					keyArray.add(jsonObject.getString("key"));
					valueArray.add(jsonObject.getString("value"));
				}
				arrayMap.put("key", keyArray);
				arrayMap.put("value", valueArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return arrayMap;
		}
		return arrayMap;
	}
	
	/**
	 * @param assetInfoInId 资产主键id
	 * @param repairtype 维修类型(1.故障 2.隐患)
	 * @param description 故障描述
	 * @param userid 当前登录人id
	 * @param dept 部门或仓库
	 * @return 1登记成功 其他登记失败
	 */
	public synchronized String addRepairInfo(Activity activity, String assetInfoInId, String repairtype, String description,String userid){
		String result = "";
		String uri = null;
		uri = activity.getResources().getString(R.string.url_addRepairInfo);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("assetInfoInId", assetInfoInId));
		formparams.add(new BasicNameValuePair("repairtype", repairtype));
		formparams.add(new BasicNameValuePair("description", description));
		formparams.add(new BasicNameValuePair("userid", userid));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = new String(EntityUtils.toString(httpResponse.getEntity()).getBytes(),"UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result; 
		}
		if(result.equals("-1")){
			return "该资产已有故障上报";
		}
		return result;
	}
	
	public List<BuMenInfo> getAllDeptByUser(Activity activity, String username) {
		Log.i("a", "username = " + username);
		List<BuMenInfo> infos = new ArrayList<BuMenInfo>();
		String uri = null;
		String result = null;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = activity.getResources().getString(R.string.url_getAllDeptByUser);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ID_", username));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("a", "资产分类 = " + result);
				jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					BuMenInfo info = new BuMenInfo();
					info.setKey(jsonObject.getString("key"));
					info.setValue(jsonObject.getString("value"));
					infos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return infos;
	}
	public List<CangKuInfo> getStoresByUser(Activity activity, String username) {
		Log.i("a", "username = " + username);
		List<CangKuInfo> infos = new ArrayList<CangKuInfo>();
		String uri = null;
		String result = null;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = activity.getResources().getString(R.string.url_getStoresByUser);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ID_", username));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("a", "资产分类 = " + result);
				jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					CangKuInfo info = new CangKuInfo();
					info.setKey(jsonObject.getString("key"));
					info.setValue(jsonObject.getString("value"));
					infos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return infos;
	}

	public List<ZiChanFenLeiInfo> getAssetTypesByUserAndDept(Activity activity,
			String username, String deptId) {
		List<ZiChanFenLeiInfo> infos = new ArrayList<ZiChanFenLeiInfo>();
		Log.i("a", "username = " + username + "deptId = " + deptId);
		String uri = null;
		String result = null;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = activity.getResources().getString(
				R.string.url_getAssetTypesByUserAndDept);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ID_", username));
		pairs.add(new BasicNameValuePair("deptId", deptId));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("a", "资产分类 = " + result);
				jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					// Log.i("a", "value = " + jsonObject.getString("value"));
					ZiChanFenLeiInfo info = new ZiChanFenLeiInfo();
					info.setKey(jsonObject.getString("key"));
					info.setValue(jsonObject.getString("value"));
					infos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	public List<ZiChanZiFenLeiInfo> getAssetTypesByPTypeAndUser(
			Activity activity, String username, String deptId, String pType) {
		List<ZiChanZiFenLeiInfo> infos = new ArrayList<ZiChanZiFenLeiInfo>();
		String uri = null;
		String result = null;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = activity.getResources().getString(
				R.string.url_getAssetTypesByPTypeAndUser);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ID_", username));
		pairs.add(new BasicNameValuePair("deptId", deptId));
		pairs.add(new BasicNameValuePair("pType", pType));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("a", "资产分类 = " + result);
				jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					Log.i("a", "value = " + jsonObject.getString("value"));
					ZiChanZiFenLeiInfo info = new ZiChanZiFenLeiInfo();
					info.setKey(jsonObject.getString("key"));
					info.setValue(jsonObject.getString("value"));
					infos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * 盘盈新增
	 * 
	 * @param activity
	 * @param item
	 * @return
	 */
	public synchronized String pyxz(Context context, YingPanXinZengItem item) {
		String result = null;
		String uri = null;
		uri = context.getResources().getString(R.string.url_pyxz);
		HttpPost post = new HttpPost(ip + uri);
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("assetCheckplanId", item
				.getAssetCheckplanId()));
		formparams.add(new BasicNameValuePair("rfidLabelnum", item
				.getRfid_labelnum()));
		formparams.add(new BasicNameValuePair("classify", item.getClassify()));
		formparams.add(new BasicNameValuePair("type", item.getType()));
		formparams.add(new BasicNameValuePair("name", item.getName()));
		formparams.add(new BasicNameValuePair("registrant", item
				.getRegistrant()));
		formparams.add(new BasicNameValuePair("assetUse.dept", item.getDept()));
		formparams.add(new BasicNameValuePair("assetUse.office", ""));
		formparams.add(new BasicNameValuePair("assetUse.keeper", item
				.getKeeper()));
		formparams.add(new BasicNameValuePair("isck", item.getIsck()));
		formparams.add(new BasicNameValuePair("buyDate", item.getBuyDate()));
		formparams.add(new BasicNameValuePair("lifetime", item.getLifetime()));
		formparams.add(new BasicNameValuePair("shfwdqsj", item.getShfwdqsj()));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		post.setEntity(entity);
		try {
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = new String(EntityUtils.toString(
						httpResponse.getEntity()).getBytes(), "UTF-8");
				Log.i("a", "添加result = " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public String isHasRfid(Context context, String rfidNumber) {
		String uri = null;
		String result = "false";
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = context.getResources().getString(R.string.url_isHasRfid);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("rfidNumber", rfidNumber));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				// Log.i("a", "是否存在 = " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> rfidfilter(Context context, String rfids) {
		String uri = null;
		String resultStr = "";
		JSONObject jsonObject = null;
		List<String> result = new ArrayList<String>();
		JSONArray jsonArray = null;
		uri = context.getResources().getString(R.string.url_rfidfilter);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("rfid", rfids));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				resultStr = EntityUtils.toString(httpResponse.getEntity());
				Log.i("a", "resultStr = " + resultStr);
				jsonArray = new JSONArray(resultStr);
				Log.i("a", "size = " +jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					String s = jsonArray.getString(i);
					Log.i("a", "http s = " + s);
					s = s.replace("'","");
					result.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<DeptInfo> getAllDepts(Activity activity, String username) {
		Log.i("a", "username = " + username);
		List<DeptInfo> infos = new ArrayList<DeptInfo>();
		String uri = null;
		String result = null;
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = activity.getResources().getString(R.string.url_getStoresByUser);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("ID_", username));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("a", "资产分类 = " + result);
				jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);
					CangKuInfo info = new CangKuInfo();
					info.setKey(jsonObject.getString("key"));
					info.setValue(jsonObject.getString("value"));
					infos.add(info);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		String uri2 = null;
		String result2 = null;
		JSONObject jsonObject2 = null;
		JSONArray jsonArray2 = null;
		uri2 = activity.getResources().getString(R.string.url_getAllDeptByUser);
		HttpPost post2 = new HttpPost(ip + uri2);
		List<NameValuePair> pairs2 = new ArrayList<NameValuePair>();
		pairs2.add(new BasicNameValuePair("ID_", username));
		HttpEntity entity2 = null;
		try {
			entity2 = new UrlEncodedFormEntity(pairs2, "UTF-8");
			post2.setEntity(entity2);
			HttpResponse httpResponse2 = getHttpClient().execute(post2);
			if (httpResponse2.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result2 = EntityUtils.toString(httpResponse2.getEntity());
				Log.i("a", "资产分类 = " + result);
				jsonArray2 = new JSONArray(result2);
				for (int i = 0; i < jsonArray2.length(); i++) {
					//if(i==0) continue;
					jsonObject2 = jsonArray2.getJSONObject(i);
					BuMenInfo info2 = new BuMenInfo();
					info2.setKey(jsonObject2.getString("key"));
					info2.setValue(jsonObject2.getString("value"));
					infos.add(info2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	public String lastAppVersion(Context context, String appName) {
		String uri = null;
		String result = "0";
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		uri = context.getResources().getString(R.string.url_lastAppVersion);
		 Log.i("a", "uri = " + ip + uri);
		HttpPost post = new HttpPost(ip + uri);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("appName", appName));
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairs, "UTF-8");
			post.setEntity(entity);
			HttpResponse httpResponse = getHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				 Log.i("a", "版本号 = " + result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}