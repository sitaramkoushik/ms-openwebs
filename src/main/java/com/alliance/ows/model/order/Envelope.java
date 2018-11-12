package com.alliance.ows.model.order;

public class Envelope {
	private OrdHeader header;
	private OrdBody body;
	private String envAttrName;
	private String envAttrValue;
	private String envAttrRevName;
	private String envAttrRevValue;

	public String getEnvAttrName() {
		return envAttrName;
	}

	public void setEnvAttrName(String envAttrName) {
		this.envAttrName = envAttrName;
	}

	public String getEnvAttrValue() {
		return envAttrValue;
	}

	public void setEnvAttrValue(String envAttrValue) {
		this.envAttrValue = envAttrValue;
	}

	public String getEnvAttrRevName() {
		return envAttrRevName;
	}

	public void setEnvAttrRevName(String envAttrRevName) {
		this.envAttrRevName = envAttrRevName;
	}

	public String getEnvAttrRevValue() {
		return envAttrRevValue;
	}

	public void setEnvAttrRevValue(String envAttrRevValue) {
		this.envAttrRevValue = envAttrRevValue;
	}

	public OrdHeader getHeader() {
		return header;
	}

	public void setHeader(OrdHeader header) {
		this.header = header;
	}

	public OrdBody getBody() {
		return body;
	}

	public void setBody(OrdBody body) {
		this.body = body;
	}
}
