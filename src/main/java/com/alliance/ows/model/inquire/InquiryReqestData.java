package com.alliance.ows.model.inquire;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryReqestData implements Serializable {

	private static final long serialVersionUID = 4549102110679275275L;
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

	public void setPart(List<InquiryRequestPart> part) {
		parts = part;
	}

	public void setlookupType(String lookupType) {
		this.lookupType = lookupType;
	}

	public void settoken(String token) {
		this.token = token;
	}

	public void setsearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setscat(String scat) {
		this.scat = scat;
	}

	public void setsindex(String sindex) {
		this.sindex = sindex;
	}

	public void setstype(String stype) {
		this.stype = stype;
	}

	public void setpartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public void setservice(String service) {
		this.service = service;
	}

	public void setstart(String start) {
		this.start = start;
	}

	public void setlookupInUse(String lookupInUse) {
		this.lookupInUse = lookupInUse;
	}

	public List<InquiryRequestPart> getPart() {
		return parts;
	}

	public String getlookupType() {
		return lookupType;
	}

	public String getpartnerid() {
		return partnerid;
	}

	public String getsearchType() {
		return searchType;
	}

	public String getscat() {
		return scat;
	}

	public String getsindex() {
		return sindex;
	}

	public String getstype() {
		return stype;
	}

	public String getservice() {
		return service;
	}

	public String getstart() {
		return start;
	}

	public String getlookupInUse() {
		return lookupInUse;
	}

	public String gettoken() {
		return token;
	}

}
