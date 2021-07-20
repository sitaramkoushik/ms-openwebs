package com.alliance.ows.model.inquire;

import com.alliance.ows.model.order.OrdBody;
import com.alliance.ows.model.order.OrdHeader;

public class Envelope {
	private Header header;
	private Body body;
	private OrdBody ordBody;
	private OrdHeader ordHeader;
	private String envAttrOweName;
	private String envAttrOweValue;
	private String envAttrRevName;
	private String envAttrRevValue;
	private String envAttrOaName;
	private String envAttrOaValue;
	private String envAttrOwoName;
	private String envAttrOwoValue;

	public OrdBody getOrdBody() {
		return ordBody;
	}

	public void setOrdBody(OrdBody ordBody) {
		this.ordBody = ordBody;
	}

	public OrdHeader getOrdHeader() {
		return ordHeader;
	}

	public void setOrdHeader(OrdHeader ordHeader) {
		this.ordHeader = ordHeader;
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

	public String getEnvAttrOaName() {
		return envAttrOaName;
	}

	public void setEnvAttrOaName(String envAttrOaName) {
		this.envAttrOaName = envAttrOaName;
	}

	public String getEnvAttrOaValue() {
		return envAttrOaValue;
	}

	public void setEnvAttrOaValue(String envAttrOaValue) {
		this.envAttrOaValue = envAttrOaValue;
	}

	public String getEnvAttrOweName() {
		return envAttrOweName;
	}

	public void setEnvAttrOweName(String envAttrOweName) {
		this.envAttrOweName = envAttrOweName;
	}

	public String getEnvAttrOweValue() {
		return envAttrOweValue;
	}

	public void setEnvAttrOweValue(String envAttrOweValue) {
		this.envAttrOweValue = envAttrOweValue;
	}

	public String getEnvAttrOwoName() {
		return envAttrOwoName;
	}

	public void setEnvAttrOwoName(String envAttrOwoName) {
		this.envAttrOwoName = envAttrOwoName;
	}

	public String getEnvAttrOwoValue() {
		return envAttrOwoValue;
	}

	public void setEnvAttrOwoValue(String envAttrOwoValue) {
		this.envAttrOwoValue = envAttrOwoValue;
	}
}