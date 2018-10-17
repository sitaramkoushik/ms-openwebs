package com.alliance.ows.model.order;

import java.io.Serializable;
import java.util.List;
import com.alliance.ows.model.Vehicle;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OrderRequestData implements Serializable {

	private static final long serialVersionUID = -6097694849498022945L;
	private List<OrderRequestPart> parts;
	private String token;
	private String searchType;
	private String lookupType;
	private String scat;
	private String sindex;
	private String stype;
	private String partnerid;
	private String service;
	private String start;
	private String lookupInUse;
	private boolean testOrder;
	private Vehicle vehicle;
	private String comment;
	private String poNumber;
	private String description;
	private int quoteId;
	private String deliveryMethod;

	public List<OrderRequestPart> getParts() {
		return parts;
	}

	public void setParts(List<OrderRequestPart> parts) {
		this.parts = parts;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getLookupType() {
		return lookupType;
	}

	public void setLookupType(String lookupType) {
		this.lookupType = lookupType;
	}

	public String getScat() {
		return scat;
	}

	public void setScat(String scat) {
		this.scat = scat;
	}

	public String getSindex() {
		return sindex;
	}

	public void setSindex(String sindex) {
		this.sindex = sindex;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLookupInUse() {
		return lookupInUse;
	}

	public void setLookupInUse(String lookupInUse) {
		this.lookupInUse = lookupInUse;
	}

	public boolean isTestOrder() {
		return testOrder;
	}

	public void setTestOrder(boolean isTestOrder) {
		this.testOrder = isTestOrder;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(int quoteId) {
		this.quoteId = quoteId;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

}