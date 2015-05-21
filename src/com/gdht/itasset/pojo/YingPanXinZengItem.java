package com.gdht.itasset.pojo;

import android.widget.EditText;
import android.widget.TextView;

public class YingPanXinZengItem {

	private String assetCheckplanId;// 盘点计划主键id
	private String rfid_labelnum;// rfid标签号
	private String classify;// 资产分类
	private String type;// 资产类型
	private String name;// 资产名称
	private String registrant;// 登记人(id)
	private String dept;// 部门
	private String deptName; //部门仓库名称
	private String office;// 办公室
	private String keeper;// 责任人
	private String buyDate;  //购买日期  时间戳精确到日
	private String lifetime; //预计寿命   int型  按月算
	private String shfwdqsj; //售后服务到期时间  时间戳精确到日
	private String isck = "1";// 仓库新增还是在运新增(1仓库 2在运)
	private EditText fuzerennameEt;
	private TextView rfidCodeTv;
	private EditText assetNameEt;
	private TextView zcflTv;
	private TextView zclbTv;
	private String zcflCode;
	private String zcflName;
	private String zclxCode;
	private String zclxName;
	public String getAssetCheckplanId() {
		return assetCheckplanId;
	}
	public void setAssetCheckplanId(String assetCheckplanId) {
		this.assetCheckplanId = assetCheckplanId;
	}
	public String getRfid_labelnum() {
		return rfid_labelnum;
	}
	public void setRfid_labelnum(String rfid_labelnum) {
		this.rfid_labelnum = rfid_labelnum;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistrant() {
		return registrant;
	}
	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getKeeper() {
		return keeper;
	}
	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getLifetime() {
		return lifetime;
	}
	public void setLifetime(String lifetime) {
		this.lifetime = lifetime;
	}
	public String getShfwdqsj() {
		return shfwdqsj;
	}
	public void setShfwdqsj(String shfwdqsj) {
		this.shfwdqsj = shfwdqsj;
	}
	public String getIsck() {
		return isck;
	}
	public void setIsck(String isck) {
		this.isck = isck;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public EditText getFuzerennameEt() {
		return fuzerennameEt;
	}
	public void setFuzerennameEt(EditText fuzerennameEt) {
		this.fuzerennameEt = fuzerennameEt;
	}
	public TextView getRfidCodeTv() {
		return rfidCodeTv;
	}
	public void setRfidCodeTv(TextView rfidCodeTv) {
		this.rfidCodeTv = rfidCodeTv;
	}
	public EditText getAssetNameEt() {
		return assetNameEt;
	}
	public void setAssetNameEt(EditText assetNameEt) {
		this.assetNameEt = assetNameEt;
	}
	
	
	
	public TextView getZcflTv() {
		return zcflTv;
	}
	public void setZcflTv(TextView zcflTv) {
		this.zcflTv = zcflTv;
	}
	public TextView getZclbTv() {
		return zclbTv;
	}
	public void setZclbTv(TextView zclbTv) {
		this.zclbTv = zclbTv;
	}
	public String getZcflCode() {
		return zcflCode;
	}
	public void setZcflCode(String zcflCode) {
		this.zcflCode = zcflCode;
	}
	public String getZcflName() {
		return zcflName;
	}
	public void setZcflName(String zcflName) {
		this.zcflName = zcflName;
	}
	public String getZclxCode() {
		return zclxCode;
	}
	public void setZclxCode(String zclxCode) {
		this.zclxCode = zclxCode;
	}
	public String getZclxName() {
		return zclxName;
	}
	public void setZclxName(String zclxName) {
		this.zclxName = zclxName;
	}
	@Override
	public String toString() {
		return "YingPanXinZengItem [assetCheckplanId=" + assetCheckplanId
				+ ", rfid_labelnum=" + rfid_labelnum + ", name=" + name
				+ ", registrant=" + registrant + ", dept=" + dept + ", office="
				+ office + ", keeper=" + keeper + ", isck=" + isck
				+ ", zcflCode=" + zcflCode + ", zclxCode=" + zclxCode + "]";
	}
	
	
	
	
	
	

}
