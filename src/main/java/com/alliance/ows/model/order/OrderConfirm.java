/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model.order;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Krishna Kumar
 * 
 */

public class OrderConfirm implements Serializable, Comparable<OrderConfirm> {

	private static final long serialVersionUID = -5284605982610280553L;
	private boolean error = false;
	private String track = "";
	private String confirm = "";
	private String seller = "";
	private String message = "";
	private int network;
	private String snName = "";
	private String snType = "";
	private boolean onHold = false;
	private String called = "";
	private boolean accounting = false;
	private int accountngNetwork;
	private List<OrderResponsePart> parts;
	private String type;
	private boolean courierOrder;
	private int errorType;

	/**
	 * @return the onHold
	 */
	public boolean isOnHold() {
		return onHold;
	}

	/**
	 * @param onHold the onHold to set
	 */
	public void setOnHold(boolean onHold) {
		this.onHold = onHold;
	}

	/**
	 * @return the snName
	 */
	public String getSnName() {
		return snName;
	}

	/**
	 * @param snName the snName to set
	 */
	public void setSnName(String snName) {
		this.snName = snName;
	}

	/**
	 * @return the snType
	 */
	public String getSnType() {
		return snType;
	}

	/**
	 * @param snType the snType to set
	 */
	public void setSnType(String snType) {
		this.snType = snType;
	}

	/**
	 * @return the confirm
	 */
	public String getConfirm() {
		return confirm;
	}

	/**
	 * @param confirm the confirm to set
	 */
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the network
	 */
	public int getNetwork() {
		return network;
	}

	/**
	 * @param network the network to set
	 */
	public void setNetwork(int network) {
		this.network = network;
	}

	/**
	 * @return the seller
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * @param seller the seller to set
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}

	/**
	 * @return the smsTrack
	 */
	public String getTrack() {
		return track;
	}

	/**
	 * @param track the smsTrack to set
	 */
	public void setTrack(String track) {
		this.track = track;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}

	public boolean isAccounting() {
		return accounting;
	}

	public void setAccounting(boolean accounting) {
		this.accounting = accounting;
	}

	public int getAccountngNetwork() {
		return accountngNetwork;
	}

	public void setAccountngNetwork(int accountngNetwork) {
		this.accountngNetwork = accountngNetwork;
	}

	@Override
	public int compareTo(OrderConfirm o) {
		int i = this.network * 100;
		int j = o.network * 100;
		if (this.accounting) {
			i = this.accountngNetwork * 100 + 1;
		}
		if (o.accounting) {
			j = o.accountngNetwork * 100 + 1;
		}
		return i - j;
	}

	public List<OrderResponsePart> getParts() {
		return Collections.unmodifiableList(this.parts);
	}

	public void setParts(List<OrderResponsePart> parts) {
		this.parts = parts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCourierOrder() {
		return courierOrder;
	}

	public void setCourierOrder(boolean courierOrder) {
		this.courierOrder = courierOrder;
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

}
