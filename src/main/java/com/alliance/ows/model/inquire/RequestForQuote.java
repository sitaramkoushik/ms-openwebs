package com.alliance.ows.model.inquire;

import java.util.List;

public class RequestForQuote {
	private OaHeader oaHeader;
	private List<Line> line;

	public List<Line> getLine() {
		return line;
	}

	public void setLine(List<Line> line) {
		this.line = line;
	}

	public OaHeader getOaHeader() {
		return oaHeader;
	}

	public void setOaHeader(OaHeader oaHeader) {
		this.oaHeader = oaHeader;
	}

}
