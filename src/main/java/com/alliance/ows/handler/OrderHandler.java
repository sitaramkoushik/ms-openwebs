package com.alliance.ows.handler;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.alliance.ows.model.order.OrderRequestData;
import com.alliance.ows.model.order.OrderRequestPart;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OrderHandler extends DefaultHandler {

	private String content = null;
	private String lineNumber = "";
	private String orderQuantity = "";
	private String supplierLocationId = "";
	private String itemType = "";
	private String id = "";
	private String supplierManufacturer = "";
	private boolean headerId = false;
	private boolean customerDocumentId = false;
	private OrderRequestData orderReqData = new OrderRequestData();
	private OrderRequestPart partObj = null;
	private List<OrderRequestPart> partData = new ArrayList<OrderRequestPart>();

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {

		case "oa:LineNumber":
			partObj = new OrderRequestPart();
			lineNumber = qName;
			break;
		case "oa:Id":
			id = qName;
			break;
		case "ow-o:SupplierManufacturer":
			supplierManufacturer = qName;
			break;
		case "oa:OrderQuantity":
			orderQuantity = qName;
			break;
		case "ow-o:SupplierLocationId":
			supplierLocationId = qName;
			break;
		case "oa:ItemType":
			itemType = qName;
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equalsIgnoreCase("Line")) {
			partData.add(partObj);
		} else if (qName.equalsIgnoreCase("ow-o:Header")) {
			id = "";
			headerId = true;
		} else if (qName.equalsIgnoreCase("oa:CustomerDocumentId")) {
			id = "";
			customerDocumentId = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
		if (supplierManufacturer.equalsIgnoreCase("ow-o:SupplierManufacturer")) {
			partObj.setLineCode(content);
			supplierManufacturer = "";
		} else if (id.equalsIgnoreCase("oa:Id") && headerId) {
			partObj.setPart(content);
			id = "";
			headerId = false;
		} else if (lineNumber.equalsIgnoreCase("oa:LineNumber")) {
			partObj.setLine(Integer.parseInt(content));
			lineNumber = "";
		} else if (orderQuantity.equalsIgnoreCase("oa:OrderQuantity")) {
			partObj.setOrderedQuantity(Integer.parseInt(content));
			orderQuantity = "";
		} else if (itemType.equalsIgnoreCase("oa:ItemType")) {
			partObj.setPartType(content);
			itemType = "";
		} else if (supplierLocationId.equalsIgnoreCase("ow-o:SupplierLocationId") && content != null) {
			partObj.setSelLoc(Integer.parseInt(content));
			supplierLocationId = "";
		} else if (id.equalsIgnoreCase("oa:Id") && customerDocumentId) {
			orderReqData.setPoNumber(content);
			id = "";
			customerDocumentId = false;
		}
	}

	public OrderRequestData getOrderReqData() {
		orderReqData.setParts(partData);
		return orderReqData;
	}

}
