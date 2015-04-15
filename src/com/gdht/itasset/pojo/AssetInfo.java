package com.gdht.itasset.pojo; 

import java.math.BigDecimal;
import java.util.Date;

public class AssetInfo {
	private String assetName;

	private String assetType;

	private String brand;

	private String buyDate;

	private BigDecimal buyPrice;

	private String detail;

	private String id;

	private int lifetime;

	private String model;

	private String name;

	private BigDecimal depreciationRate;

	private String registerdate;

	private String registrant;

	private String rfidLabelnum;

	private String supplier;

	private String units;

	private Integer warrantyPeriod;

	private String dept;

	private String office;

	private String keeper;

	private String assetInfoId2;
	private BigDecimal percent;// 当前剩余百分比
	private String money;// 当前剩余金额

	/**
	 * 新增基础信息
	 */
	private String yjdw;// 一級單位

	private String ejdw;// 二級單位

	private String sjdw;// 三級單位

	private String zzs;// 製造商

	private String xl;// 系列

	private Date tyrq;// 投运日期

	private String yt;// 用途

	private String gwbh;// 国网编号

	private String bz;// 备注

	private String cgfs;// 采购方式

	private String cqgs;// 产权归属

	private String cghtbh;// 采购合同编号

	private String gyslxr;// 供应商联系人

	private String gyslxdh;// 供应商联系电话

	private String ccbh;// 出厂编号

	private Date ccrq;// 出厂日期

	private Date shfwdqsj;// 售后服务到期时间

	private String fwhtbh;// 服务合同编号

	private String fws;// 服务商

	private String fwjb;// 服务级别

	private Date fwksrq;// 服务开始日期

	private Date fwjsrq;// 服务到期日期

	private String fwslxr;// 服务商联系人

	private String fwslxdh;// 服务商联系电话

	private String shzt;// 审核状态

	private Date zhshrq;// 最后审核日期

	private String ywbm;// 运维部门

	private String ywzrr;// 运维责任人

	private String ywlxdh;// 运维联系电话
	/******************** tab3 **********************/

	private String sybgbm;// 使用保管部门

	private String swglbm;// 实物管理部门

	private String sbzjfs;// 设备增加方式

	private String sbbdfs;// 设备变动方式

	private String zzgj;// 制造国家

	private String sbcfdd;// 设备存放地点

	private String zcxz;// 资产性质

	private String erpWhgc;// ERP维护工厂

	private String sftbzErp;// 是否同步至ERP

	private String erpWbs;// ERP_WBS

	private String erpGnwz;// ERP功能位置

	private String erpZcbm;// ERP资产编码

	private String erpSbtzbm;// ERP设备台账编码

	private String xzbs;// 线站标识

	private Date zhtbsj;// 最后同步时间

	private String tbzt;// 同步状态

	/********************** setter/getter **********************/
	public String getYjdw() {
		return yjdw;
	}

	public void setYjdw(String yjdw) {
		this.yjdw = yjdw;
	}

	public String getEjdw() {
		return ejdw;
	}

