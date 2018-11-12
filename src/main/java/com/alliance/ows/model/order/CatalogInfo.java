package com.alliance.ows.model.order;

public class CatalogInfo {
	private String CatalogName;
	private String CatalogVersion;
	private String CatalogPublisher;

	public String getCatalogName() {
		return CatalogName;
	}

	public void setCatalogName(String catalogName) {
		CatalogName = catalogName;
	}

	public String getCatalogVersion() {
		return CatalogVersion;
	}

	public void setCatalogVersion(String catalogVersion) {
		CatalogVersion = catalogVersion;
	}

	public String getCatalogPublisher() {
		return CatalogPublisher;
	}

	public void setCatalogPublisher(String catalogPublisher) {
		CatalogPublisher = catalogPublisher;
	}
}
