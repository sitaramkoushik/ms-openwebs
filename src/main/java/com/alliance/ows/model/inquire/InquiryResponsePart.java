/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.inquire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.alliance.ows.model.Price;
import com.alliance.ows.model.Status;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryResponsePart implements Serializable {

	private static final long serialVersionUID = 8672800485794850935L;
	private int line;
	private String part;
	private String lineCode;
	private String description;
	private Vector<SelectOption> locations = new Vector<>();
	private Status status;
	private long partId = 0;
	private String brandImage;
	private String partImage;
	private long qty = 0;
	private Price price100 = new Price();
	private List<Integer> partlookupType = new ArrayList<Integer>();
	private long perCarQty = 0;
	private Vector<InquiryResponsePart> alternateParts = new Vector<InquiryResponsePart>();

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
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public Vector<SelectOption> getLocations() {
		return locations;
	}

	public SelectOption getLocation(int selLoc) {
		SelectOption so = new SelectOption();
		so.setNetwork(selLoc);
		int index = locations.indexOf(so);
		if (index >= 0) {
			so = locations.get(index);
		}
		return so;
	}

	public Price getPrice(int seqNo) {
		SelectOption so = getLocation(seqNo);
		Price p = so.getPrice();
		if (p == null) {
			if (so.getRealNetwork() > 0) {
				p = getLocation(so.getRealNetwork()).getPrice();
				if (p != null) {
					so.setPrice(p);
				}
			}
		}
		if (p == null) {
			p = price100;
			if (p != null) {
				so.setPrice(p);
			}
		}
		return p;
	}

	public void setLocations(Vector<SelectOption> locations) {
		this.locations = locations;
	}

	public void addLocations(SelectOption locations) {
		this.locations.add(locations);
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getBrandImage() {
		return brandImage;
	}

	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}

	public String getPartImage() {
		return partImage;
	}

	public void setPartImage(String partImage) {
		this.partImage = partImage;
	}

	public long getQty() {
		return qty;
	}

	public boolean equals(Object p) {
		boolean equality = false;

		if (p == null) {
			equality = false;
		} else if (this == p) {
			equality = true;
		} else if (!(p instanceof InquiryResponsePart)) {
			equality = false;
		} else {
			InquiryResponsePart that = (InquiryResponsePart) p;

			// Same part-number and line-code determine equality
			equality = ((this.line == that.line && this.line != 0))
							|| (this.partId == that.partId && this.partId != 0)
							|| (this.part.trim().equalsIgnoreCase(that.part.trim()) && this.lineCode.trim().equalsIgnoreCase(that.lineCode.trim())
											&& this.description.trim().equalsIgnoreCase(that.description.trim()) || ((this.description.trim().isEmpty() || that.description
											.trim().isEmpty()) && (this.part.trim().equalsIgnoreCase(that.part.trim()) && this.lineCode.trim()
											.equalsIgnoreCase(that.lineCode.trim()))));
		}
		return equality;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public void updateLocationBuyQty() {
		for (SelectOption location : locations) {
			location.updateBuyQty((int) getQty());
		}
	}

	public Price getPrice100() {
		return price100;
	}

	public void setPrice100(Price price100) {
		this.price100 = price100;
	}

	public long getPerCarQty() {
		return perCarQty;
	}

	public void setPerCarQty(long perCarQty) {
		this.perCarQty = perCarQty;
	}

	public InquiryResponsePart() {

	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Integer> getPartlookupType() {
		return partlookupType;
	}

	public void setPartlookupType(List<Integer> partlookupType) {
		this.partlookupType = partlookupType;
	}

	public Vector<InquiryResponsePart> getAlternateParts() {
		return alternateParts;
	}

	public void setAlternateParts(Vector<InquiryResponsePart> alternateParts) {
		this.alternateParts = alternateParts;
	}

}