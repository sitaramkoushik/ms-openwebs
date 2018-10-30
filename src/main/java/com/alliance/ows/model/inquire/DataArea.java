package com.alliance.ows.model.inquire;

public class DataArea {
	private String addAttrName;
	public String getAddAttrName() {
		return addAttrName;
	}

	public void setAddAttrName(String addAttrName) {
		this.addAttrName = addAttrName;
	}

	public String getAddAttrVal() {
		return addAttrVal;
	}

	public void setAddAttrVal(String addAttrVal) {
		this.addAttrVal = addAttrVal;
	}

	private String addAttrVal ;
	private RequestForQuote requestForQuote;
	

	public RequestForQuote getRequestForQuote() {
		return requestForQuote;
	}

	public void setRequestForQuote(RequestForQuote requestForQuote) {
		this.requestForQuote = requestForQuote;
	}
}
