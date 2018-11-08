package com.alliance.ows.model.inquire;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryRequestData implements Serializable {

	private static final long serialVersionUID = -509201867004469780L;
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
	private List<InquiryRequestPart> parts;

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

	public List<InquiryRequestPart> getParts() {
		return parts;
	}

	public void setParts(List<InquiryRequestPart> parts) {
		this.parts = parts;
	}
}