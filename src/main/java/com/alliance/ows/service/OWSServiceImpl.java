package com.alliance.ows.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.alliance.fault.AESException;
import com.alliance.fault.Fault;
import com.alliance.fault.FaultConstants;
import com.alliance.logging.UtilityLogger;
import com.alliance.ows.handler.InquiryHandler;
import com.alliance.ows.handler.OrderHandler;
import com.alliance.ows.handler.OwsXmlGenerator;
import com.alliance.ows.model.Quantity;
import com.alliance.ows.model.inquire.Envelope;
import com.alliance.ows.model.inquire.InquiryRequestData;
import com.alliance.ows.model.inquire.InquiryRequestPart;
import com.alliance.ows.model.inquire.InquiryResponseData;
import com.alliance.ows.model.inquire.Line;
import com.alliance.ows.model.order.OrderRequestData;
import com.alliance.ows.model.order.OrderRequestPart;
import com.alliance.ows.model.order.OrderResponseData;
import com.alliance.ows.model.order.SelectOption;
import com.alliance.utils.ConstantsUtility;
import com.alliance.utils.MPFPResourceReader;
import com.google.gson.Gson;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OWSServiceImpl implements OWSServiceInterface {

	private static MPFPResourceReader mpfpReader;
	private static String sellNetworkInqUrl;
	private static String sellNetworkOrderUrl;
	private static Gson gson;
	private static int TIMEOUT_MILLIS;
	private static RequestConfig requestConfig = null;
	private static OwsXmlGenerator xmlGenerator;
	private static String tokenBaseURL;
	private static String x_api_key;
	private static final int INQ = 1;
	private static final int ORD = 2;

	static {
		xmlGenerator = new OwsXmlGenerator();
		mpfpReader = MPFPResourceReader.getInstance();
		sellNetworkInqUrl = mpfpReader.getString("sellnetwork.inquire.url");
		sellNetworkOrderUrl = mpfpReader.getString("sellnetwork.order.url");

		tokenBaseURL = mpfpReader.getString("tokenservice.store.userinfo.baseurl");
		x_api_key = mpfpReader.getString("tokenservice.store.key.value");
		gson = new Gson();
		try {
			TIMEOUT_MILLIS = Integer.parseInt(mpfpReader.getString("ows.api.request.connection.timeout").trim());
		} catch (Exception e) {
			UtilityLogger.genericError("No property available with ows.api.request.connection.timeout");
			TIMEOUT_MILLIS = 10000;
		}

		requestConfig = RequestConfig.custom().setConnectionRequestTimeout(TIMEOUT_MILLIS).setConnectTimeout(TIMEOUT_MILLIS).build();
	}

	@Override
	public String doOWSInq(String bodyString) throws Exception {
		try {
			UtilityLogger.warn(bodyString);
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setNamespaceAware(true);
			SAXParser parser = saxParserFactory.newSAXParser();
			InquiryHandler handler = new InquiryHandler();
			InputStream in = new ByteArrayInputStream(bodyString.getBytes());
			parser.parse(in, handler);
			Envelope envelopeData = handler.getEnvelopeData();
			return prepareOwsInqReqData(getPartData(envelopeData), envelopeData);
		} catch (Exception e) {
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
	}

	public String prepareOwsInqReqData(List<InquiryRequestPart> partData, Envelope envelopeData) throws ParserConfigurationException, TransformerException {
		InquiryRequestData inqReqData = new InquiryRequestData();
		inqReqData.setPart(partData);
		String token = getTokenByOrgId("DummyOrg");
		inqReqData.settoken(token);
		inqReqData.setlookupType("IIS");
		inqReqData.setsearchType("RECHECK_ALL");
		return inqRequestToSellNetwork(gson.toJson(inqReqData), envelopeData);
	}

	private HttpResponse getHttpResponse(String requestData, String url) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(URI.create(url));
		HttpClient httpClient = HttpClients.createDefault();
		StringEntity stringEntity = new StringEntity(requestData);
		httpPost.setEntity(stringEntity);
		httpPost.getRequestLine();
		httpPost.setConfig(requestConfig);
		HttpResponse response = httpClient.execute(httpPost);

		return response;
	}

	public String inqRequestToSellNetwork(String requestData, Envelope envelopeData) {
		return getSellNetworkData(requestData, sellNetworkInqUrl, INQ, envelopeData);
	}

	private String getSellNetworkData(String requestData, String sellNetworkURL, int reqType, Envelope envelopeData) {
		JSONObject respJson = new JSONObject();
		try {
			HttpResponse response = getHttpResponse(requestData, sellNetworkURL);
			JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
							&& json.getString(ConstantsUtility.STATUS).equals(ConstantsUtility.SUCCESS)) {

				String result = "";

				switch (reqType) {
				case INQ:
					result = inqRespParseToXml(json.toString(), envelopeData);
					break;

				case ORD:
					result = orderRespParseToXml(json.toString());
					break;

				default:
					result = inqRespParseToXml(json.toString(), envelopeData);
					break;
				}
				respJson.put(ConstantsUtility.STATUS, ConstantsUtility.SUCCESS);
				respJson.put(ConstantsUtility.MESSAGE, result);
			} else {
				respJson.put(ConstantsUtility.STATUS, ConstantsUtility.FAILED);
				respJson.put(ConstantsUtility.MESSAGE, json.getString(ConstantsUtility.ERROR));
			}
		} catch (ParseException | JSONException e) {
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (ConnectTimeoutException e) {
			throw new AESException(new Fault(FaultConstants.CONNECTION_TIMEOUT, new Object[] { e.getMessage() }));
		} catch (IOException e) {
			throw new AESException(new Fault(FaultConstants.HOST_NOT_FOUND, new Object[] { e.getMessage() }));
		} catch (Exception e) {
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
		return respJson.toString().replaceAll("\n|\r", "").replaceAll("(\\\\r|\\\\n)", "").replaceAll("\\\\/", "/").replaceAll("\\\\", "");
	}

	public String inqRespParseToXml(String respJson, Envelope envelopeData) throws MalformedURLException, ParserConfigurationException, TransformerException {

		InquiryResponseData obj = gson.fromJson(respJson, InquiryResponseData.class);

		return xmlGenerator.getInquireRespXml(obj, envelopeData);
	}

	@Override
	public String doOWSOrder(String bodyString) throws Exception {
		try {
			UtilityLogger.warn(bodyString);
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setNamespaceAware(true);
			SAXParser parser = saxParserFactory.newSAXParser();
			OrderHandler handler = new OrderHandler();
			InputStream in = new ByteArrayInputStream(bodyString.getBytes());
			parser.parse(in, handler);
			OrderRequestData getPartData = handler.getOrderReqData();
			Envelope envelopeData = null;
			return prepareOwsOrdReqData(getPartData, envelopeData);
		} catch (Exception e) {
			UtilityLogger.error(e);
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}

	}

	public String prepareOwsOrdReqData(OrderRequestData ordReqData, Envelope envelopeData) throws ParserConfigurationException, TransformerException {
		String token = "";
		List<OrderRequestPart> ordReqPartList = ordReqData.getParts();
		for (OrderRequestPart ordReqPart : ordReqPartList) {
			ordReqPart.setAdded_to_cart(ordReqPart.getOrderedQuantity());
			Vector<SelectOption> locations;
			if (ordReqPart.getLocations() == null) {
				locations = new Vector<SelectOption>();
				SelectOption selOpt = new SelectOption();
				Quantity quant = new Quantity();
				quant.setRequested(ordReqPart.getOrderedQuantity());
				selOpt.setQuantity(quant);
				selOpt.setNetwork(ordReqPart.getSelLoc());
				locations.add(selOpt);
			} else {
				locations = ordReqPart.getLocations();
				for (SelectOption selOpt : locations) {
					Quantity quant = new Quantity();
					quant.setRequested(ordReqPart.getOrderedQuantity());
					selOpt.setQuantity(quant);
					selOpt.setNetwork(ordReqPart.getSelLoc());
					locations.add(selOpt);
				}
			}
			ordReqPart.setLocations(locations);
		}
		try {
			token = getTokenByOrgId("DummyOrg");
		} catch (JSONException e1) {
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e1.getMessage() }));
		}
		ordReqData.setToken(token);
		ordReqData.setTestOrder(true);
		ordReqData.setComment("test");
		return orderRequestToSellNetwork(gson.toJson(ordReqData), envelopeData);
	}

	public String orderRequestToSellNetwork(String requestData, Envelope envelopeData) throws ParserConfigurationException, TransformerException {
		return getSellNetworkData(requestData, sellNetworkOrderUrl, ORD, envelopeData);
	}

	public String orderRespParseToXml(String respJson) throws ParserConfigurationException, TransformerException {

		OrderResponseData obj = gson.fromJson(respJson, OrderResponseData.class);

		return xmlGenerator.getOrderRespXml(obj);
	}

	/**
	 * This method should be updated to get the token as per the OrgId and remove the hard-coded data.
	 * @param orgId
	 * @return token
	 * @throws JSONException
	 */
	private String getTokenByOrgId(String orgId) throws JSONException {
		String json = null;
		JSONObject loginData = new JSONObject();
		try {
			loginData.put("username", "aa999jsmith");
			loginData.put("password", "123456");
		} catch (JSONException e1) {
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e1.getMessage() }));
		}

		HttpPost httpPost = new HttpPost(URI.create(tokenBaseURL + "/userservice/active"));
		httpPost.addHeader(ConstantsUtility.API_KEY, x_api_key);
		try {
			HttpClient httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(loginData.toString());
			httpPost.getRequestLine();
			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			json = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			UtilityLogger.error(e.getCause());
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (IOException e) {
			UtilityLogger.error(e.getCause());
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
		return new JSONObject(json).getString(ConstantsUtility.TOKEN);
	}

	public List<InquiryRequestPart> getPartData(Envelope envelope) {
		List<InquiryRequestPart> obj = new ArrayList<InquiryRequestPart>();
		List<Line> partList = envelope.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getLine();
		for (Line partData : partList) {
			InquiryRequestPart inqPartData = new InquiryRequestPart();
			try{
				inqPartData.setLine(Integer.parseInt(partData.getLineNumber()));
			}catch(Exception e){
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			try{
				inqPartData.setLineCode(partData.getOrderItem().getItemInfo().getManufacturerInfo().getSupplierManufacturer());
			}catch(Exception e){
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			try{
				inqPartData.setPart(partData.getOrderItem().getItemId().getSupplierItemId().getId());
			}catch(Exception e){
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			if (partData.getOrderItem().getQuantityInfo() != null && partData.getOrderItem().getQuantityInfo().getRequestedQuantity() != null) {
				inqPartData.setQty(Integer.parseInt(partData.getOrderItem().getQuantityInfo().getRequestedQuantity()));
			} else {
				inqPartData.setQty(0);
			}

			obj.add(inqPartData);
		}
		return obj;
	}
}
