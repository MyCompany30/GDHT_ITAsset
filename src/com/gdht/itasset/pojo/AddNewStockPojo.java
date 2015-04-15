package com.gdht.itasset.pojo;

public class AddNewStockPojo {
	private String assetType = null;
	private String assetName = null;
	private String name = null;
	private String brand = null;
	private String model = null;
	private String units = null;
	private String buyDate = null;
	private String buyPrice = null;
	private String supplier = null;
	private String warrantyPeriod = null;
	private String lifetime = null;
	private String keeper = null;
	private String depreciationRate = null;
	public String getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(String depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	private String dept = null;
	private String office = null;
	private String rfidLabelnumber = null;

	public String getRfidLabelnumber() {
		if(rfidLabelnumber==null)
			return "";
		return rfidLabelnumber;
	}

	public void setRfidLabelnumber(String rfidLabelnumber) {
		this.rfidLabelnumber = rfidLabelnumber;
	}

	public String getDetail() {
		if(detail==null)
			return "";
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	private String detail = null;

	public String getAssetType() {
		if(assetType==null)
			return "";
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetName() {
		if(assetName==null)
			return "";
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getName() {
		if(name==null)
			return "";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		if(brand==null)
			return "";
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		if(model==null)
			return "";
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUnits() {
		if(units==null)
			return "";
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getBuyDate() {
		if(buyDate==null)
			return "";
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getBuyPrice() {
		if(buyPrice==null)
			return "";
		return buyPrice;
	}

	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getSupplier() {
		if(supplier==null)
			return "";
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getWarrantyPeriod() {
		if(warrantyPeriod==null)
			return "";
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getLifetime() {
		if(lifetime==null)
			return "";
		return lifetime;
	}

	public void setLifetime(String lifetime) {
		this.lifetime = lifetime;
	}

	public String getKeeper() {
		if(keeper==null)
			return "";
		return keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}

	public String getDept() {
		if(dept==null)
			return "";
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getOffice() {
		if(office==null)
			return "";
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

}
