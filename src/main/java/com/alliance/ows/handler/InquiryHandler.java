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
import com.alliance.ows.model.inquire.Properties;
import com.alliance.ows.model.inquire.QuantityInfo;
import com.alliance.ows.model.inquire.RequestForQuote;
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

	private String sentAt = "";
	private String topic = "";
	private String identity = "";
	private String userName = "";
	private String password = "";

	private String id = "";
	private String component = "";
	private String creationDateTime = "";
	private String lineNumber = "";
	private String itemType = "";
	private String catalogName = "";
	private String catalogVersion = "";
	private String universalManufacturer = "";
	private String catalogPublisher = "";
	private String customerManufacturer = "";
	private String supplierManufacturer = "";
	private String requestLineGUID = "";
	private String universalSize = "";
	private String speedRating = "";
	private String loadRating = "";
	private String requestedQuantity = "";
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
		if (prefix.equalsIgnoreCase("ow-e")) {
			envelopeData = new Envelope();
			envelopeData.setEnvAttrName("xmlns:" + prefix);
			envelopeData.setEnvAttrValue(uri);
		} else if (prefix.equalsIgnoreCase("ow-o")) {
			addReqForQuote = new AddRequestForQuote();
			addReqForQuote.setRfqAttrName("xmlns:" + prefix);
			addReqForQuote.setRfqAttrValue(uri);
		} else if (prefix.equalsIgnoreCase("oa")) {
			addReqForQuote.setRfqAttrOaName("xmlns:" + prefix);
			addReqForQuote.setRfqAttrOaValue(uri);
		}

	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
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
		case "ow-e:SentAt":
			sentAt = qName;
			break;
		case "ow-e:Topic":
			topic = qName;
			break;
		case "ow-e:Identity":
			identity = qName;
			break;
		case "ow-e:Username":
			userName = qName;
			break;
		case "ow-e:Password":
			password = qName;
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
			id = "";
			customerPartyId = true;
			partyFlag = qName;
			customerParty = new CustomerParty();
			parties.setCustomerParty(customerParty);
			break;
		case "ow-o:SupplierParty":
			id = "";
			supplierPartyId = true;
			partyFlag = qName;
			supplierParty = new SupplierParty();
			parties.setSupplierParty(supplierParty);
			break;
		case "ow-o:ShipToParty":
			id = "";
			partyFlag = qName;
			shipToPartyId = true;
			shipToParty = new ShipToParty();
			parties.setShipToParty(shipToParty);
			break;
		case "oa:PartyId":
			break;
		case "oa:Business":
			id = "";
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
			id = "";
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

		// setting check variable to get value of fields
		case "oa:Component":
			component = qName;
			break;
		case "oa:CreationDateTime":
			creationDateTime = qName;
			break;
		case "oa:Id":
			id = qName;
			break;
		case "ow-e:Id":
			id = qName;
			break;
		case "oa:LineNumber":
			lineNumber = qName;
			break;
		case "oa:ItemType":
			itemType = qName;
			break;
		case "ow-o:CatalogName":
			catalogName = qName;
			break;
		case "ow-o:CatalogVersion":
			catalogVersion = qName;
			break;
		case "ow-o:CatalogPublisher":
			catalogPublisher = qName;
			break;
		case "ow-o:UniversalManufacturer":
			universalManufacturer = qName;
			break;
		case "ow-o:CustomerManufacturer":
			customerManufacturer = qName;
			break;
		case "ow-o:SupplierManufacturer":
			supplierManufacturer = qName;
			break;
		case "ow-o:RequestLineGUID":
			requestLineGUID = qName;
			break;
		case "ow-o:UniversalSize":
			universalSize = qName;
			break;
		case "ow-o:SpeedRating":
			speedRating = qName;
			break;
		case "ow-o:LoadRating":
			loadRating = qName;
			break;
		case "ow-o:RequestedQuantity":
			requestedQuantity = qName;
			break;

		}
		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			String name = attributes.getQName(i);
			String value = attributes.getValue(name);

			if (qName.equalsIgnoreCase("ow-o:AddRequestForQuote") && name.equalsIgnoreCase("revision")) {
				addReqForQuote.setRfqAttrRevName(name);
				addReqForQuote.setRfqAttrRevValue(value);
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
		if (id.equalsIgnoreCase("ow-e:Id") && toId) {
			to.setId(content);
			toId = false;
			id = "";
		} else if (id.equalsIgnoreCase("ow-e:Id") && fromId) {
			from.setId(content);
			fromId = false;
			id = "";
		} else if (id.equalsIgnoreCase("oa:Id") && customerBussId) {
			bussiness = new Business();
			bussiness.setId(content);
			customerParty.setBussiness(bussiness);
			customerBussId = false;
			id = "";
		} else if (id.equalsIgnoreCase("oa:Id") && supplierBussId) {
			bussiness = new Business();
			bussiness.setId(content);
			supplierParty.setBussiness(bussiness);
			supplierBussId = false;
			id = "";
		} else if (id.equalsIgnoreCase("oa:Id") && shipToBussId) {
			id = "";
			bussiness = new Business();
			bussiness.setId(content);
			shipToParty.setBussiness(bussiness);
			shipToBussId = false;

		} else if (id.equalsIgnoreCase("oa:Id") && customerPartyId) {
			id = "";
			partyId = new PartyId();
			partyId.setId(content);
			customerParty.setPartyId(partyId);
			customerPartyId = false;

		} else if (id.equalsIgnoreCase("oa:Id") && supplierPartyId) {
			id = "";
			partyId = new PartyId();
			partyId.setId(content);
			supplierParty.setPartyId(partyId);
			supplierPartyId = false;

		} else if (id.equalsIgnoreCase("oa:Id") && shipToPartyId) {
			id = "";
			partyId = new PartyId();
			partyId.setId(content);
			shipToParty.setPartyId(partyId);
			shipToPartyId = false;

		} else if (id.equalsIgnoreCase("oa:Id") && supplierItId) {
			id = "";
			supplierItemId.setId(content);
			supplierItId = false;

		} else if (id.equalsIgnoreCase("oa:Id") && customerDocId) {
			customerDocumentId.setId(content);
			customerDocId = false;
			id = "";
		} else if (supplierManufacturer.equalsIgnoreCase("ow-o:SupplierManufacturer")) {
			manufacturerInfo.setSupplierManufacturer(content);
			supplierManufacturer = "";
		} else if (lineNumber.equalsIgnoreCase("oa:LineNumber")) {
			line.setLineNumber(content);
			lineNumber = "";
		} else if (component.equalsIgnoreCase("oa:Component")) {
			sender.setComponent(content);
			component = "";
		} else if (creationDateTime.equalsIgnoreCase("oa:CreationDateTime")) {
			applicationArea.setCreationDateTime(content);
			creationDateTime = "";
		} else if (itemType.equalsIgnoreCase("oa:ItemType")) {
			orderItem.setItemType(content);
			itemType = "";
		} else if (catalogName.equalsIgnoreCase("ow-o:CatalogName")) {
			catalogInfo.setCatalogName(content);
			catalogName = "";
		} else if (catalogVersion.equalsIgnoreCase("ow-o:CatalogVersion")) {
			catalogInfo.setCatalogVersion(content);
			catalogVersion = "";
		} else if (catalogPublisher.equalsIgnoreCase("ow-o:CatalogPublisher")) {
			catalogInfo.setCatalogPublisher(content);
			catalogPublisher = "";
		} else if (universalManufacturer.equalsIgnoreCase("ow-o:UniversalManufacturer")) {
			manufacturerInfo.setUniversalManufacturer(content);
			universalManufacturer = "";
		} else if (supplierManufacturer.equalsIgnoreCase("ow-o:SupplierManufacturer")) {
			manufacturerInfo.setSupplierManufacturer(content);
			supplierManufacturer = "";
		} else if (customerManufacturer.equalsIgnoreCase("ow-o:CustomerManufacturer")) {
			manufacturerInfo.setCustomerManufacturer(content);
			customerManufacturer = "";
		} else if (requestLineGUID.equalsIgnoreCase("ow-o:RequestLineGUID")) {
			orderItem.setRequestLineGUID(content);
			requestLineGUID = "";
		} else if (universalSize.equalsIgnoreCase("ow-o:UniversalSize")) {
			sizeInfo.setUniversalSize(content);
			universalSize = "";
		} else if (speedRating.equalsIgnoreCase("ow-o:SpeedRating")) {
			itemInfo.setSpeedRating(content);
			speedRating = "";
		} else if (loadRating.equalsIgnoreCase("ow-o:LoadRating")) {
			itemInfo.setLoadRating(content);
			loadRating = "";
		} else if (requestedQuantity.equalsIgnoreCase("ow-o:RequestedQuantity")) {
			quantityInfo.setRequestedQuantity(content);
			requestedQuantity = "";
		} else if (sentAt.equalsIgnoreCase("ow-e:SentAt")) {
			properties.setSentAt(content);
			sentAt = "";
		} else if (topic.equalsIgnoreCase("ow-e:Topic")) {
			properties.setTopic(content);
			topic = "";
		} else if (identity.equalsIgnoreCase("ow-e:Identity")) {
			properties.setIdentity(content);
			identity = "";
		} else if (userName.equalsIgnoreCase("ow-e:Username")) {
			securityInfo.setUserName(content);
			userName = "";
		} else if (password.equalsIgnoreCase("ow-e:Password")) {
			securityInfo.setPassword(content);
			password = "";
		}

	}

	public Envelope getEnvelopeData() {
		return envelopeData;
	}

}