	public void setEjdw(String ejdw) {
		this.ejdw = ejdw;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getZzs() {
		return zzs;
	}

	public void setZzs(String zzs) {
		this.zzs = zzs;
	}

	public String getXl() {
		return xl;
	}

	public void setXl(String xl) {
		this.xl = xl;
	}

	public Date getTyrq() {
		return tyrq;
	}

	public void setTyrq(Date tyrq) {
		this.tyrq = tyrq;
	}

	public String getYt() {
		return yt;
	}

	public void setYt(String yt) {
		this.yt = yt;
	}

	public String getGwbh() {
		return gwbh;
	}

	public void setGwbh(String gwbh) {
		this.gwbh = gwbh;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getCgfs() {
		return cgfs;
	}

	public void setCgfs(String cgfs) {
		this.cgfs = cgfs;
	}

	public String getCqgs() {
		return cqgs;
	}

	public void setCqgs(String cqgs) {
		this.cqgs = cqgs;
	}

	public String getCghtbh() {
		return cghtbh;
	}

	public void setCghtbh(String cghtbh) {
		this.cghtbh = cghtbh;
	}

	public String getGyslxr() {
		return gyslxr;
	}

	public void setGyslxr(String gyslxr) {
		this.gyslxr = gyslxr;
	}

	public String getGyslxdh() {
		return gyslxdh;
	}

	public void setGyslxdh(String gyslxdh) {
		this.gyslxdh = gyslxdh;
	}

	public String getCcbh() {
		return ccbh;
	}

	public void setCcbh(String ccbh) {
		this.ccbh = ccbh;
	}

	public Date getCcrq() {
		return ccrq;
	}

	public void setCcrq(Date ccrq) {
		this.ccrq = ccrq;
	}

	public Date getShfwdqsj() {
		return shfwdqsj;
	}

	public void setShfwdqsj(Date shfwdqsj) {
		this.shfwdqsj = shfwdqsj;
	}

	public String getFwhtbh() {
		return fwhtbh;
	}

	public void setFwhtbh(String fwhtbh) {
		this.fwhtbh = fwhtbh;
	}

	public String getFws() {
		return fws;
	}

	public void setFws(String fws) {
		this.fws = fws;
	}

	public String getFwjb() {
		return fwjb;
	}

	public void setFwjb(String fwjb) {
		this.fwjb = fwjb;
	}

	public Date getFwksrq() {
		return fwksrq;
	}

	public void setFwksrq(Date fwksrq) {
		this.fwksrq = fwksrq;
	}

	public Date getFwjsrq() {
		return fwjsrq;
	}

	public void setFwjsrq(Date fwjsrq) {
		this.fwjsrq = fwjsrq;
	}

	public String getFwslxr() {
		return fwslxr;
	}

	public void setFwslxr(String fwslxr) {
		this.fwslxr = fwslxr;
	}

	public String getFwslxdh() {
		return fwslxdh;
	}

	public void setFwslxdh(String fwslxdh) {
		this.fwslxdh = fwslxdh;
	}

	public String getShzt() {
		return shzt;
	}

	public void setShzt(String shzt) {
		this.shzt = shzt;
	}

	public Date getZhshrq() {
		return zhshrq;
	}

	public void setZhshrq(Date zhshrq) {
		this.zhshrq = zhshrq;
	}

	public String getYwbm() {
		return ywbm;
	}

	public void setYwbm(String ywbm) {
		this.ywbm = ywbm;
	}

	public String getYwzrr() {
		return ywzrr;
	}

	public void setYwzrr(String ywzrr) {
		this.ywzrr = ywzrr;
	}

	public String getYwlxdh() {
		return ywlxdh;
	}

	public void setYwlxdh(String ywlxdh) {
		this.ywlxdh = ywlxdh;
	}

	public String getSybgbm() {
		return sybgbm;
	}

	public void setSybgbm(String sybgbm) {
		this.sybgbm = sybgbm;
	}

	public String getSwglbm() {
		return swglbm;
	}

	public void setSwglbm(String swglbm) {
		this.swglbm = swglbm;
	}

	public String getSbzjfs() {
		return sbzjfs;
	}

	public void setSbzjfs(String sbzjfs) {
		this.sbzjfs = sbzjfs;
	}

	public String getSbbdfs() {
		return sbbdfs;
	}

	public void setSbbdfs(String sbbdfs) {
		this.sbbdfs = sbbdfs;
	}

	public String getZzgj() {
		return zzgj;
	}

	public void setZzgj(String zzgj) {
		this.zzgj = zzgj;
	}

	public String getSbcfdd() {
		return sbcfdd;
	}

	public void setSbcfdd(String sbcfdd) {
		this.sbcfdd = sbcfdd;
	}

	public String getZcxz() {
		return zcxz;
	}

	public void setZcxz(String zcxz) {
		this.zcxz = zcxz;
	}

	public String getErpWhgc() {
		return erpWhgc;
	}

	public void setErpWhgc(String erpWhgc) {
		this.erpWhgc = erpWhgc;
	}

	public String getSftbzErp() {
		return sftbzErp;
	}

	public void setSftbzErp(String sftbzErp) {
		this.sftbzErp = sftbzErp;
	}

	public String getErpWbs() {
		return erpWbs;
	}

	public void setErpWbs(String erpWbs) {
		this.erpWbs = erpWbs;
	}

	public String getErpGnwz() {
		return erpGnwz;
	}

	public void setErpGnwz(String erpGnwz) {
		this.erpGnwz = erpGnwz;
	}

	public String getErpZcbm() {
		return erpZcbm;
	}

	public void setErpZcbm(String erpZcbm) {
		this.erpZcbm = erpZcbm;
	}

	public String getErpSbtzbm() {
		return erpSbtzbm;
	}

	public void setErpSbtzbm(String erpSbtzbm) {
		this.erpSbtzbm = erpSbtzbm;
	}

	public String getXzbs() {
		return xzbs;
	}

	public void setXzbs(String xzbs) {
		this.xzbs = xzbs;
	}

	public Date getZhtbsj() {
		return zhtbsj;
	}

	public void setZhtbsj(Date zhtbsj) {
		this.zhtbsj = zhtbsj;
	}

	public String getTbzt() {
		return tbzt;
	}

	public void setTbzt(String tbzt) {
		this.tbzt = tbzt;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAssetName() {
		return assetName;
	}

	public String getAssetType() {
		return assetType;
	}

	public String getBrand() {
		return brand;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public String getDetail() {
		return detail;
	}

	public String getId() {
		return id;
	}

	public int getLifetime() {
		return lifetime;
	}

	public String getModel() {
		return model;
	}

	public String getName() {
		return name;
	}

	public String getRegisterdate() {
		return registerdate;
	}

	public String getRegistrant() {
		return registrant;
	}

	public String getRfidLabelnum() {
		return rfidLabelnum;
	}

	public String getSupplier() {
		return supplier;
	}

	public String getUnits() {
		return units;
	}

	public Integer getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setAssetName(String assetName) {
		if (assetName.trim().equals("")) {
			assetName = null;
		}
		this.assetName = assetName == null ? null : assetName.trim();
	}

	public void setAssetType(String assetType) {
		if (assetType.trim().equals("")) {
			assetType = null;
		}
		this.assetType = assetType == null ? null : assetType.trim();
	}

	public void setBrand(String brand) {
		if (brand.trim().equals("")) {
			brand = null;
		}
		this.brand = brand == null ? null : brand.trim();
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public void setDetail(String detail) {
		if (detail.trim().equals("")) {
			detail = null;
		}
		this.detail = detail == null ? null : detail.trim();
	}

	public void setId(String id) {
		if (id.trim().equals("")) {
			id = null;
		}
		this.id = id == null ? null : id.trim();
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public void setModel(String model) {
		if (model.trim().equals("")) {
			model = null;
		}
		this.model = model == null ? null : model.trim();
	}

	public void setName(String name) {
		if (name.trim().equals("")) {
			name = null;
		}
		this.name = name == null ? null : name.trim();
	}

	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}

	public void setRegistrant(String registrant) {
		if (registrant.trim().equals("")) {
			registrant = null;
		}
		this.registrant = registrant == null ? null : registrant.trim();
	}

	public void setRfidLabelnum(String rfidLabelnum) {
		if (rfidLabelnum.trim().equals("")) {
			rfidLabelnum = null;
		}
		this.rfidLabelnum = rfidLabelnum == null ? null : rfidLabelnum.trim();
	}

	public void setSupplier(String supplier) {
		if (supplier.trim().equals("")) {
			supplier = null;
		}
		this.supplier = supplier == null ? null : supplier.trim();
	}

	public void setUnits(String units) {
		if (units.trim().equals("")) {
			units = null;
		}
		this.units = units == null ? null : units.trim();
	}

	public void setWarrantyPeriod(Integer warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getKeeper() {
		return keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}

	public String getassetInfoId2() {
		return assetInfoId2;
	}

	public void setassetInfoId2(String assetInfoId2) {
		this.assetInfoId2 = assetInfoId2;
	}

	public BigDecimal getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(BigDecimal depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

}