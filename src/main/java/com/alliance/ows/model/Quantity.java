/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model;

import java.io.Serializable;

/**z
 *
 * @author Krishna Kumar
 * 
 */
public class Quantity implements Serializable {
	
	private static final long serialVersionUID = 3112773480453875721L;
	private int available;
	private long requested;

	/**
	 * @return the available
	 */
	public int getAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(int available) {
		this.available = available;
	}

	/**
	 * @return the available
	 */
	public long getRequested() {
		return requested;
	}

	/**
	 * @param available the available to set
	 */
	public void setRequested(long requested) {
		this.requested = requested;
	}

}
