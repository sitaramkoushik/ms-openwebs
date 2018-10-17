package com.alliance.ows.model.inquire;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryResponseData implements Serializable {

	private static final long serialVersionUID = 6268558842179369248L;
	
	@JsonProperty("Data")
	private List<InquiryResponsePart> Data;

	public List<InquiryResponsePart> getData() {
		return Data;
	}

	public void setData(List<InquiryResponsePart> respdata) {
		Data = respdata;
	}
}
