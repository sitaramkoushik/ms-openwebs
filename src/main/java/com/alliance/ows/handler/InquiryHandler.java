package com.alliance.ows.handler;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.alliance.ows.model.inquire.InquiryRequestPart;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryHandler extends DefaultHandler {
	
	private String content = null;
	private String lineNumber = "";
	private String id = "";
	private String supplierManufacturer = "";
	private InquiryRequestPart partObj = null;
	private List<InquiryRequestPart> partData = new ArrayList<InquiryRequestPart>();

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {

		case "oa:LineNumber":
			partObj = new InquiryRequestPart();
			lineNumber = qName;
			break;

		case "oa:Id":
			id = qName;
			break;

		case "ow-o:SupplierManufacturer":
			supplierManufacturer = qName;
			break;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase("Line")) {
			partData.add(partObj);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
		if (supplierManufacturer.equalsIgnoreCase("ow-o:SupplierManufacturer")) {
			partObj.setLineCode(content);
			supplierManufacturer = "";
		} else if (id.equalsIgnoreCase("oa:Id")) {
			partObj.setPart(content);
			id = "";
		} else if (lineNumber.equalsIgnoreCase("oa:LineNumber")) {
			partObj.setLine(Integer.parseInt(content));
			lineNumber = "";
		}
	}

	public List<InquiryRequestPart> getPartData() {
		return partData;
	}

}
