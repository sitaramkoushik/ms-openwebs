package com.alliance.ows.handler;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.alliance.ows.model.inquire.Line;
import com.alliance.ows.model.inquire.SelectOption;
import com.alliance.ows.model.order.OrderConfirm;
import com.alliance.ows.model.order.OrderResponseData;
import com.alliance.ows.model.order.OrderResponsePart;
import com.alliance.utils.ConstantsUtility;

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
	public String getOrderRespXml(OrderResponseData ordRespData, Envelope envData) throws ParserConfigurationException, TransformerException {
		List<String> reqPartDetail = new ArrayList<>();
		List<com.alliance.ows.model.order.Line> lineList = new ArrayList<com.alliance.ows.model.order.Line>();
		try {
			lineList = envData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getLine();
		} catch (Exception e) {
			lineList = null;
		}
		if (lineList != null) {
			for (com.alliance.ows.model.order.Line line : lineList) {
				String partNum = null;
				String lineCode = null;
				try {
					partNum = line.getOrderItem().getItemId().getSupplierItemId().getId();
				} catch (Exception e) {
					partNum = null;
				}
				try {
					lineCode = line.getOrderItem().getItemInfo().getManufacturerInfo().getSupplierManufacturer();
				} catch (Exception e) {
					lineCode = null;
				}
				if (partNum != null && lineCode != null && !partNum.isEmpty() && !lineCode.isEmpty()) {
					reqPartDetail.add(lineCode + "_" + partNum);
				} else if (partNum != null && !partNum.isEmpty()) {
					reqPartDetail.add(partNum);
				}
			}
		}
		
		List<OrderConfirm> lstOrderConfirms = ordRespData.getData();
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
		try {
			createElement("ow-e:Id", To, doc, envData.getOrdHeader().getEndPoints().getFrom().getId());
		} catch (Exception e) {
			createElement("ow-e:Id", To, doc, "");
		}
		Element From = createElement("ow-e:From", EndPoints, doc);
		try {
			createElement("ow-e:Id", From, doc, envData.getOrdHeader().getEndPoints().getTo().getId());
		} catch (Exception e) {
			createElement("ow-e:Id", From, doc, "");
		}
		Element Properties = createElement("ow-e:Properties", Header, doc);
		try {
			createElement("ow-e:Identity", Properties, doc, envData.getOrdHeader().getProperties().getIdentity());
			createElement("ow-e:SentAt", Properties, doc, envData.getOrdHeader().getProperties().getSentAt());
		} catch (Exception e) {
			createElement("ow-e:Identity", Properties, doc, "");
			createElement("ow-e:SentAt", Properties, doc, "");
		}
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
		try {
			createElement("oa:Component", Sender, doc, envData.getOrdBody().getProcessPurchaseOrder().getApplicationArea().getSender().getComponent());
		} catch (Exception e) {
			createElement("oa:Component", Sender, doc, "");
		}
		try {
			createElement("oa:CreationDateTime", ApplicationArea, doc, envData.getOrdBody().getProcessPurchaseOrder().getApplicationArea()
							.getCreationDateTime());
		} catch (Exception e) {
			createElement("oa:CreationDateTime", ApplicationArea, doc, "");
		}
		// DataArea data setting
		Element DataArea = createElement("oa:DataArea", AcknowledgePurchaseOrder, doc);
		Element Acknowledge = createElement("oa:Acknowledge", DataArea, doc);
		createElement("oa:Code", Acknowledge, doc, "");
		// PurchaseOrder Fields and Data setting
		Element PurchaseOrder = createElement("oa:PurchaseOrder", DataArea, doc);
		Element oaHeader = createElement("ow-o:Header", PurchaseOrder, doc);
		Element DocumentIds = createElement("oa:DocumentIds", oaHeader, doc);

		Element SupplierDocumentId = createElement("oa:SupplierDocumentId", DocumentIds, doc);
		OrderConfirm orderConfirm = null;
		if (lstOrderConfirms != null && lstOrderConfirms.size() > 0) {
			orderConfirm = lstOrderConfirms.get(0);
			createElement("oa:Id", SupplierDocumentId, doc, orderConfirm.getConfirm());
		}

		Element DocumentReferences = createElement("oa:DocumentReferences", oaHeader, doc);
		Element PurchaseOrderDocumentReference = createElement("oa:PurchaseOrderDocumentReference", DocumentReferences, doc);
		Element oaDocumentIds = createElement("oa:DocumentIds", PurchaseOrderDocumentReference, doc);
		Element CustomerDocumentId = createElement("oa:CustomerDocumentId", oaDocumentIds, doc);

		createElement("oa:Id", CustomerDocumentId, doc, envData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder()
						.getOwoHeader().getDocuments().getCustomerDocumentId().getId());

		Element DocumentId = createElement("oa:DocumentId", oaDocumentIds, doc);
		createElement("oa:Id", DocumentId, doc, envData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getOwoHeader()
						.getDocuments().getDocumentId().getId());
		// Order status detail
		Element OrderStatus = createElement("ow-o:OrderStatus", oaHeader, doc);

		if (orderConfirm != null && !orderConfirm.isError()) {
			createElement("oa:Code", OrderStatus, doc, "Open");
			createElement("oa:Description", OrderStatus, doc, "");
			createElement("ow-o:Status", OrderStatus, doc, ConstantsUtility.SUCCESS);
		} else {
			createElement("oa:Code", OrderStatus, doc, "Hold");
			createElement("oa:Description", OrderStatus, doc, orderConfirm.getMessage());
			createElement("ow-o:Status", OrderStatus, doc, ConstantsUtility.FAIL);
		}

		/*Element Charges = createElement("oa:Charges", oaHeader, doc);
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
		}*/

		int ordLineNumber = -1, lineNum = 0;
		double extendedPrice = 0.0, totalPrice = 0.0;
		for (OrderConfirm ordConfirm : ordRespData.getData()) {
			Element Line = createElement("oa:Line", PurchaseOrder, doc);
			createElement("oa:LineNumber", Line, doc, String.valueOf(++lineNum));
			// Part List data setting
			for (OrderResponsePart ordRespPart : ordConfirm.getParts()) {
				if (reqPartDetail.contains(ordRespPart.getPart())) {
					ordLineNumber = reqPartDetail.indexOf(ordRespPart.getPart());
				} else if (reqPartDetail.contains(ordRespPart.getLineCode() + "_" + ordRespPart.getPart())) {
					ordLineNumber = reqPartDetail.indexOf(ordRespPart.getLineCode() + "_" + ordRespPart.getPart());
				}

				if (ordLineNumber == -1) {
					continue;
				}

				com.alliance.ows.model.order.Line line = lineList.get(ordLineNumber);

				Element OrderItem = createElement("ow-o:OrderItem", Line, doc);
				Element ItemIds = createElement("oa:ItemIds", OrderItem, doc);
				Element SupplierItemId = createElement("oa:SupplierItemId", ItemIds, doc);

				createElement("oa:Id", SupplierItemId, doc, ordRespPart.getPart());
				createElement("oa:ItemType", OrderItem, doc, "Part");

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
					CPAmount.setAttribute("currency", "USD");
					CPAmount.setTextContent(String.valueOf(selectOpt.getPrice().getCoreCost()));

					Element CPPerQuantity = createElement("oa:PerQuantity", CorePrice, doc);
					CPPerQuantity.setAttribute("uom", "EACH");
					CPPerQuantity.setTextContent("1");

					Element OrderInfo = createElement("ow-o:OrderInfo", OrderItem, doc);
					Element SupplierLocationId = createElement("ow-o:SupplierLocationId", OrderInfo, doc);

					if (line != null && line.getOrderItem() != null && line.getOrderItem().getOrderInfo() != null) {
						SupplierLocationId.setTextContent(String.valueOf(line.getOrderItem().getOrderInfo().getSupplierLocationId()));
					} else {
						SupplierLocationId.setTextContent(String.valueOf(selectOpt.getNetwork()));
					}

					Element RequestLineGUID = createElement("ow-o:RequestLineGUID", OrderItem, doc);
					RequestLineGUID.setTextContent(envData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getLine()
									.get(ordLineNumber).getOrderItem().getRequestLineGUID());

					Element OrderQuantity = createElement("oa:OrderQuantity", Line, doc);
					OrderQuantity.setAttribute("uom", "EACH");
					OrderQuantity.setTextContent(String.valueOf(selectOpt.getOrderQuantity()));

					Element UnitPrice = createElement("oa:UnitPrice", Line, doc);
					Element UPAmount = createElement("oa:Amount", UnitPrice, doc);
					UPAmount.setAttribute("currency", "USD");
					UPAmount.setTextContent(String.valueOf(selectOpt.getPrice().getActualCost()));

					Element UPPerQuantity = createElement("oa:PerQuantity", UnitPrice, doc);
					UPPerQuantity.setAttribute("uom", "EACH");
					UPPerQuantity.setTextContent("1");
					Element PartExtendedPrice = createElement("oa:ExtendedPrice", Line, doc);
					PartExtendedPrice.setAttribute("currency", "");
					extendedPrice = extendedPrice + selectOpt.getPrice().getActualCost() * selectOpt.getOrderQuantity();
					PartExtendedPrice.setTextContent(String.valueOf(selectOpt.getPrice().getActualCost() * selectOpt.getOrderQuantity()));
					totalPrice = totalPrice + selectOpt.getPrice().getCoreList() * selectOpt.getOrderQuantity();
					Element PartTotalAmount = createElement("oa:TotalAmount", Line, doc);
					PartTotalAmount.setAttribute("currency", "USD");
					PartTotalAmount.setTextContent(String.valueOf(selectOpt.getPrice().getCoreList() * selectOpt.getOrderQuantity()));

					Element BackOrderedQuantity = createElement("oa:BackOrderedQuantity", Line, doc);
					BackOrderedQuantity.setAttribute("uom", "EACH");
					BackOrderedQuantity.setTextContent("");

				}

				Element OwoOrderStatus = createElement("ow-o:OrderStatus", Line, doc);
				createElement("oa:Code", OwoOrderStatus, doc, "Open");
				createElement("oa:Description", OwoOrderStatus, doc, ordRespPart.getDesc());
				createElement("ow-o:Status", OwoOrderStatus, doc, "success");
				Element PartDocumentReferences = createElement("oa:DocumentReferences", Line, doc);
				Element PartPurchaseOrderDocumentReference = createElement("oa:PurchaseOrderDocumentReference", PartDocumentReferences, doc);
				try {
					createElement("oa:LineNumber",
									PartPurchaseOrderDocumentReference,
									doc,
									String.valueOf(envData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getLine()
													.get(ordLineNumber).getLineNumber()));
				} catch (Exception e) {
					createElement("oa:LineNumber", PartPurchaseOrderDocumentReference, doc, String.valueOf(""));
				}
			}

		}
		Element ExtendedPrice = createElement("oa:ExtendedPrice", oaHeader, doc);
		ExtendedPrice.setAttribute("currency", "USD");
		ExtendedPrice.setTextContent(String.valueOf(extendedPrice));
		Element TotalAmount = createElement("oa:TotalAmount", oaHeader, doc);
		TotalAmount.setAttribute("currency", "USD");
		TotalAmount.setTextContent(String.valueOf(totalPrice));

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(Envelope);
		transformer.transform(source, result);
		return sw.toString();

	}

	// Inquire response XML formatting
	public String getInquireRespXml(InquiryResponseData inqRespdata, Envelope envData) throws ParserConfigurationException, TransformerException,
	MalformedURLException {
		List<String> reqPartDetail = new ArrayList<>();
		List<Line> lineList = new ArrayList<Line>();
		try {
			lineList = envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine();
		} catch (Exception e) {
			lineList = null;
		}
		if (lineList != null) {
			for (Line line : lineList) {
				String partNum = null;
				String lineCode = null;
				try {
					partNum = line.getOrderItem().getItemId().getSupplierItemId().getId();
				} catch (Exception e) {
					partNum = null;
				}
				try {
					lineCode = line.getOrderItem().getItemInfo().getManufacturerInfo().getSupplierManufacturer();
				} catch (Exception e) {
					lineCode = null;
				}
				if (partNum != null && lineCode != null && !partNum.isEmpty() && !lineCode.isEmpty()) {
					reqPartDetail.add(lineCode + "_" + partNum);
				} else if (partNum != null && !partNum.isEmpty()) {
					reqPartDetail.add(partNum);
				}
			}
		}
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		doc.setXmlStandalone(false);
		Element Envelope = doc.createElementNS(envData.getEnvAttrValue(), "Envelope");
		Envelope.setPrefix("ow-e");
		Envelope.setAttribute(envData.getEnvAttrRevName(), envData.getEnvAttrRevValue());
		// Header element data setting
		Element Header = createElement("ow-e:Header", Envelope, doc);
		Element EndPoints = createElement("ow-e:EndPoints", Header, doc);
		Element To = createElement("ow-e:To", EndPoints, doc);
		try {
			createElement("ow-e:Id", To, doc, envData.getHeader().getEndPoints().getFrom().getId());
		} catch (Exception e) {
			createElement("ow-e:Id", To, doc, "");
		}
		Element From = createElement("ow-e:From", EndPoints, doc);
		try {
			createElement("ow-e:Id", From, doc, envData.getHeader().getEndPoints().getTo().getId());
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
		Element OrderStatus = createElement("ow-o:OrderStatus", oaHeader, doc);
		createElement("oa:Code", OrderStatus, doc, "Open");
		createElement("oa:Description", OrderStatus, doc, "0");
		createElement("ow-o:Status", OrderStatus, doc, "success");
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
		int inqLineNumber = -1, lineNumber = 1;
		for (InquiryResponsePart inqRespPartinq : inqRespdata.getData()) {
			if (reqPartDetail.contains(inqRespPartinq.getPart())) {
				inqLineNumber = reqPartDetail.indexOf(inqRespPartinq.getPart());
			} else if (reqPartDetail.contains(inqRespPartinq.getLineCode() + "_" + inqRespPartinq.getPart())) {
				inqLineNumber = reqPartDetail.indexOf(inqRespPartinq.getLineCode() + "_" + inqRespPartinq.getPart());
			}

			if (inqLineNumber == -1) {
				continue;
			}

			for (Iterator<SelectOption> iterator = inqRespPartinq.getLocations().iterator(); iterator.hasNext();) {
				SelectOption selectOption = iterator.next();
				addPartResponse(doc, Quote, envData, inqRespPartinq, selectOption, inqLineNumber, lineNumber);

				lineNumber++;
			}
		}
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(Envelope);
		transformer.transform(source, result);
		String xmlString = sw.toString();
		return xmlString;

	}

	private void addPartResponse(Document doc, Element Quote, Envelope envData, InquiryResponsePart inqRespPartinq, SelectOption selectOp,
					int inqLineNumber, int lineNumber) {
		Element Line = createElement("ow-o:Line", Quote, doc);
		Element LineNumber = createElement("oa:LineNumber", Line, doc);
		LineNumber.setTextContent(String.valueOf(lineNumber));
		Element OrderItem = createElement("ow-o:OrderItem", Line, doc);
		Element ItemIds = createElement("oa:ItemIds", OrderItem, doc);
		Element SupplierItemId = createElement("oa:SupplierItemId", ItemIds, doc);
		createElement("oa:Id", SupplierItemId, doc, String.valueOf(inqRespPartinq.getPart()));
		try {
			createElement("oa:ItemType",
							OrderItem,
							doc,
							String.valueOf(envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine().get(inqLineNumber)
											.getOrderItem().getItemType()));
		} catch (Exception e) {
			createElement("oa:ItemType", OrderItem, doc, String.valueOf("Part"));
		}

		Element ItemInfo = createElement("ow-o:ItemInfo", OrderItem, doc);
		Element ManufacturerInfo = createElement("ow-o:ManufacturerInfo", ItemInfo, doc);
		createElement("ow-o:SupplierManufacturer", ManufacturerInfo, doc, inqRespPartinq.getLineCode());
		Element AdditionalInfo = createElement("ow-o:AdditionalInfo", ItemInfo, doc);
		createElement("ow-o:InfoName", AdditionalInfo, doc, "");
		createElement("ow-o:InfoValue", AdditionalInfo, doc, "");
		Element QuantityInfo = createElement("ow-o:QuantityInfo", OrderItem, doc);

		Element RequestedQuantity = createElement("ow-o:RequestedQuantity", QuantityInfo, doc);
		RequestedQuantity.setAttribute("uom", "EACH");
		RequestedQuantity.setTextContent(String.valueOf(inqRespPartinq.getQty()));
		Element AvailableQuantity = createElement("ow-o:AvailableQuantity", QuantityInfo, doc);
		AvailableQuantity.setAttribute("uom", "EACH");
		AvailableQuantity.setTextContent(String.valueOf(selectOp.getQuantity().getAvailable()));
		
		Element PriceInfo = createElement("ow-o:PriceInfo", OrderItem, doc);
		Element ListPrice = createElement("ow-o:ListPrice", PriceInfo, doc);
		Element Amount = createElement("oa:Amount", ListPrice, doc);
		Amount.setAttribute("currency", "USD");

		Element PerQuantity = createElement("oa:PerQuantity", ListPrice, doc);
		PerQuantity.setAttribute("uom", "EACH");
		PerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPerCarQty()));
		Element CorePrice = createElement("ow-o:CorePrice", PriceInfo, doc);
		Element CPAmount = createElement("oa:Amount", CorePrice, doc);
		CPAmount.setAttribute("currency", "USD");
		
		try {
			CPAmount.setTextContent(String.valueOf(selectOp.getPrice().getCoreCost()));
			Amount.setTextContent(String.valueOf(selectOp.getPrice().getList()));
		} catch (Exception e) {
			try {
				CPAmount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getCoreCost()));
				Amount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getList()));
			}catch (Exception e1) {
				Amount.setTextContent("0.0");
				CPAmount.setTextContent("0.0");
			} 
		}
		
		Element CPPerQuantity = createElement("oa:PerQuantity", CorePrice, doc);
		CPPerQuantity.setAttribute("uom", "EACH");
		CPPerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPerCarQty()));
		Element OrderInfo = createElement("ow-o:OrderInfo", OrderItem, doc);
		try {
			String temp = envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine().get(inqLineNumber).getOrderItem()
							.getRequestLineGUID();

			if (temp != null && !temp.isEmpty()) {
				Element RequestLineGUID = createElement("ow-o:RequestLineGUID", OrderItem, doc);
				RequestLineGUID.setTextContent(temp);
			}
		} catch (Exception e) {
		}
		Element SupplierLocationId = createElement("ow-o:SupplierLocationId", OrderInfo, doc);
		try {
			SupplierLocationId.setTextContent(selectOp.getDisplay());
		} catch (Exception e) {
			SupplierLocationId.setTextContent("");
		}
		Element UnitPrice = createElement("oa:UnitPrice", Line, doc);
		Element UPAmount = createElement("oa:Amount", UnitPrice, doc);
		UPAmount.setAttribute("currency", "USD");
		try {
			UPAmount.setTextContent(String.valueOf(selectOp.getPrice().getCost()));
		} catch (Exception e) {

			try {
				UPAmount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getCost()));
			} catch (Exception e1) {
				UPAmount.setTextContent("0.0");
			}
		}
		Element UPPerQuantity = createElement("oa:PerQuantity", UnitPrice, doc);
		UPPerQuantity.setAttribute("uom", "EACH");
		try {
			UPPerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPerCarQty()));
		} catch (Exception e) {
			UPPerQuantity.setTextContent("1");
		}

		if (inqRespPartinq.getDescription() != null) {
			createElement("oa:Description", Line, doc, inqRespPartinq.getDescription());
		} else {
			createElement("oa:Description", Line, doc, "");
		}
		Element OWOrderStatus = createElement("ow-o:OrderStatus", Line, doc);
		createElement("oa:Code", OWOrderStatus, doc, "Open");
		createElement("oa:Description", OWOrderStatus, doc, "");
		createElement("ow-o:Status", OWOrderStatus, doc, "success");
		Element DocumentReferences = createElement("oa:DocumentReferences", Line, doc);
		Element RFQDocumentReference = createElement("oa:RFQDocumentReference", DocumentReferences, doc);

		try {
			createElement("oa:LineNumber",
							RFQDocumentReference,
							doc,
							String.valueOf(envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine().get(inqLineNumber)
											.getLineNumber()));
		} catch (Exception e) {
			createElement("oa:LineNumber", RFQDocumentReference, doc, String.valueOf(""));
		}
		
		if(selectOp.getNetwork() == 100){

			for (Iterator<InquiryResponsePart> iterator = inqRespPartinq.getAlternateParts().iterator(); iterator.hasNext();) {
				InquiryResponsePart part = iterator.next();

				for (Iterator<SelectOption> iterator2 = part.getLocations().iterator(); iterator2.hasNext();) {
					SelectOption selectOptionAlt = iterator2.next();

					addAltPartResponse(doc, Quote, envData, part, selectOptionAlt, inqLineNumber, lineNumber, Line);
				}
			}
		}		
	}

	private void addAltPartResponse(Document doc, Element Quote, Envelope envData, InquiryResponsePart inqRespPartinq, SelectOption selectOp,
					int inqLineNumber, int lineNumber, Element Line) {
		Element altLine = createElement("ow-o:AlternateLine", Line, doc);
		Element OrderItem = createElement("ow-o:OrderItem", altLine, doc);
		Element ItemIds = createElement("oa:ItemIds", OrderItem, doc);
		Element SupplierItemId = createElement("oa:SupplierItemId", ItemIds, doc);
		createElement("oa:Id", SupplierItemId, doc, String.valueOf(inqRespPartinq.getPart()));
		try {
			createElement("oa:ItemType",
							OrderItem,
							doc,
							String.valueOf(envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine().get(inqLineNumber)
											.getOrderItem().getItemType()));
		} catch (Exception e) {
			createElement("oa:ItemType", OrderItem, doc, String.valueOf("Part"));
		}

		Element ItemInfo = createElement("ow-o:ItemInfo", OrderItem, doc);
		Element ManufacturerInfo = createElement("ow-o:ManufacturerInfo", ItemInfo, doc);
		createElement("ow-o:SupplierManufacturer", ManufacturerInfo, doc, inqRespPartinq.getLineCode());
		Element QuantityInfo = createElement("ow-o:QuantityInfo", OrderItem, doc);
		Element RequestedQuantity = createElement("ow-o:RequestedQuantity", QuantityInfo, doc);
		RequestedQuantity.setAttribute("uom", "EACH");
		RequestedQuantity.setTextContent(String.valueOf(inqRespPartinq.getQty()));
		Element AvailableQuantity = createElement("ow-o:AvailableQuantity", QuantityInfo, doc);
		AvailableQuantity.setAttribute("uom", "EACH");
		AvailableQuantity.setTextContent(String.valueOf(selectOp.getQuantity().getAvailable()));

		Element PriceInfo = createElement("ow-o:PriceInfo", OrderItem, doc);
		Element ListPrice = createElement("ow-o:ListPrice", PriceInfo, doc);
		Element Amount = createElement("oa:Amount", ListPrice, doc);
		Amount.setAttribute("currency", "USD");

		Element PerQuantity = createElement("oa:PerQuantity", ListPrice, doc);
		PerQuantity.setAttribute("uom", "EACH");
		PerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPerCarQty()));
		Element CorePrice = createElement("ow-o:CorePrice", PriceInfo, doc);
		Element CPAmount = createElement("oa:Amount", CorePrice, doc);
		CPAmount.setAttribute("currency", "USD");
		
		try {
			Amount.setTextContent(String.valueOf(selectOp.getPrice().getList()));
			CPAmount.setTextContent(String.valueOf(selectOp.getPrice().getCoreCost()));
		} catch (Exception e) {
			try {
				Amount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getList()));
				CPAmount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getCoreCost()));
			}catch (Exception e1) {
				Amount.setTextContent("0.0");
				CPAmount.setTextContent("0.0");
			} 
		}
		
		Element CPPerQuantity = createElement("oa:PerQuantity", CorePrice, doc);
		CPPerQuantity.setAttribute("uom", "EACH");
		CPPerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPerCarQty()));
		Element OrderInfo = createElement("ow-o:OrderInfo", OrderItem, doc);
		try {
			String temp = envData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine().get(inqLineNumber).getOrderItem()
							.getRequestLineGUID();

			if (temp != null && !temp.isEmpty()) {
				Element RequestLineGUID = createElement("ow-o:RequestLineGUID", OrderItem, doc);
				RequestLineGUID.setTextContent(temp);
			}
		} catch (Exception e) {
		}
		Element SupplierLocationId = createElement("ow-o:SupplierLocationId", OrderInfo, doc);
		try {
			SupplierLocationId.setTextContent(selectOp.getDisplay());
		} catch (Exception e) {
			SupplierLocationId.setTextContent("");
		}
		Element UnitPrice = createElement("oa:UnitPrice", altLine, doc);
		Element UPAmount = createElement("oa:Amount", UnitPrice, doc);
		UPAmount.setAttribute("currency", "USD");
		try {
			UPAmount.setTextContent(String.valueOf(selectOp.getPrice().getCost()));
		} catch (Exception e) {
			try {
				UPAmount.setTextContent(String.valueOf(inqRespPartinq.getPrice100().getCost()));
			} catch (Exception e1) {
				UPAmount.setTextContent("0.0");
			}
		}
		Element UPPerQuantity = createElement("oa:PerQuantity", UnitPrice, doc);
		UPPerQuantity.setAttribute("uom", "EACH");
		try {
			UPPerQuantity.setTextContent(String.valueOf(inqRespPartinq.getPerCarQty()));
		} catch (Exception e) {
			UPPerQuantity.setTextContent("1");
		}

		if (inqRespPartinq.getDescription() != null) {
			createElement("oa:Description", altLine, doc, inqRespPartinq.getDescription());
		} else {
			createElement("oa:Description", altLine, doc, "");
		} 
		Element altType = createElement("ow-o:AlternateType", altLine, doc);
		altType.setTextContent("alt");
	}

}
