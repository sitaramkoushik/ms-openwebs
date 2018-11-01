package com.alliance.ows.handler;

import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alliance.logging.UtilityLogger;
import com.alliance.ows.model.inquire.Envelope;
import com.alliance.ows.model.inquire.InquiryResponseData;
import com.alliance.ows.model.inquire.InquiryResponsePart;
import com.alliance.ows.model.inquire.SelectOption;
import com.alliance.ows.model.order.OrderConfirm;
import com.alliance.ows.model.order.OrderResponseData;
import com.alliance.ows.model.order.OrderResponsePart;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OwsXmlGenerator {

	private static DocumentBuilderFactory dbf;
	private static TransformerFactory tf;
	private static Transformer transformer;

	static {
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setValidating(false);
		tf = TransformerFactory.newInstance();
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			UtilityLogger.genericError("Got exception in OwsXmlGenerator: " + e.getMessage());
		}
	}

	private static Element createElement(String name, Element el, Document doc) {
		Element element = doc.createElement(name);
		el.appendChild(element);
		return element;
	}

	private static void createElement(String name, Element el, Document doc, String value) {
		Element element = doc.createElement(name);
		el.appendChild(element);
		if (value == null || value.isEmpty()) {
			element.setTextContent("");
		} else {
			element.setTextContent(value);
		}
	}

	// Order response XML formatting
	public String getOrderRespXml(OrderResponseData ordRespData) throws ParserConfigurationException, TransformerException {

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		// Root element creation and Fields,Attribute setting
		Element Envelope = doc.createElementNS("http://www.carpartstechnologies.com/openwebs-envelope", "Envelope");
		Envelope.setPrefix("ow-e");
		Envelope.setAttribute("revision", "2.0");
		// Header element data setting
		Element Header = createElement("ow-e:Header", Envelope, doc);
		Element EndPoints = createElement("ow-e:EndPoints", Header, doc);
		Element To = createElement("ow-e:To", EndPoints, doc);
		createElement("ow-e:Id", To, doc, "");
		Element From = createElement("ow-e:From", EndPoints, doc);
		createElement("ow-e:Id", From, doc, "");
		Element Properties = createElement("ow-e:Properties", Header, doc);
		createElement("ow-e:SentAt", Properties, doc, "");
		createElement("ow-e:Topic", Properties, doc, "AcknowledgePurchaseOrder");
		// Body element data setting
		Element Body = createElement("ow-e:Body", Envelope, doc);
		Element AcknowledgePurchaseOrder = createElement("ow-o:AcknowledgePurchaseOrder", Body, doc);
		AcknowledgePurchaseOrder.setAttribute("revision", "2.0");
		AcknowledgePurchaseOrder.setAttribute("xmlns:ow-o", "http://www.carpartstechnologies.com/openwebs-oagis");
		AcknowledgePurchaseOrder.setAttribute("xmlns:oa", "http://www.openapplications.org/oagis");
		// ApplicationArea data setting
		Element ApplicationArea = createElement("oa:ApplicationArea", AcknowledgePurchaseOrder, doc);
		Element Sender = createElement("oa:Sender", ApplicationArea, doc);
		createElement("oa:Component", Sender, doc, "");
		createElement("oa:CreationDateTime", ApplicationArea, doc, "");

		for (OrderConfirm ordConfirm : ordRespData.getData()) {

			// DataArea data setting
			Element DataArea = createElement("oa:DataArea", AcknowledgePurchaseOrder, doc);
			Element Acknowledge = createElement("oa:Acknowledge", DataArea, doc);
			createElement("oa:Code", Acknowledge, doc, "");
			// PurchaseOrder Fields and Data setting
			Element PurchaseOrder = createElement("oa:PurchaseOrder", DataArea, doc);
			Element oaHeader = createElement("ow-o:Header", PurchaseOrder, doc);
			Element DocumentIds = createElement("oa:DocumentIds", oaHeader, doc);
			Element SupplierDocumentId = createElement("oa:SupplierDocumentId", DocumentIds, doc);
			createElement("oa:Id", SupplierDocumentId, doc, "");
			Element DocumentReferences = createElement("oa:DocumentReferences", oaHeader, doc);
			Element PurchaseOrderDocumentReference = createElement("oa:PurchaseOrderDocumentReference", DocumentReferences, doc);
			Element oaDocumentIds = createElement("oa:DocumentIds", PurchaseOrderDocumentReference, doc);
			Element CustomerDocumentId = createElement("oa:CustomerDocumentId", oaDocumentIds, doc);
			createElement("oa:Id", CustomerDocumentId, doc, "");
			Element DocumentId = createElement("oa:DocumentId", oaDocumentIds, doc);
			createElement("oa:Id", DocumentId, doc, "");
			// Order status detail
			Element OrderStatus = createElement("ow-o:OrderStatus", oaHeader, doc);
			createElement("oa:Code", OrderStatus, doc, "");
			createElement("oa:Description", OrderStatus, doc, "");
			createElement("ow-o:Status", OrderStatus, doc, "");
			Element ExtendedPrice = createElement("oa:ExtendedPrice", oaHeader, doc);
			ExtendedPrice.setAttribute("currency", "");
			ExtendedPrice.setTextContent("");
			Element TotalAmount = createElement("oa:TotalAmount", oaHeader, doc);
			TotalAmount.setAttribute("currency", "");
			TotalAmount.setTextContent("");

			Element Charges = createElement("oa:Charges", oaHeader, doc);
			Element Charge = createElement("oa:Charge", Charges, doc);
			createElement("oa:Id", Charge, doc, "");
			Element Total = createElement("oa:Total", Charge, doc);
			Total.setAttribute("currency", "");
			Total.setTextContent("");
			Element TotalCharge = createElement("oa:TotalCharge", Charges, doc);
			Element TcTotal = createElement("oa:Total", TotalCharge, doc);
			TcTotal.setAttribute("currency", "");
			TcTotal.setTextContent("");
			int j = 2;
			while (j > 0) {
				Element Tax = createElement("oa:Tax", oaHeader, doc);
				Element TaxAmount = createElement("oa:TaxAmount", Tax, doc);
				TaxAmount.setAttribute("currency", "");
				TaxAmount.setTextContent("");
				createElement("oa:TaxCode", Tax, doc, "");
				j--;
			}
			// Part List data setting
			for (OrderResponsePart ordRespPart : ordConfirm.getParts()) {
				Element Line = createElement("oa:Line", PurchaseOrder, doc);
				createElement("oa:LineNumber", Line, doc, String.valueOf(ordRespPart.getLine()));
				Element OrderItem = createElement("ow-o:OrderItem", Line, doc);
				Element ItemIds = createElement("oa:ItemIds", OrderItem, doc);
				Element SupplierItemId = createElement("oa:SupplierItemId", ItemIds, doc);
				createElement("oa:Id", SupplierItemId, doc, ordRespPart.getPart());
				createElement("oa:ItemType", OrderItem, doc, ordRespPart.getDesc());
				Element ItemInfo = createElement("ow-o:ItemInfo", OrderItem, doc);
				Element ManufacturerInfo = createElement("ow-o:ManufacturerInfo", ItemInfo, doc);
				createElement("ow-o:SupplierManufacturer", ManufacturerInfo, doc, ordRespPart.getLineCode());
				for (com.alliance.ows.model.order.SelectOption selectOpt : ordRespPart.getLocations()) {
					Element QuantityInfo = createElement("ow-o:QuantityInfo", OrderItem, doc);
					Element ShippedQuantity = createElement("ow-o:ShippedQuantity", QuantityInfo, doc);
					ShippedQuantity.setAttribute("uom", "EACH");
					ShippedQuantity.setTextContent(String.valueOf(selectOpt.getConfirmedQuantity()));
					Element PriceInfo = createElement("ow-o:PriceInfo", OrderItem, doc);
					Element CorePrice = createElement("ow-o:CorePrice", PriceInfo, doc);
					Element CPAmount = createElement("oa:Amount", CorePrice, doc);
					CPAmount.setAttribute("currency", "");
					CPAmount.setTextContent(String.valueOf(selectOpt.getPrice().getCoreCost()));
					Element CPPerQuantity = createElement("oa:PerQuantity", CorePrice, doc);
					CPPerQuantity.setAttribute("uom", "EACH");
					CPPerQuantity.setTextContent("");
					Element OrderInfo = createElement("ow-o:OrderInfo", OrderItem, doc);
					Element SupplierLocationId = createElement("ow-o:SupplierLocationId", OrderInfo, doc);
					SupplierLocationId.setTextContent(String.valueOf(selectOpt.getNetwork()));
					Element RequestLineGUID = createElement("ow-o:RequestLineGUID", OrderItem, doc);
					RequestLineGUID.setTextContent("");
					Element OrderQuantity = createElement("oa:OrderQuantity", Line, doc);
					OrderQuantity.setAttribute("uom", "EACH");
					OrderQuantity.setTextContent(String.valueOf(selectOpt.getOrderQuantity()));
					Element UnitPrice = createElement("oa:UnitPrice", Line, doc);
					Element UPAmount = createElement("oa:Amount", UnitPrice, doc);
					UPAmount.setAttribute("currency", "");
					UPAmount.setTextContent("");
					Element UPPerQuantity = createElement("oa:PerQuantity", UnitPrice, doc);
					UPPerQuantity.setAttribute("uom", "EACH");
					UPPerQuantity.setTextContent("");
					Element PartExtendedPrice = createElement("oa:ExtendedPrice", Line, doc);
					PartExtendedPrice.setAttribute("currency", "");
					PartExtendedPrice.setTextContent("");
					Element PartTotalAmount = createElement("oa:TotalAmount", Line, doc);
					PartTotalAmount.setAttribute("currency", "");
					PartTotalAmount.setTextContent("");
					Element BackOrderedQuantity = createElement("oa:BackOrderedQuantity", Line, doc);
					BackOrderedQuantity.setAttribute("uom", "EACH");
					BackOrderedQuantity.setTextContent("");
				}
				Element PartDocumentReferences = createElement("oa:DocumentReferences", Line, doc);
				Element PartPurchaseOrderDocumentReference = createElement("oa:PurchaseOrderDocumentReference", PartDocumentReferences, doc);
				createElement("oa:LineNumber", PartPurchaseOrderDocumentReference, doc, "1");
			}
		}

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(Envelope);
		transformer.transform(source, result);
		String xmlString = sw.toString();
		return xmlString;

	}

	// Inquire response XML formatting
	public String getInquireRespXml(InquiryResponseData inqRespdata, Envelope envData) throws ParserConfigurationException, TransformerException,
					MalformedURLException {

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		Element Envelope = doc.createElementNS(envData.getEnvAttrValue(), "Envelope");
		Envelope.setPrefix("ow-e");
		Envelope.setAttribute(envData.getEnvAttrRevName(), envData.getEnvAttrRevValue());
		// Header element data setting
		Element Header = createElement("ow-e:Header", Envelope, doc);
		Element EndPoints = createElement("ow-e:EndPoints", Header, doc);
		Element To = createElement("ow-e:To", EndPoints, doc);
		try {
			createElement("ow-e:Id", To, doc, envData.getHeader().getEndPoints().getTo().getId());
		} catch (Exception e) {
			createElement("ow-e:Id", To, doc, "");
		}
		Element From = createElement("ow-e:From", EndPoints, doc);
		try {
			createElement("ow-e:Id", From, doc, envData.getHeader().getEndPoints().getFrom().getId());
		} catch (Exception e) {
			createElement("ow-e:Id", From, doc, "");
		}
		Element Properties = createElement("ow-e:Properties", Header, doc);
		try {
			createElement("ow-e:Identity", Properties, doc, envData.getHeader().getProperties().getIdentity());
			createElement("ow-e:SentAt", Properties, doc, envData.getHeader().getProperties().getSentAt());
		} catch (Exception e) {
			createElement("ow-e:Identity", Properties, doc, "");
			createElement("ow-e:SentAt", Properties, doc, "");
		}
		createElement("ow-e:Topic", Properties, doc, "AddQuote");
		Element Body = createElement("ow-e:Body", Envelope, doc);
		Element AddQuote = createElement("ow-o:AddQuote", Body, doc);
		AddQuote.setAttribute(envData.getBody().getAddReqForQuote().getRfqAttrRevName(), envData.getBody().getAddReqForQuote().getRfqAttrRevValue());
		AddQuote.setAttribute(envData.getBody().getAddReqForQuote().getRfqAttrName(), envData.getBody().getAddReqForQuote().getRfqAttrValue());
		AddQuote.setAttribute(envData.getBody().getAddReqForQuote().getRfqAttrOaName(), envData.getBody().getAddReqForQuote().getRfqAttrOaValue());

		Element ApplicationArea = createElement("oa:ApplicationArea", AddQuote, doc);
		Element Sender = createElement("oa:Sender", ApplicationArea, doc);
		try {
			createElement("oa:Component", Sender, doc, envData.getBody().getAddReqForQuote().getApplicationArea().getSender().getComponent());
			createElement("oa:CreationDateTime", ApplicationArea, doc, envData.getBody().getAddReqForQuote().getApplicationArea()
							.getCreationDateTime());
		} catch (Exception e) {
			createElement("oa:Component", Sender, doc, "");
			createElement("oa:CreationDateTime", ApplicationArea, doc, "");
		}
		Element DataArea = createElement("oa:DataArea", AddQuote, doc);
		Element Add = createElement("oa:Add", DataArea, doc);
		Add.setAttribute(envData.getBody().getAddReqForQuote().getDataArea().getAddAttrName(), envData.getBody().getAddReqForQuote().getDataArea()
						.getAddAttrVal());

		Element Quote = createElement("oa:Quote", DataArea, doc);
		Element oaHeader = createElement("oa:Header", Quote, doc);
		/*Element OrderStatus = createElement("ow-o:OrderStatus", oaHeader, doc);
		createElement("oa:Code", OrderStatus, doc, "");
		createElement("oa:Description", OrderStatus, doc, "");
		createElement("ow-o:Status", OrderStatus, doc, "");*/
		createElement("oa:SpecialPriceAuthorization", oaHeader, doc, "");
		Element Parties = createElement("oa:Parties", oaHeader, doc);
		// supplier party
		Element SupplierParty = createElement("ow-o:SupplierParty", Parties, doc);
		Element PartyId = createElement("oa:PartyId", SupplierParty, doc);
		Element Business = createElement("oa:Business", SupplierParty, doc);
		try {
			createElement("oa:Id", PartyId, doc, envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader().getParties()
							.getSupplierParty().getPartyId().getId());
			createElement("ow-o:UniversalId", SupplierParty, doc, "");
			if (envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader().getParties().getSupplierParty().getBussiness() != null) {
				createElement("oa:Id", Business, doc, envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader()
								.getParties().getSupplierParty().getBussiness().getId());
			}

		} catch (Exception e) {
			createElement("oa:Id", PartyId, doc, "");
			createElement("ow-o:UniversalId", SupplierParty, doc, "");
		}
		// customer party
		Element CustomerParty = createElement("ow-o:CustomerParty", Parties, doc);
		Element CustomerPartyId = createElement("oa:PartyId", CustomerParty, doc);
		Element CustBusiness = createElement("oa:Business", CustomerParty, doc);
		try {
			createElement("oa:Id", CustomerPartyId, doc, envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader()
							.getParties().getCustomerParty().getPartyId().getId());
			createElement("ow-o:UniversalId", CustomerParty, doc, "");
			if (envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader().getParties().getCustomerParty().getBussiness() != null) {
				createElement("oa:Id", CustBusiness, doc, envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader()
								.getParties().getCustomerParty().getBussiness().getId());
			}
		} catch (Exception e) {
			createElement("oa:Id", CustomerPartyId, doc, "");
			createElement("ow-o:UniversalId", CustomerParty, doc, "");
		}
		// ship to party
		Element ShipToParty = createElement("ow-o:ShipToParty", Parties, doc);
		Element ShipToPartyId = createElement("oa:PartyId", ShipToParty, doc);
		Element ShipToBusiness = createElement("oa:Business", ShipToParty, doc);
		try {
			createElement("oa:Id", ShipToPartyId, doc, envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader()
							.getParties().getShipToParty().getPartyId().getId());
			createElement("ow-o:UniversalId", ShipToParty, doc, "");
			if (envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader().getParties().getShipToParty().getBussiness() != null) {
				createElement("oa:Id", ShipToBusiness, doc, envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader()
								.getParties().getShipToParty().getBussiness().getId());
			}
		} catch (Exception e) {
			createElement("oa:Id", ShipToPartyId, doc, "");
			createElement("ow-o:UniversalId", ShipToParty, doc, "");
		}

		for (InquiryResponsePart inqRespPartinq : inqRespdata.getData()) {
			Element Line = createElement("ow-o:Line", Quote, doc);
			Element OrderItem = createElement("ow-o:OrderItem", Line, doc);
			Element ItemIds = createElement("oa:ItemIds", OrderItem, doc);
			Element SupplierItemId = createElement("oa:SupplierItemId", ItemIds, doc);
			createElement("oa:Id", SupplierItemId, doc, String.valueOf(inqRespPartinq.getPartId()));
			createElement("oa:ItemType", OrderItem, doc, inqRespPartinq.getPart());

			for (SelectOption selectOp : inqRespPartinq.getLocations()) {
				Element ItemInfo = createElement("ow-o:ItemInfo", OrderItem, doc);
				Element ManufacturerInfo = createElement("ow-o:ManufacturerInfo", ItemInfo, doc);
				createElement("ow-o:SupplierManufacturer", ManufacturerInfo, doc, inqRespPartinq.getLineCode());
				Element AdditionalInfo = createElement("ow-o:AdditionalInfo", ItemInfo, doc);
				createElement("ow-o:InfoName", AdditionalInfo, doc, "");
				createElement("ow-o:InfoValue", AdditionalInfo, doc, "");
				Element QuantityInfo = createElement("ow-o:QuantityInfo", OrderItem, doc);
				Element AvailableQuantity = createElement("ow-o:AvailableQuantity", QuantityInfo, doc);
				AvailableQuantity.setAttribute("uom", "EACH");
				AvailableQuantity.setTextContent(String.valueOf(selectOp.getQuantity().getAvailable()));
			}

			Element PriceInfo = createElement("ow-o:PriceInfo", OrderItem, doc);
			Element ListPrice = createElement("ow-o:ListPrice", PriceInfo, doc);
			Element Amount = createElement("oa:Amount", ListPrice, doc);
			Amount.setAttribute("currency", "");
			Amount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getList()));
			Element PerQuantity = createElement("oa:PerQuantity", ListPrice, doc);
			PerQuantity.setAttribute("uom", "EACH");
			PerQuantity.setTextContent("");
			Element CorePrice = createElement("ow-o:CorePrice", PriceInfo, doc);
			Element CPAmount = createElement("oa:Amount", CorePrice, doc);
			CPAmount.setAttribute("currency", "");
			CPAmount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getCoreCost()));
			Element CPPerQuantity = createElement("oa:PerQuantity", CorePrice, doc);
			CPPerQuantity.setAttribute("uom", "EACH");
			CPPerQuantity.setTextContent("");
			Element OrderInfo = createElement("ow-o:OrderInfo", OrderItem, doc);
			Element SupplierLocationId = createElement("ow-o:SupplierLocationId", OrderInfo, doc);
			SupplierLocationId.setTextContent("");
			Element UnitPrice = createElement("oa:UnitPrice", Line, doc);
			Element UPAmount = createElement("oa:Amount", UnitPrice, doc);
			UPAmount.setAttribute("currency", "USD");
			UPAmount.setTextContent("");
			Element UPPerQuantity = createElement("oa:PerQuantity", UnitPrice, doc);
			UPPerQuantity.setAttribute("uom", "EACH");
			UPPerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getActualCost()));
			if (inqRespPartinq.getDescription() != null) {
				createElement("oa:Description", Line, doc, inqRespPartinq.getDescription());
			} else {
				createElement("oa:Description", Line, doc, "");
			}
			Element OWOrderStatus = createElement("ow-o:OrderStatus", Line, doc);
			createElement("oa:Code", OWOrderStatus, doc, "");
			createElement("oa:Description", OWOrderStatus, doc, "");
			createElement("ow-o:Status", OWOrderStatus, doc, "");
			Element DocumentReferences = createElement("oa:DocumentReferences", Line, doc);
			Element RFQDocumentReference = createElement("oa:RFQDocumentReference", DocumentReferences, doc);
			createElement("oa:LineNumber", RFQDocumentReference, doc, String.valueOf(inqRespPartinq.getLine()));
		}
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(Envelope);
		transformer.transform(source, result);
		String xmlString = sw.toString();
		return xmlString;

	}

}
