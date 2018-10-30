package com.alliance.ows.model.inquire;

public class OrderItem {
	private ItemIds itemId;
	private String itemType;
	private String requestLineGUID;
	private ItemInfo itemInfo;
	private SizeInfo sizeInfo;
	private String SpeedRating;
	private String LoadRating;
	private QuantityInfo quantityInfo;

	public QuantityInfo getQuantityInfo() {
		return quantityInfo;
	}

	public void setQuantityInfo(QuantityInfo quantityInfo) {
		this.quantityInfo = quantityInfo;
	}

	public SizeInfo getSizeInfo() {
		return sizeInfo;
	}

	public void setSizeInfo(SizeInfo sizeInfo) {
		this.sizeInfo = sizeInfo;
	}

	public String getSpeedRating() {
		return SpeedRating;
	}

	public void setSpeedRating(String speedRating) {
		SpeedRating = speedRating;
	}

	public String getLoadRating() {
		return LoadRating;
	}

	public void setLoadRating(String loadRating) {
		LoadRating = loadRating;
	}

	public ItemIds getItemId() {
		return itemId;
	}

	public void setItemId(ItemIds itemId) {
		this.itemId = itemId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getRequestLineGUID() {
		return requestLineGUID;
	}

	public void setRequestLineGUID(String requestLineGUID) {
		this.requestLineGUID = requestLineGUID;
	}

	public ItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}
}
