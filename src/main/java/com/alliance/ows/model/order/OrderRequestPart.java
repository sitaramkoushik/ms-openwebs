/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.order;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import com.alliance.ows.model.Price;
import com.alliance.ows.model.Status;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OrderRequestPart implements Serializable {

	private static final long serialVersionUID = 260121811400409074L;
	private int line;
	private String part;
	private String lineCode;
	private String orderNumber;
	private String desc;
	private Vector<SelectOption> locations;
	private int selLoc;
	private Status status;
	private Set<Integer> origline;
	private long originalQty;
	private boolean active;
	private long partId = 0;
	private String brand;
	private boolean found;
	private long added_to_cart;
	private long brand_id;
	private String orderMessage = "";
	private long qty;
	private String packCode = "";
	private int packUnits = 0;
	private Price price100 = new Price();
	private boolean unavailable;
	private long orderedQuantity;
	private String partType = "";
	private boolean visible = true;
	private Set<Integer> partlookupType = new HashSet<Integer>();
	private boolean recheck = false;
	private String sourceType = "Miscellaneous";

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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public void setLocations(Vector<SelectOption> locations) {
		this.locations = locations;
	}

	public int getSelLoc() {
		return selLoc;
	}

	public void setSelLoc(int selLoc) {
		this.selLoc = selLoc;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<Integer> getOrigline() {
		return origline;
	}

	public void setOrigline(Set<Integer> origline) {
		this.origline = origline;
	}

	public long getOriginalQty() {
		return originalQty;
	}

	public void setOriginalQty(long originalQty) {
		this.originalQty = originalQty;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public long getAdded_to_cart() {
		return added_to_cart;
	}

	public void setAdded_to_cart(long added_to_cart) {
		this.added_to_cart = added_to_cart;
	}

	public long getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(long brand_id) {
		this.brand_id = brand_id;
	}

	public String getOrderMessage() {
		return orderMessage;
	}

	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}

	public long getQty() {
		return qty;
	}

	public void setQty(long qty) {
		this.qty = qty;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
	}

	public int getPackUnits() {
		return packUnits;
	}

	public void setPackUnits(int packUnits) {
		this.packUnits = packUnits;
	}

	public Price getPrice100() {
		return price100;
	}

	public void setPrice100(Price price100) {
		this.price100 = price100;
	}

	public boolean isUnavailable() {
		return unavailable;
	}

	public void setUnavailable(boolean unavailable) {
		this.unavailable = unavailable;
	}

	public long getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(long orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Set<Integer> getPartlookupType() {
		return partlookupType;
	}

	public void setPartlookupType(Set<Integer> partlookupType) {
		this.partlookupType = partlookupType;
	}

	public boolean isRecheck() {
		return recheck;
	}

	public void setRecheck(boolean recheck) {
		this.recheck = recheck;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
}