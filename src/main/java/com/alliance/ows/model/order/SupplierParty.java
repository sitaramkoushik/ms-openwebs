package com.alliance.ows.model.order;

public class SupplierParty {
	private PartyId partyId;

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

	private Business bussiness;

}
