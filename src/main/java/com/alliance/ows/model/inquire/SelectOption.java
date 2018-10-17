/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.inquire;

import java.io.Serializable;
import java.util.Comparator;

import com.alliance.ows.model.Price;
import com.alliance.ows.model.Quantity;

/**
 *
 * @author Krishna Kumar
 */
public class SelectOption implements Serializable {

	private static final long serialVersionUID = -1922106258822597423L;
	private String display;
	private int network;
	private Quantity quantity;
	private Price price;
	private boolean selected;
	private boolean bulk;
	private int buyIncr = 1;
	private int minQty = 1;
	private int buyQty = 1;
	private int realNetwork;
	private String uOfM = "";
	private int originalLocation;
	private boolean virtual;
	private boolean selldirect;

	public SelectOption() {
		display = "";
		quantity = new Quantity();
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public int getNetwork() {
		return network;
	}

	public void setNetwork(int network) {
		this.network = network;
	}

	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the price
	 */
	public Price getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Price price) {
		this.price = price;
	}

	public int getRealNetwork() {
		return realNetwork;
	}

	public void setRealNetwork(int realNetwork) {
		this.realNetwork = realNetwork;
	}

	public void setOriginalLocation(int originalLocation) {
		this.originalLocation = originalLocation;
	}

	public int getOriginalLocation() {
		return originalLocation;
	}

	/**
	 * Comparison operator for sorting by 'network'.
	 */
	public static class CompareNetwork implements Comparator<SelectOption> {
		/**
		 * @param y
		 * @param z
		 * @return comparison value
		 */
		public int compare(SelectOption y, SelectOption z) {
			int c = y.network - z.network;
			return c;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + network;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SelectOption other = (SelectOption) obj;
		if (network != other.network)
			return false;

		return true;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return true if location is bulk, else false
	 */
	public boolean isBulk() {
		return bulk;
	}

	/**
	 * set the bulk flag for this location.
	 * 
	 * @param bulk boolean
	 */
	public void setBulk(boolean bulk) {
		this.bulk = bulk;
	}

	public int getBuyIncr() {
		return buyIncr;
	}

	public void setBuyIncr(int buyIncr) {
		this.buyIncr = buyIncr;
	}

	public int getMinQty() {
		return minQty;
	}

	public void setMinQty(int minQty) {
		if (minQty > 0) {
			this.minQty = minQty;
		} else {
			minQty = 1;
		}
	}

	public int getBuyQty() {
		return buyQty;
	}

	/**
	 * buyQty is calculated as the Part's requiredQty updated to the nearest
	 * buyIncrement of the location.
	 * 
	 * @param reqty
	 */
	public void updateBuyQty(int reqty) {
		if (reqty == 0) {
			reqty = 1;
		}

		if (reqty < minQty) {
			reqty = minQty;
		}

		buyQty = reqty;
		while (buyQty % buyIncr != 0) {
			buyQty++;
		}
	}

	public boolean checkQtyAvailable() {
		return buyQty <= getQuantity().getAvailable();
	}

	/**
	 * Updates the given qty to its nearest multiple of this location's
	 * buyIncrement
	 * 
	 * @param qty
	 * @return nearest multiple of this location's buyIncrement
	 */
	public long applyBuyIncrement(long qty) {
		while (qty % buyIncr != 0) {
			qty++;
		}
		return qty;
	}

	public String toString() {
		String str = network + ":" + display;
		if (price != null) {
			str += "Price:" + price.toString();
		}
		if (quantity != null) {
			str += "Quantity:" + quantity.toString();
		}
		return str;
	}

	/**
	 * @return the uOfM
	 */
	public String getuOfM() {
		return uOfM;
	}

	/**
	 * @param uOfM the uOfM to set
	 */
	public void setuOfM(String uOfM) {
		this.uOfM = uOfM;
	}

	public boolean isVirtual() {
		return virtual;
	}

	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public boolean isSelldirect() {
		return selldirect;
	}

	public void setSelldirect(boolean selldirect) {
		this.selldirect = selldirect;
	}

}
