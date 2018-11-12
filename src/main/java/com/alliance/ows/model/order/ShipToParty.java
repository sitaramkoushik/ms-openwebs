package com.alliance.ows.model.order;

public class ShipToParty {
	private PartyId partyId;
	private Business bussiness;
	private Addresses addresses;
	private String  name ;
	public Addresses getAddresses() {
		return addresses;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddresses(Addresses addresses) {
		this.addresses = addresses;
	}

	public PartyId getPartyId() {
		return partyId;
	}

	public void setPartyId(PartyId partyId) {
		this.partyId = partyId;
	}

	public Business getBussiness() {
		return bussiness;
	}

	public void setBussiness(Business bussiness) {
		this.bussiness = bussiness;
	}
}
