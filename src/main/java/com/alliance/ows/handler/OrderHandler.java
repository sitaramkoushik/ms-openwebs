package com.alliance.ows.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alliance.ows.model.inquire.Envelope;
import com.alliance.ows.model.inquire.ProcessMessage;
import com.alliance.ows.model.inquire.SOAPBody;
import com.alliance.ows.model.inquire.SOAPBodyEnvelope;
import com.alliance.ows.model.inquire.SOAPEnvelop;
import com.alliance.ows.model.order.Addresses;
import com.alliance.ows.model.order.ApplicationArea;
import com.alliance.ows.model.order.Business;
import com.alliance.ows.model.order.CustomerDocumentId;
import com.alliance.ows.model.order.CustomerParty;
import com.alliance.ows.model.order.DataArea;
import com.alliance.ows.model.order.DocumentId;
import com.alliance.ows.model.order.DocumentIds;
import com.alliance.ows.model.order.EndPoints;
import com.alliance.ows.model.order.From;
import com.alliance.ows.model.order.ItemIds;
import com.alliance.ows.model.order.ItemInfo;
import com.alliance.ows.model.order.Line;
import com.alliance.ows.model.order.ManufacturerInfo;
import com.alliance.ows.model.order.OrdBody;
import com.alliance.ows.model.order.OrdHeader;
import com.alliance.ows.model.order.OrderInfo;
import com.alliance.ows.model.order.OrderItem;
import com.alliance.ows.model.order.OrderItemInfo;
import com.alliance.ows.model.order.OwoHeader;
import com.alliance.ows.model.order.Parties;
import com.alliance.ows.model.order.PartyId;
import com.alliance.ows.model.order.ProcessPurchaseOrder;
import com.alliance.ows.model.order.Properties;
import com.alliance.ows.model.order.PurchaseOrder;
import com.alliance.ows.model.order.SecurityInfo;
import com.alliance.ows.model.order.Sender;
import com.alliance.ows.model.order.ShipToParty;
import com.alliance.ows.model.order.ShippingAddress;
import com.alliance.ows.model.order.ShippingInfo;
import com.alliance.ows.model.order.SupplierDocumentId;
import com.alliance.ows.model.order.SupplierItemId;
import com.alliance.ows.model.order.SupplierParty;
import com.alliance.ows.model.order.To;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OrderHandler extends DefaultHandler {
	private SOAPEnvelop soapEnvelop = null;
	private SOAPBody soapBody = null;
	private ProcessMessage processMessage = null;
	private SOAPBodyEnvelope soapBodyEnvelope = null;
	
	private Envelope envelopeData = null;
	private OrdHeader headerData = null;
	private EndPoints endPoints = null;
	private Properties properties = null;
	private SecurityInfo securityInfo = null;
	private To to = null;
	private From from = null;
	private OrdBody bodyData = null;
	private ApplicationArea applicationArea = null;
	private Sender sender = null;
	private DataArea dataArea = null;
	private PurchaseOrder purchaseOrder = null;
	private OwoHeader owoHeader = null;
	private DocumentIds documentIds = null;
	private CustomerDocumentId customerDocumentId = null;
	private SupplierDocumentId supplierDocumentId = null;
	private DocumentId documentId = null;
	private Parties parties = null;
	private CustomerParty customerParty = null;
	private SupplierParty supplierParty = null;
	private ShipToParty shipToParty = null;
	private Addresses addresses = null;
	private ShippingAddress shippingAddress = null;
	private PartyId partyId = null;
	private Business bussiness = null;
	private OrderInfo orderInfo = null;
	private ShippingInfo shippingInfo = null;
	private Line line = null;
	private OrderItem orderItem = null;
	private OrderItemInfo orderItemInfo = null;
	private ItemIds itemIds = null;
	private SupplierItemId supplierItemId = null;
	private ItemInfo itemInfo = null;
	private ManufacturerInfo manufacturerInfo = null;

	private ProcessPurchaseOrder processPurchaseOrder = null;

	private List<Line> lineData = new ArrayList<Line>();
	private List<String> addressLine = new ArrayList<String>();
	private List<String> comment = new ArrayList<String>();
	private String content = "";
	private String partyFlag = "";
	private String elementName = "";

	private boolean toId = false;
	private boolean fromId = false;
	private boolean customerDocId = false;
	private boolean supplierDocId = false;
	private boolean docId = false;
	private boolean supplierItId = false;
	private boolean customerPartyId = false;
	private boolean supplierPartyId = false;
	private boolean shipToPartyId = false;
	private boolean customerBussId = false;
	private boolean supplierBussId = false;
	private boolean shipToBussId = false;
	private boolean orderitemInfo = false;

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		System.out.println("startPrefixMapping, prefix: " + prefix + ", uri: " + uri);
		if (prefix.equalsIgnoreCase("ow-e")) {
			envelopeData = new Envelope();
			envelopeData.setEnvAttrName("xmlns:" + prefix);
			envelopeData.setEnvAttrValue(uri);
		} else if (prefix.equalsIgnoreCase("ow-o")) {
			processPurchaseOrder = new ProcessPurchaseOrder();
			processPurchaseOrder.setPpoAttrName("xmlns:" + prefix);
			processPurchaseOrder.setPpoAttrValue(uri);
		} else if (prefix.equalsIgnoreCase("oa")) {
			processPurchaseOrder.setPpoAttrOaName("xmlns:" + prefix);
			processPurchaseOrder.setPpoAttrOaValue(uri);
		}

	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		elementName = qName;
		switch (qName) {
		case "soap:Envelope":
			soapEnvelop = new SOAPEnvelop();
			break;
		case "soap:Body":
			soapBody = new SOAPBody();
			soapEnvelop.setSoapBody(soapBody);
			break;
		case "ProcessMessage":
			processMessage = new ProcessMessage();
			soapBody.setProcessMessage(processMessage);
			break;
		case "envelope":
			soapBodyEnvelope = new SOAPBodyEnvelope();
			processMessage.setBodyEnvelope(soapBodyEnvelope);

			soapBodyEnvelope.setEnvelope(envelopeData);
			break;
		// header initialization
		case "ow-e:Header":
			headerData = new OrdHeader();
			envelopeData.setOrdHeader(headerData);
			break;
		case "ow-e:EndPoints":
			endPoints = new EndPoints();
			headerData.setEndPoints(endPoints);
			break;
		case "ow-e:Properties":
			properties = new Properties();
			headerData.setProperties(properties);
			break;
		case "ow-e:SecurityInfo":
			securityInfo = new SecurityInfo();
			headerData.setSecurityInfo(securityInfo);
			break;
		case "ow-e:To":
			toId = true;
			to = new To();
			endPoints.setTo(to);
			break;
		case "ow-e:From":
			fromId = true;
			from = new From();
			endPoints.setFrom(from);
			break;
		case "ow-e:Body":
			bodyData = new OrdBody();
			envelopeData.setOrdBody(bodyData);
			break;
		case "ow-o:ProcessPurchaseOrder":
			bodyData.setProcessPurchaseOrder(processPurchaseOrder);
			break;
		case "oa:ApplicationArea":
			applicationArea = new ApplicationArea();
			processPurchaseOrder.setApplicationArea(applicationArea);
			break;
		case "oa:Sender":
			sender = new Sender();
			applicationArea.setSender(sender);
			break;
		case "oa:DataArea":
			dataArea = new DataArea();
			processPurchaseOrder.setDataArea(dataArea);
			break;
		case "oa:PurchaseOrder":
			purchaseOrder = new PurchaseOrder();
			dataArea.setPurchaseOrder(purchaseOrder);
		case "oa:Header":
			owoHeader = new OwoHeader();
			purchaseOrder.setOwoHeader(owoHeader);
			break;
		case "oa:DocumentIds":
			documentIds = new DocumentIds();
			owoHeader.setDocuments(documentIds);
			break;
		case "oa:CustomerDocumentId":
			customerDocId = true;
			customerDocumentId = new CustomerDocumentId();
			documentIds.setCustomerDocumentId(customerDocumentId);
			break;
		case "oa:SupplierDocumentId":
			supplierDocId = true;
			supplierDocumentId = new SupplierDocumentId();
			documentIds.setSupplierDocumentId(supplierDocumentId);
			break;
		case "oa:DocumentId":
			docId = true;
			documentId = new DocumentId();
			documentIds.setDocumentId(documentId);
		case "oa:Parties":
			parties = new Parties();
			owoHeader.setParties(parties);
			break;
		case "ow-o:OrderInfo":
			if (orderitemInfo) {
				orderItemInfo = new OrderItemInfo();
				orderItem.setOrderInfo(orderItemInfo);
				break;
			} else {
				orderInfo = new OrderInfo();
				owoHeader.setOrderInfo(orderInfo);
				break;
			}
		case "ow-o:ShippingInfo":
			shippingInfo = new ShippingInfo();
			orderInfo.setShippingInfo(shippingInfo);
			break;
		case "ow-o:CustomerParty":
			customerPartyId = true;
			partyFlag = qName;
			customerParty = new CustomerParty();
			parties.setCustomerParty(customerParty);
			break;
		case "ow-o:SupplierParty":
			supplierPartyId = true;
			partyFlag = qName;
			supplierParty = new SupplierParty();
			parties.setSupplierParty(supplierParty);
			break;
		case "ow-o:ShipToParty":
			partyFlag = qName;
			shipToPartyId = true;
			shipToParty = new ShipToParty();
			parties.setShipToParty(shipToParty);
			break;
		case "oa:Addresses":
			addresses = new Addresses();
			shipToParty.setAddresses(addresses);
			break;
		case "oa:ShippingAddress":
			shippingAddress = new ShippingAddress();
			addresses.setShippingAddress(shippingAddress);
			break;
		case "oa:Business":
			supplierBussId = false;
			shipToBussId = false;
			customerBussId = false;
			if (partyFlag.equalsIgnoreCase("ow-o:SupplierParty")) {
				supplierBussId = true;
			} else if (partyFlag.equalsIgnoreCase("ow-o:CustomerParty")) {
				customerBussId = true;
			} else if (partyFlag.equalsIgnoreCase("ow-o:ShipToParty")) {
				shipToBussId = true;
			}
			partyFlag = "";
			break;
		case "oa:Line":
			line = new Line();
			lineData.add(line);
			break;
		case "ow-o:OrderItem":
			orderitemInfo = true;
			orderItem = new OrderItem();
			line.setOrderItem(orderItem);
			break;
		case "oa:ItemIds":
			itemIds = new ItemIds();
			orderItem.setItemId(itemIds);
			break;
		case "oa:SupplierItemId":
			supplierItId = true;
			shipToPartyId = false ;
			supplierItemId = new SupplierItemId();
			itemIds.setSupplierItemId(supplierItemId);
			break;
		case "ow-o:ItemInfo":
			itemInfo = new ItemInfo();
			orderItem.setItemInfo(itemInfo);
			break;
		case "ow-o:ManufacturerInfo":
			manufacturerInfo = new ManufacturerInfo();
			itemInfo.setManufacturerInfo(manufacturerInfo);
			break;

		}
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(name);

			if (qName.equalsIgnoreCase("ow-o:ProcessPurchaseOrder") && name.equalsIgnoreCase("revision")) {
				processPurchaseOrder.setPpoAttrRevName(name);
				processPurchaseOrder.setPpoAttrRevValue(value);
			} else if (name.equalsIgnoreCase("confirm")) {
				dataArea.setProcessAttrName(name);
				dataArea.setProcessAttrValue(value);
			} else if (qName.equalsIgnoreCase("ow-e:Envelope") && name.equalsIgnoreCase("revision")) {
				envelopeData.setEnvAttrRevName(name);
				envelopeData.setEnvAttrRevValue(value);
			} else if (qName.equalsIgnoreCase("oa:Process")) {
				dataArea.setProcessAttrName(name);
				dataArea.setProcessAttrValue(value);
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "oa:PurchaseOrder":
			purchaseOrder.setLine(lineData);
			break;
		case "oa:ShippingAddress":
			shippingAddress.setAddressLine(addressLine);
			break;
		case "ow-o:OrderInfo":
			orderInfo.setComment(comment);
			break;

		default:
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
		switch (elementName) {

		case "ow-e:Id":
			if (toId) {
				to.setId(content);
				toId = false;
			} else if (fromId) {
				from.setId(content);
				fromId = false;
			}
			break;
		case "ow-e:SentAt":
			properties.setSentAt(content);
			break;
		case "ow-e:Topic":
			properties.setTopic(content);
			break;
		case "ow-e:Identity":
			properties.setIdentity(content);
			break;
		case "ow-e:Username":
			securityInfo.setUserName(content);
			break;
		case "ow-e:Password":
			securityInfo.setPassword(content);
			break;
		case "oa:Component":
			sender.setComponent(content);
			break;
		case "oa:CreationDateTime":
			applicationArea.setCreationDateTime(content);
			break;

		case "oa:Id":
			if (customerBussId) {
				bussiness = new Business();
				bussiness.setId(content);
				customerParty.setBussiness(bussiness);
				customerBussId = false;
			} else if (supplierBussId) {
				bussiness = new Business();
				bussiness.setId(content);
				supplierParty.setBussiness(bussiness);
				supplierBussId = false;
			} else if (shipToBussId) {
				bussiness = new Business();
				bussiness.setId(content);
				shipToParty.setBussiness(bussiness);
				shipToBussId = false;

			} else if (customerPartyId) {
				partyId = new PartyId();
				partyId.setId(content);
				customerParty.setPartyId(partyId);
				customerPartyId = false;

			} else if (supplierPartyId) {
				partyId = new PartyId();
				partyId.setId(content);
				supplierParty.setPartyId(partyId);
				supplierPartyId = false;

			} else if (shipToPartyId) {
				partyId = new PartyId();
				partyId.setId(content);
				shipToParty.setPartyId(partyId);
				shipToPartyId = false;

			} else if (supplierItId) {
				supplierItemId.setId(content);
				supplierItId = false;

			} else if (customerDocId) {
				customerDocumentId.setId(content);
				customerDocId = false;
			} else if (docId) {
				documentId.setId(content);
				docId = false;
			} else if (supplierDocId) {
				supplierDocumentId.setId(content);
			}
			break;
		case "oa:Name":
			shipToParty.setName(content);
			break;
		case "oa:AddressLine":
			addressLine.add(content);
			break;
		case "oa:City":
			shippingAddress.setCity(content);
			break;
		case "oa:StateOrProvince":
			shippingAddress.setStateOrProvince(content);
			break;
		case "oa:PostalCode":
			shippingAddress.setPostalCode(content);
			break;
		case "ow-o:RequestType":
			orderInfo.setRequestType(content);
			break;
		case "ow-o:FillFlag":
			orderInfo.setFillFlag(content);
			break;
		case "ow-o:Comment":
			comment.add(content);
			break;
		case "ow-o:Code":
			shippingInfo.setCode(content);
			break;
		case "ow-o:Description":
			shippingInfo.setDescription(content);
			break;

		case "oa:LineNumber":
			line.setLineNumber(content);
			break;
		case "oa:ItemType":
			orderItem.setItemType(content);
			break;
		case "ow-o:SupplierManufacturer":
			manufacturerInfo.setSupplierManufacturer(content);
			break;
		case "ow-o:RequestLineGUID":
			orderItem.setRequestLineGUID(content);
			break;
		case "oa:OrderQuantity":
			line.setOrderQuantity(content);
			break;

		case "ow-o:SupplierLocationId":
			if (orderitemInfo) {
				orderItemInfo.setSupplierLocationId(content);
				orderitemInfo = false;
			}
			break;
		}
		elementName = "";
	}

	public Envelope getOrderReqEnvelope() {
		return envelopeData;
	}

}