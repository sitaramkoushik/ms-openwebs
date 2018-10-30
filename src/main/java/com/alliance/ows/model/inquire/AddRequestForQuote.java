package com.alliance.ows.model.inquire;

public class AddRequestForQuote {
	private ApplicationArea applicationArea;
	private DataArea dataArea;
	private String rfqAttrName;
	private String rfqAttrValue;
	private String rfqAttrOaName;
	private String rfqAttrOaValue;
	private String rfqAttrRevName;
	private String rfqAttrRevValue;
	public String getRfqAttrRevName() {
		return rfqAttrRevName;
	}

	public void setRfqAttrRevName(String rfqAttrRevName) {
		this.rfqAttrRevName = rfqAttrRevName;
	}

	public String getRfqAttrRevValue() {
		return rfqAttrRevValue;
	}

	public void setRfqAttrRevValue(String rfqAttrRevValue) {
		this.rfqAttrRevValue = rfqAttrRevValue;
	}

	public String getRfqAttrName() {
		return rfqAttrName;
	}

	public void setRfqAttrName(String rfqAttrName) {
		this.rfqAttrName = rfqAttrName;
	}

	public String getRfqAttrValue() {
		return rfqAttrValue;
	}

	public void setRfqAttrValue(String rfqAttrValue) {
		this.rfqAttrValue = rfqAttrValue;
	}

	public String getRfqAttrOaName() {
		return rfqAttrOaName;
	}

	public void setRfqAttrOaName(String rfqAttrOaName) {
		this.rfqAttrOaName = rfqAttrOaName;
	}

	public String getRfqAttrOaValue() {
		return rfqAttrOaValue;
	}

	public void setRfqAttrOaValue(String rfqAttrOaValue) {
		this.rfqAttrOaValue = rfqAttrOaValue;
	}

	public ApplicationArea getApplicationArea() {
		return applicationArea;
	}

	public void setApplicationArea(ApplicationArea applicationArea) {
		this.applicationArea = applicationArea;
	}

	public DataArea getDataArea() {
		return dataArea;
	}

	public void setDataArea(DataArea dataArea) {
		this.dataArea = dataArea;
	}
}
