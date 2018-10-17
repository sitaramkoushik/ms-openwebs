/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.inquire;

import java.io.Serializable;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryRequestPart implements Serializable {

	private static final long serialVersionUID = 8772596154554778072L;
	private int line;
	private String part;
	private String lineCode;
	private String desc;
	private boolean active;
	private String brand;
	private long brand_id;
	private long qty;
	private String sourceType;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public long getBrandId() {
		return brand_id;
	}

	public void setBrandId(long brand_id) {
		this.brand_id = brand_id;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long current_qty) {
		this.qty = current_qty;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

}