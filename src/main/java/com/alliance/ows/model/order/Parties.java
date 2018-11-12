package com.alliance.ows.model.order;

public class Parties {
	private CustomerParty customerParty;

	public SupplierParty getSupplierParty() {
		return supplierParty;
	}

	public void setSupplierParty(SupplierParty supplierParty) {
		this.supplierParty = supplierParty;
	}

	public ShipToParty getShipToParty() {
		return shipToParty;
	}

	public void setShipToParty(ShipToParty shipToParty) {
		this.shipToParty = shipToParty;
	}

	private SupplierParty supplierParty;
	private ShipToParty shipToParty;

	public CustomerParty getCustomerParty() {
		return customerParty;
	}

	public void setCustomerParty(CustomerParty customerParty) {
		this.customerParty = customerParty;
	}
}
