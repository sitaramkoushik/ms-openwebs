package com.alliance.ows.model.order;

import java.util.List;

public class PurchaseOrder {
	private OwoHeader owoHeader;
	private List<Line> line ;
	public OwoHeader getOwoHeader() {
		return owoHeader;
	}
	public void setOwoHeader(OwoHeader owoHeader) {
		this.owoHeader = owoHeader;
	}
	public List<Line> getLine() {
		return line;
	}
	public void setLine(List<Line> line) {
		this.line = line;
	}
	
}
