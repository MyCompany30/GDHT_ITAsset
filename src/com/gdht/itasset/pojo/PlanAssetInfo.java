package com.gdht.itasset.pojo;

import java.io.Serializable;

/**
 * 移动巡检实体类
 * @author dongyang 2015年4月17日 下午5:03:31	
 * @update
 * @copyright 北京国电海通科技发展有限公司
 * @version 1.0.0
 */
public class PlanAssetInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2010001246870410672L;
	private String assetName;// 资产名称
	private String rfidnumber;// rfid标签号
	private String barnumber;// 条形码标签号
	private String qrnumber;// 二维码标签号
	private String usetype;// 资产状态(1库存备用 2.在运)
	private String dept;// 部门
	private String office;// 办公室
	private String warehouseArea;//仓库区域
	private String goodsShelves;//区域货架
	private String id;// 资产id主键
	
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

	public String getRfidnumber() {
		return rfidnumber;
	}

	public void setRfidnumber(String rfidnumber) {
		this.rfidnumber = rfidnumber;
	}

	public String getUsetype() {
		return usetype;
	}

	public void setUsetype(String usetype) {
		this.usetype = usetype;
	}

	public String getAssetName() {
		return assetName;
	}

	public String getDept() {
		return dept;
	}


	public String getId() {
		return id;
	}

	public String getOffice() {
		return office;
	}

	public void setAssetName(String assetName) {
		if (assetName.trim().equals(""))
			assetName = null;
		this.assetName = assetName == null ? null : assetName.trim();
	}





	public void setDept(String dept) {
		if (dept.trim().equals(""))
			dept = null;
		this.dept = dept == null ? null : dept.trim();
	}


	public void setId(String id) {
		if (id.trim().equals(""))
			id = null;
		this.id = id == null ? null : id.trim();
	}



	public void setOffice(String office) {
		if (office.trim().equals(""))
			office = null;
		this.office = office == null ? null : office.trim();
	}




}
