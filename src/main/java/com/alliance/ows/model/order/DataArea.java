package com.alliance.ows.model.order;

public class DataArea {
	private String processAttrName ;
	private String processAttrValue ;
	public String getProcessAttrValue() {
		return processAttrValue;
	}
	public void setProcessAttrValue(String processAttrValue) {
		this.processAttrValue = processAttrValue;
	}
	private PurchaseOrder purchaseOrder;
	public String getProcessAttrName() {
		return processAttrName;
	}
	public void setProcessAttrName(String processAttrName) {
		this.processAttrName = processAttrName;
	}
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
}
