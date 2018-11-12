package com.alliance.ows.model.order;

import java.util.List;

public class ShippingAddress {
	private List<String> AddressLine ;
	private String city;
	private String stateOrProvince;
	private String postalCode ;
	
	public List<String> getAddressLine() {
		return AddressLine;
	}
	public void setAddressLine(List<String> addressLine) {
		AddressLine = addressLine;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateOrProvince() {
		return stateOrProvince;
	}
	public void setStateOrProvince(String stateOrProvince) {
		this.stateOrProvince = stateOrProvince;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
