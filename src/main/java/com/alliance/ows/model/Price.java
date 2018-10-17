/*******************************************************************************
 * Private and Confidential. All rights reserved by the Aftermarket Auto Parts Alliance.
 *******************************************************************************/
package com.alliance.ows.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class Price implements Serializable {
	
	private static final long serialVersionUID = -6245455157784990711L;
	private double list;
	private double cost;
	private double coreList;
	private double coreCost;
	private double orderedCost;
	private double orderedList;
	private double orderedCoreCost;
	private double orderedCoreList;
	private boolean requestedPrice;
	private double actualCost;
	private double actualCoreCost;
	private LinkedHashMap<String, Double> otherPrices;

	public boolean isRequestedPrice() {
		return requestedPrice;
	}

	public void setRequestedPrice(boolean requestedPrice) {
		this.requestedPrice = requestedPrice;
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}

	public double getActualCoreCost() {
		return actualCoreCost;
	}

	public void setActualCoreCost(double actualCoreCost) {
		this.actualCoreCost = actualCoreCost;
	}

	public LinkedHashMap<String, Double> getOtherPrices() {
		return otherPrices;
	}

	public void setOtherPrices(LinkedHashMap<String, Double> otherPrices) {
		this.otherPrices = otherPrices;
	}

	public double getList() {
		return list;
	}

	public void setList(double list) {
		this.list = list;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getCoreList() {
		return coreList;
	}

	public void setCoreList(double coreList) {
		this.coreList = coreList;
	}

	public double getCoreCost() {
		return coreCost;
	}

	public void setCoreCost(double coreCost) {
		this.coreCost = coreCost;
	}

	public double getOrderedCost() {
		return orderedCost;
	}

	public void setOrderedCost(double orderedCost) {
		this.orderedCost = orderedCost;
	}

	public double getOrderedList() {
		return orderedList;
	}

	public void setOrderedList(double orderedList) {
		this.orderedList = orderedList;
	}

	public double getOrderedCoreCost() {
		return orderedCoreCost;
	}

	public void setOrderedCoreCost(double orderedCoreCost) {
		this.orderedCoreCost = orderedCoreCost;
	}

	public double getOrderedCoreList() {
		return orderedCoreList;
	}

	public void setOrderedCoreList(double orderedCoreList) {
		this.orderedCoreList = orderedCoreList;
	}

}