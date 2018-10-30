package com.alliance.ows.model.inquire;

public class Header {
	private EndPoints endPoints;
	private Properties properties;
	private SecurityInfo securityInfo;

	public EndPoints getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(EndPoints endPoints) {
		this.endPoints = endPoints;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public SecurityInfo getSecurityInfo() {
		return securityInfo;
	}

	public void setSecurityInfo(SecurityInfo securityInfo) {
		this.securityInfo = securityInfo;
	}
}
