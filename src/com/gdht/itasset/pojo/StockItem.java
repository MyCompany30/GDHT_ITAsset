package com.gdht.itasset.pojo;

public class StockItem {
	private boolean  isChecked;
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	private String useType;           //资产状态:    1 库存  2在运  3退役
	public String getBarNumber() {
		return barNumber;
	}
	public void setBarNumber(String barNumber) {
		this.barNumber = barNumber;
	}
	public String getQrNumber() {
		return qrNumber;
	}
	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public void setOperation(boolean operation) {
		this.operation = operation;
	}
	private String rfidLabelnum;  //RFID标签编号
	private String barNumber; 	  //条码
	private String qrNumber;      //二维码
	private String assetType;     //资产类别
	private String assetName;     //资产名称
	private String name;          //名称
	private String brandModel;    //品牌型号
	private String deptOffice;    //部门办公室
	private String deptQyHj;      //仓库区域货架
	private String dept;
	public String getDetil() {
		return detil;
	}
	public void setDetil(String detil) {
		this.detil = detil;
	}
	private String detil;
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
	public String getQy() {
		return qy;
	}
	public void setQy(String qy) {
		this.qy = qy;
	}
	public String getHj() {
		return hj;
	}
	public void setHj(String hj) {
		this.hj = hj;
	}
	private String office;
	private String qy;      //仓库区域货架
	private String hj;
	public String getDeptQyHj() {
		return deptQyHj;
	}
	public void setDeptQyHj(String deptQyHj) {
		this.deptQyHj = deptQyHj;
	}
	private String keeper;        //责任人
	private String checkstate;    //盘点状态
	private boolean operation;    //操作
	private String assetInfoId;    //资产基本信息表主键，用于调拨
	private String assetChecklistId;// 盘点清单表主键，用于更改盘点状态
	private String assetCheckplanId;// 盘点计划ID,用于盘盈新增
	public String getAssetChecklistId() {
		return assetChecklistId;
	}
	public void setAssetChecklistId(String assetChecklistId) {
		this.assetChecklistId = assetChecklistId;
	}
	public String getAssetCheckplanId() {
		return assetCheckplanId;
	}
	public void setAssetCheckplanId(String assetCheckplanId) {
		this.assetCheckplanId = assetCheckplanId;
	}
	public String getAssetInfoId() {
		return assetInfoId;
	}
	public void setAssetInfoId(String assetInfoId) {
		this.assetInfoId = assetInfoId;
	}
	public String getRfidLabelnum() {
		return rfidLabelnum;
	}
	public void setRfidLabelnum(String rfidLabelnum) {
		this.rfidLabelnum = rfidLabelnum;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrandModel() {
		return brandModel;
	}
	public void setBrandModel(String brandModel) {
		this.brandModel = brandModel;
	}
	public String getDeptOffice() {
		return deptOffice;
	}
	public void setDeptOffice(String deptOffice) {
		this.deptOffice = deptOffice;
	}
	public String getKeeper() {
		return keeper;
	}
	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}
	public String getCheckstate() {
		//0未盘 1已盘 2 盘盈 3盘亏
		if("0".equals(checkstate)){
			return "未盘";
		}
		if("1".equals(checkstate)){
			return "已盘";
		}
		if("2".equals(checkstate)){
			return "盘盈";
		}
		if("3".equals(checkstate)){
			return "盘亏";
		}
		return "未知状态";
	}
	public void setCheckstate(String checkstate) {
		this.checkstate = checkstate;
	}
	public boolean getOperation() {
		String s = getCheckstate();
		if(s.equals("2")&&s.equals("3")){
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		return "StockItem [isChecked=" + isChecked + ", useType=" + useType
				+ ", rfidLabelnum=" + rfidLabelnum + ", barNumber=" + barNumber
				+ ", qrNumber=" + qrNumber + ", assetType=" + assetType
				+ ", assetName=" + assetName + ", name=" + name
				+ ", brandModel=" + brandModel + ", deptOffice=" + deptOffice
				+ ", deptQyHj=" + deptQyHj + ", dept=" + dept + ", detil="
				+ detil + ", office=" + office + ", qy=" + qy + ", hj=" + hj
				+ ", keeper=" + keeper + ", checkstate=" + checkstate
				+ ", operation=" + operation + ", assetInfoId=" + assetInfoId
				+ ", assetChecklistId=" + assetChecklistId
				+ ", assetCheckplanId=" + assetCheckplanId + "]";
	}
	
}
