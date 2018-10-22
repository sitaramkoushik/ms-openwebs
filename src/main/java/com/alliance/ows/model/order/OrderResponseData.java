package com.alliance.ows.model.order;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Krishna Kumar
 * 
 */

public class OrderResponseData implements Serializable {
	
	private static final long serialVersionUID = 7687930903891755394L;
	
	@JsonProperty("Data")
	private List<OrderConfirm> Data;

	public List<OrderConfirm> getData() {
		return Data;
	}

	public void setData(List<OrderConfirm> respdata) {
		Data = respdata;
	}
}