package com.alliance.ows.model.inquire;

public class QuantityInfo {
	private String RequestedQuantity;
	private String uomAttrName;
	private String uomAttrValue;
	public String getUomAttrName() {
		return uomAttrName;
	}

	public void setUomAttrName(String uomAttrName) {
		this.uomAttrName = uomAttrName;
	}

	public String getUomAttrValue() {
		return uomAttrValue;
	}

	public void setUomAttrValue(String uomAttrValue) {
		this.uomAttrValue = uomAttrValue;
	}

	public String getRequestedQuantity() {
		return RequestedQuantity;
	}

	public void setRequestedQuantity(String requestedQuantity) {
		RequestedQuantity = requestedQuantity;
	}
}
