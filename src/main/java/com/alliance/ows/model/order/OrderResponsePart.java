/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.order;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OrderResponsePart implements Serializable {

	private static final long serialVersionUID = -6079673355891083632L;
	private int line;
	private String part;
	private String lineCode;
	private String desc;
	private Vector<SelectOption> locations = new Vector<SelectOption>();

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Vector<SelectOption> getLocations() {
		return locations;
	}

	public void setLocations(List<SelectOption> locs) {
		this.locations.addAll(locs);
	}

}