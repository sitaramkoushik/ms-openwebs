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
	
	@JsonProperty("data")
	private List<InquiryResponsePart> data;

	public List<InquiryResponsePart> getData() {
		return data;
	}

	public void setData(List<InquiryResponsePart> respdata) {
		data = respdata;
	}
}
