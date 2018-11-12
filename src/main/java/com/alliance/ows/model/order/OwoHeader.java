package com.alliance.ows.model.order;

public class OwoHeader {
	private DocumentIds documents;
	private Parties parties;
	private OrderInfo orderInfo;

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public DocumentIds getDocuments() {
		return documents;
	}

	public void setDocuments(DocumentIds documents) {
		this.documents = documents;
	}

	public Parties getParties() {
		return parties;
	}

	public void setParties(Parties parties) {
		this.parties = parties;
	}
}
