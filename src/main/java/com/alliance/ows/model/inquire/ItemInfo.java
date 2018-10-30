package com.alliance.ows.model.inquire;

public class ItemInfo {
	private CatalogInfo catalogInfo;
	private ManufacturerInfo manufacturerInfo;
	private SizeInfo sizeInfo;
	private String speedRating;
	private String loadRating;

	public String getSpeedRating() {
		return speedRating;
	}

	public void setSpeedRating(String speedRating) {
		this.speedRating = speedRating;
	}

	public String getLoadRating() {
		return loadRating;
	}

	public void setLoadRating(String loadRating) {
		this.loadRating = loadRating;
	}

	public SizeInfo getSizeInfo() {
		return sizeInfo;
	}

	public void setSizeInfo(SizeInfo sizeInfo) {
		this.sizeInfo = sizeInfo;
	}

	public CatalogInfo getCatalogInfo() {
		return catalogInfo;
	}

	public void setCatalogInfo(CatalogInfo catalogInfo) {
		this.catalogInfo = catalogInfo;
	}

	public ManufacturerInfo getManufacturerInfo() {
		return manufacturerInfo;
	}

	public void setManufacturerInfo(ManufacturerInfo manufacturerInfo) {
		this.manufacturerInfo = manufacturerInfo;
	}
}
