package com.alliance.ows.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.alliance.ows.model.inquire.AddRequestForQuote;
import com.alliance.ows.model.inquire.ApplicationArea;
import com.alliance.ows.model.inquire.Body;
import com.alliance.ows.model.inquire.Business;
import com.alliance.ows.model.inquire.CatalogInfo;
import com.alliance.ows.model.inquire.CustomerDocumentId;
import com.alliance.ows.model.inquire.CustomerParty;
import com.alliance.ows.model.inquire.DataArea;
import com.alliance.ows.model.inquire.DocumentIds;
import com.alliance.ows.model.inquire.EndPoints;
import com.alliance.ows.model.inquire.Envelope;
import com.alliance.ows.model.inquire.From;
import com.alliance.ows.model.inquire.Header;
import com.alliance.ows.model.inquire.ItemIds;
import com.alliance.ows.model.inquire.ItemInfo;
import com.alliance.ows.model.inquire.Line;
import com.alliance.ows.model.inquire.ManufacturerInfo;
import com.alliance.ows.model.inquire.OaHeader;
import com.alliance.ows.model.inquire.OrderItem;
import com.alliance.ows.model.inquire.Parties;
import com.alliance.ows.model.inquire.PartyId;
import com.alliance.ows.model.inquire.ProcessMessage;
import com.alliance.ows.model.inquire.Properties;
import com.alliance.ows.model.inquire.QuantityInfo;
import com.alliance.ows.model.inquire.RequestForQuote;
import com.alliance.ows.model.inquire.SOAPBody;
import com.alliance.ows.model.inquire.SOAPBodyEnvelope;
import com.alliance.ows.model.inquire.SOAPEnvelop;
import com.alliance.ows.model.inquire.SecurityInfo;
import com.alliance.ows.model.inquire.Sender;
import com.alliance.ows.model.inquire.ShipToParty;
import com.alliance.ows.model.inquire.SizeInfo;
import com.alliance.ows.model.inquire.SupplierItemId;
import com.alliance.ows.model.inquire.SupplierParty;
import com.alliance.ows.model.inquire.To;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class InquiryHandler extends DefaultHandler {

	private SOAPEnvelop soapEnvelop = null;
	private SOAPBody soapBody = null;
	private ProcessMessage processMessage = null;
	private SOAPBodyEnvelope soapBodyEnvelope = null;

	private Envelope envelopeData = null;

	private Header headerData = null;
	private EndPoints endPoints = null;
	private Properties properties = null;
	private SecurityInfo securityInfo = null;
	private To to = null;
	private From from = null;

	private Body bodyData = null;
	private AddRequestForQuote addReqForQuote = null;
	private ApplicationArea applicationArea = null;
	private Sender sender = null;
	private DataArea dataArea = null;
	private RequestForQuote requestForQuote = null;
	private OaHeader oaHeader = null;
	private DocumentIds documentIds = null;
	private CustomerDocumentId customerDocumentId = null;
	private Parties parties = null;
	private CustomerParty customerParty = null;
	private PartyId partyId = null;
	private Business bussiness = null;

	private Line line = null;
	private OrderItem orderItem = null;
	private ItemIds itemIds = null;
	private SupplierItemId supplierItemId = null;

	private ItemInfo itemInfo = null;
	private CatalogInfo catalogInfo = null;
	private ManufacturerInfo manufacturerInfo = null;

	private SizeInfo sizeInfo = null;
	private QuantityInfo quantityInfo = null;

	private boolean toId = false;
	private boolean fromId = false;
	private boolean customerDocId = false;
	private boolean supplierItId = false;
	private List<Line> lineData = new ArrayList<Line>();

	private String elementName = "";
	private String content = "";
	private String partyFlag = "";
	private SupplierParty supplierParty = null;
	private ShipToParty shipToParty = null;

	private boolean customerPartyId = false;
	private boolean supplierPartyId = false;
	private boolean shipToPartyId = false;
	private boolean customerBussId = false;
	private boolean supplierBussId = false;
	private boolean shipToBussId = false;

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {

		if (envelopeData == null) {
			envelopeData = new Envelope();
		}
		if (addReqForQuote == null) {
			addReqForQuote = new AddRequestForQuote();
		}
		if (prefix.equalsIgnoreCase("ow-e")) {
			envelopeData.setEnvAttrOweName("xmlns:" + prefix);
			envelopeData.setEnvAttrOweValue(uri);

		} else if (prefix.equalsIgnoreCase("ow-o")) {
			envelopeData.setEnvAttrOwoName("xmlns:" + prefix);
			envelopeData.setEnvAttrOwoValue(uri);
		} else if (prefix.equalsIgnoreCase("oa")) {
			envelopeData.setEnvAttrOaName("xmlns:" + prefix);
			envelopeData.setEnvAttrOaValue(uri);
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
		// instantiating objects for header
		case "ow-e:Header":
			headerData = new Header();
			envelopeData.setHeader(headerData);
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
			bodyData = new Body();
			envelopeData.setBody(bodyData);
			break;
		case "ow-o:AddRequestForQuote":
			bodyData.setAddReqForQuote(addReqForQuote);
			break;
		case "oa:ApplicationArea":
			applicationArea = new ApplicationArea();
			addReqForQuote.setApplicationArea(applicationArea);
			break;
		case "oa:Sender":
			sender = new Sender();
			applicationArea.setSender(sender);
			break;
		case "oa:DataArea":
			dataArea = new DataArea();
			addReqForQuote.setDataArea(dataArea);
			break;
		case "oa:RequestForQuote":
			requestForQuote = new RequestForQuote();
			dataArea.setRequestForQuote(requestForQuote);
			break;
		case "oa:Header":
			oaHeader = new OaHeader();
			requestForQuote.setOaHeader(oaHeader);
			break;
		case "oa:DocumentIds":
			documentIds = new DocumentIds();
			oaHeader.setDocuments(documentIds);
			break;
		case "oa:CustomerDocumentId":
			customerDocId = true;
			customerDocumentId = new CustomerDocumentId();
			documentIds.setCustomerDocumentId(customerDocumentId);
			break;
		case "oa:Parties":
			parties = new Parties();
			oaHeader.setParties(parties);
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
		case "oa:PartyId":
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
			orderItem = new OrderItem();
			line.setOrderItem(orderItem);
			break;
		case "oa:ItemIds":
			itemIds = new ItemIds();
			orderItem.setItemId(itemIds);
			break;
		case "oa:SupplierItemId":
			supplierItId = true;
			supplierItemId = new SupplierItemId();
			itemIds.setSupplierItemId(supplierItemId);
			break;
		case "ow-o:ItemInfo":
			itemInfo = new ItemInfo();
			orderItem.setItemInfo(itemInfo);
			break;
		case "ow-o:CatalogInfo":
			catalogInfo = new CatalogInfo();
			itemInfo.setCatalogInfo(catalogInfo);
			break;
		case "ow-o:ManufacturerInfo":
			manufacturerInfo = new ManufacturerInfo();
			itemInfo.setManufacturerInfo(manufacturerInfo);
			break;
		case "ow-o:SizeInfo":
			sizeInfo = new SizeInfo();
			itemInfo.setSizeInfo(sizeInfo);
			break;
		case "ow-o:QuantityInfo":
			quantityInfo = new QuantityInfo();
			orderItem.setQuantityInfo(quantityInfo);
			break;

		}
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(name);

			if ((envelopeData.getEnvAttrRevName() == null || envelopeData.getEnvAttrRevValue() == null)
							&& qName.equalsIgnoreCase("ow-o:AddRequestForQuote") && name.equalsIgnoreCase("revision")) {
				envelopeData.setEnvAttrRevName(name);
				envelopeData.setEnvAttrRevValue(value);
			} else if (name.equalsIgnoreCase("confirm")) {
				dataArea.setAddAttrName(name);
				dataArea.setAddAttrVal(value);
			} else if (name.equalsIgnoreCase("uom")) {
				quantityInfo.setUomAttrName(name);
				quantityInfo.setUomAttrValue(value);
			} else if (qName.equalsIgnoreCase("ow-e:Envelope") && name.equalsIgnoreCase("revision")) {
				envelopeData.setEnvAttrRevName(name);
				envelopeData.setEnvAttrRevValue(value);
			}
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "oa:RequestForQuote":
			requestForQuote.setLine(lineData);
			break;
		default:
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
		if (elementName.equalsIgnoreCase("ow-e:Id") && toId) {
			to.setId(content);
			toId = false;
		} else if (elementName.equalsIgnoreCase("ow-e:Id") && fromId) {
			from.setId(content);
			fromId = false;
		} else if (elementName.equalsIgnoreCase("oa:Id") && customerBussId) {
			bussiness = new Business();
			bussiness.setId(content);
			customerParty.setBussiness(bussiness);
			customerBussId = false;
		} else if (elementName.equalsIgnoreCase("oa:Id") && supplierBussId) {
			bussiness = new Business();
			bussiness.setId(content);
			supplierParty.setBussiness(bussiness);
			supplierBussId = false;
		} else if (elementName.equalsIgnoreCase("oa:Id") && shipToBussId) {
			bussiness = new Business();
			bussiness.setId(content);
			shipToParty.setBussiness(bussiness);
			shipToBussId = false;

		} else if (elementName.equalsIgnoreCase("oa:Id") && customerPartyId) {
			partyId = new PartyId();
			partyId.setId(content);
			customerParty.setPartyId(partyId);
			customerPartyId = false;

		} else if (elementName.equalsIgnoreCase("oa:Id") && supplierPartyId) {
			partyId = new PartyId();
			partyId.setId(content);
			supplierParty.setPartyId(partyId);
			supplierPartyId = false;

		} else if (elementName.equalsIgnoreCase("oa:Id") && shipToPartyId) {
			partyId = new PartyId();
			partyId.setId(content);
			shipToParty.setPartyId(partyId);
			shipToPartyId = false;

		} else if (elementName.equalsIgnoreCase("oa:Id") && supplierItId) {
			supplierItemId.setId(content);
			supplierItId = false;

		} else if (elementName.equalsIgnoreCase("oa:Id") && customerDocId) {
			customerDocumentId.setId(content);
			customerDocId = false;
		} else if (elementName.equalsIgnoreCase("oa:LineNumber")) {
			line.setLineNumber(content);
		} else if (elementName.equalsIgnoreCase("oa:Component")) {
			sender.setComponent(content);
		} else if (elementName.equalsIgnoreCase("oa:CreationDateTime")) {
			applicationArea.setCreationDateTime(content);
		} else if (elementName.equalsIgnoreCase("oa:ItemType")) {
			orderItem.setItemType(content);
		} else if (elementName.equalsIgnoreCase("ow-o:CatalogName")) {
			catalogInfo.setCatalogName(content);
		} else if (elementName.equalsIgnoreCase("ow-o:CatalogVersion")) {
			catalogInfo.setCatalogVersion(content);
		} else if (elementName.equalsIgnoreCase("ow-o:CatalogPublisher")) {
			catalogInfo.setCatalogPublisher(content);
		} else if (elementName.equalsIgnoreCase("ow-o:UniversalManufacturer")) {
			manufacturerInfo.setUniversalManufacturer(content);
		} else if (elementName.equalsIgnoreCase("ow-o:SupplierManufacturer")) {
			manufacturerInfo.setSupplierManufacturer(content);
		} else if (elementName.equalsIgnoreCase("ow-o:CustomerManufacturer")) {
			manufacturerInfo.setCustomerManufacturer(content);
		} else if (elementName.equalsIgnoreCase("ow-o:RequestLineGUID")) {
			orderItem.setRequestLineGUID(content);
		} else if (elementName.equalsIgnoreCase("ow-o:UniversalSize")) {
			sizeInfo.setUniversalSize(content);
		} else if (elementName.equalsIgnoreCase("ow-o:SpeedRating")) {
			itemInfo.setSpeedRating(content);
		} else if (elementName.equalsIgnoreCase("ow-o:LoadRating")) {
			itemInfo.setLoadRating(content);
		} else if (elementName.equalsIgnoreCase("ow-o:RequestedQuantity")) {
			quantityInfo.setRequestedQuantity(content);
		} else if (elementName.equalsIgnoreCase("ow-e:SentAt")) {
			properties.setSentAt(content);
		} else if (elementName.equalsIgnoreCase("ow-e:Topic")) {
			properties.setTopic(content);
		} else if (elementName.equalsIgnoreCase("ow-e:Identity")) {
			properties.setIdentity(content);
		} else if (elementName.equalsIgnoreCase("ow-e:Username")) {
			securityInfo.setUserName(content);
		} else if (elementName.equalsIgnoreCase("ow-e:Password")) {
			securityInfo.setPassword(content);
		}
		elementName = "";

	}

	public Envelope getEnvelopeData() {
		return envelopeData;
	}

	public SOAPEnvelop getSOAPEnvelop() {
		return soapEnvelop;
	}

}
