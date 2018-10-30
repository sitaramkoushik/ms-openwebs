package com.alliance.ows.model.inquire;

public class Envelope {
	private Header header;
	private Body body;
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

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
