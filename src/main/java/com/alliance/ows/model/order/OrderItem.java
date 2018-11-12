package com.alliance.ows.model.order;

public class OrderItem {
	private ItemIds itemId;
	private String itemType;
	private String requestLineGUID;
	private ItemInfo itemInfo;
	private OrderItemInfo orderInfo;


	public OrderItemInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderItemInfo orderInfo) {
		this.orderInfo = orderInfo;
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
