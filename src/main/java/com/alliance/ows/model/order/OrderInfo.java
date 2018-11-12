package com.alliance.ows.model.order;

import java.util.List;

public class OrderInfo {
	private ShippingInfo shippingInfo;
	private String requestType;
	private String fillFlag;
	private List<String> comment;
	public ShippingInfo getShippingInfo() {
		return shippingInfo;
	}
	public void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getFillFlag() {
		return fillFlag;
	}
	public void setFillFlag(String fillFlag) {
		this.fillFlag = fillFlag;
	}
	public List<String> getComment() {
		return comment;
	}
	public void setComment(List<String> comment) {
		this.comment = comment;
	}
}
