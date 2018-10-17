/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.order;

import java.util.Comparator;
import com.alliance.ows.model.Price;
import com.alliance.ows.model.Quantity;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class SelectOption implements java.io.Serializable {

	private static final long serialVersionUID = -6926665039728870286L;
	private String display;
	private int network;
	private Quantity quantity;
	private Price price;
	private int orderStatus;
	private boolean orderReplaced;
	private String orderReplacedPart;
	private String orderReplacedLineCode;
	private int orderQuantity = 0;
	private int confirmedQuantity = 0;
	private int realNetwork;
	private String tempLineCode = "";
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

	public void setNetwork(int network) {
		this.network = network;
	}

	public int getNetwork() {
		return network;
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

	public boolean isOrderReplaced() {
		return orderReplaced;
	}

	public void setOrderReplaced(boolean orderReplaced) {
		this.orderReplaced = orderReplaced;
	}

	public String getOrderReplacedPart() {
		return orderReplacedPart;
	}

	public void setOrderReplacedPart(String orderReplacedPart) {
		this.orderReplacedPart = orderReplacedPart;
	}

	public String getOrderReplacedLineCode() {
		return orderReplacedLineCode;
	}

	public void setOrderReplacedLineCode(String orderReplacedLineCode) {
		this.orderReplacedLineCode = orderReplacedLineCode;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public int getConfirmedQuantity() {
		return confirmedQuantity;
	}

	public void setConfirmedQuantity(int confirmedQuantity) {
		this.confirmedQuantity = confirmedQuantity;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getRealNetwork() {
		return realNetwork;
	}

	public void setRealNetwork(int realNetwork) {
		this.realNetwork = realNetwork;
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

	public SelectOption clone() {
		SelectOption s = new SelectOption();

		s.display = this.display;
		s.network = this.network;

		if (this.price != null) {
			s.price = this.price;
		}
		if (this.quantity != null) {
			s.quantity = this.quantity;
		}

		s.orderStatus = this.orderStatus;
		s.orderReplaced = this.orderReplaced;
		s.orderReplacedLineCode = this.orderReplacedLineCode;
		s.orderReplacedPart = this.orderReplacedPart;

		s.orderQuantity = this.orderQuantity;
		s.realNetwork = this.realNetwork;

		return s;
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

	public void setTempLineCode(String tempLineCode) {

		this.tempLineCode = tempLineCode;

	}

	public String getTempLineCode() {

		return this.tempLineCode;
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
