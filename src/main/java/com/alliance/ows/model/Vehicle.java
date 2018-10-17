package com.alliance.ows.model;

import java.io.Serializable;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class Vehicle implements Serializable {

	private static final long serialVersionUID = -1360652296692266941L;
	private String year;
	private String make;
	private String model;
	private String engine;

	public void setYear(String year) {
		this.year = year;
	}

	public String getYear() {
		return year;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getMake() {
		return make;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getEngine() {
		return engine;
	}

}
