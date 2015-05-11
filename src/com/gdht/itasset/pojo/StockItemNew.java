package com.gdht.itasset.pojo;

import java.util.Date;

public class StockItemNew {
	private String assetChecklistId;// 盘点清单表主键，用于更改盘点状态
	private String assetCheckplanId;// 盘点计划ID,用于盘盈新增
	private String assetInfoId;// 资产基本信息表主键，用于调拨
	private String assetName;// 资产名称
	private String classify;// 资产分类
	private String type;// 资产类别
	private String rfidnumber;// rfid标签号
	private String barnumber;// 条形码标签号
	private String qrnumber;// 二维码标签号
	private String brand;// 品牌
	private String model;// 型号
	private int usetype;// 资产状态(1库存备用 2.在运 3.退役)
	private int checkstate;// 盘点状态
	private String dept;// 部门
	private String detil;// 盘点状态改变原因
	private String id;// 资产id主键
	private String keeper;// 责任人
	private String name;// 名称
	private String office;// 办公室
	private String warehouseArea;//仓库区域
	private String goodsShelves;//所在货架
	private Date registerdate;// 盘点日期
	private String registrant;// 盘点人
	private String rfidLabelnum;// RFID标签编号
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
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
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
	public String getRfidnumber() {
		return rfidnumber;
	}
	public void setRfidnumber(String rfidnumber) {
		this.rfidnumber = rfidnumber;
	}
	public String getBarnumber() {
		return barnumber;
	}
	public void setBarnumber(String barnumber) {
		this.barnumber = barnumber;
	}
	public String getQrnumber() {
		return qrnumber;
	}
	public void setQrnumber(String qrnumber) {
		this.qrnumber = qrnumber;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getUsetype() {
		return usetype;
	}
	public void setUsetype(int usetype) {
		this.usetype = usetype;
	}
	public int getCheckstate() {
		return checkstate;
	}
	public void setCheckstate(int checkstate) {
		this.checkstate = checkstate;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDetil() {
		return detil;
	}
	public void setDetil(String detil) {
		this.detil = detil;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKeeper() {
		return keeper;
	}
	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getWarehouseArea() {
		return warehouseArea;
	}
	public void setWarehouseArea(String warehouseArea) {
		this.warehouseArea = warehouseArea;
	}
	public String getGoodsShelves() {
		return goodsShelves;
	}
	public void setGoodsShelves(String goodsShelves) {
		this.goodsShelves = goodsShelves;
	}
	public Date getRegisterdate() {
		return registerdate;
	}
	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}
	public String getRegistrant() {
		return registrant;
	}
	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}
	public String getRfidLabelnum() {
		return rfidLabelnum;
	}
	public void setRfidLabelnum(String rfidLabelnum) {
		this.rfidLabelnum = rfidLabelnum;
	}
	
}
