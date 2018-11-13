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

import org.apache.commons.lang.StringEscapeUtils;
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

			bodyString = StringEscapeUtils.unescapeXml(bodyString);

			bodyString = bodyString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
			bodyString = bodyString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			saxParserFactory.setNamespaceAware(true);
			SAXParser parser = saxParserFactory.newSAXParser();
			InputStream in = new ByteArrayInputStream(bodyString.getBytes());

			String prefix = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ProcessMessageResponse xmlns=\"http://www.carpartstechnologies.com/openwebs/\"><ProcessMessageResult>";
			String suffix = "</ProcessMessageResult></ProcessMessageResponse></soap:Body></soap:Envelope>";

			String actualResult = "";
			if(bodyString.contains("ow-o:ProcessPurchaseOrder")){

				UtilityLogger.warn("Order request data: " + bodyString);
				
				OrderHandler handler = new OrderHandler();
				parser.parse(in, handler);
				Envelope envelopeData = handler.getOrderReqEnvelope();

				actualResult = prepareOwsOrdReqData(getOrderPartData(envelopeData), envelopeData);

			} else {

				UtilityLogger.warn("Inquiry request data: " + bodyString);
				
				InquiryHandler handler = new InquiryHandler();
				parser.parse(in, handler);
				Envelope envelopeData = handler.getEnvelopeData();
				
				actualResult = prepareOwsInqReqData(getPartData(envelopeData), envelopeData);
			}
			
			actualResult = actualResult.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

			String finalResult = prefix + StringEscapeUtils.escapeXml(actualResult) + suffix;
			// String finalResult = prefix + actualResult + suffix;

			UtilityLogger.warn("Response data: " + finalResult);
			return finalResult;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
	}

	public String prepareOwsInqReqData(List<InquiryRequestPart> partData, Envelope envelopeData) throws ParserConfigurationException,
					TransformerException {

		InquiryRequestData inqReqData = new InquiryRequestData();
		inqReqData.setParts(partData);
		String token = getTokenByOrgId("DummyOrg");
		inqReqData.setToken(token);
		inqReqData.setLookupType("IIS");
		inqReqData.setLookupInUse("3");
		inqReqData.setScat("99");
		inqReqData.setSindex("0");
		inqReqData.setStype("");
		inqReqData.setService("OpenWebs");
		inqReqData.setStart("0");
		inqReqData.setSearchType("RECHECK_ALL");
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
			if (response.getEntity() != null) {
				long contentLength = response.getEntity().getContentLength();
				if (contentLength == 0) {
					respJson.put(ConstantsUtility.STATUS, ConstantsUtility.SUCCESS);
					respJson.put(ConstantsUtility.MESSAGE, "Parts Not Found");
				} else {
					JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
									&& json.getString(ConstantsUtility.STATUS).equals(ConstantsUtility.SUCCESS)) {

						String result = "";

						switch (reqType) {
						case INQ:
							result = inqRespParseToXml(json.toString(), envelopeData);
							break;

						case ORD:
							result = orderRespParseToXml(json.toString(), envelopeData);
							break;

						default:
							result = inqRespParseToXml(json.toString(), envelopeData);
							break;
						}
						return result;
					} else {
						respJson.put(ConstantsUtility.STATUS, ConstantsUtility.FAILED);
						respJson.put(ConstantsUtility.MESSAGE, json.getString(ConstantsUtility.ERROR));
					}
				}
			} else {
				respJson.put(ConstantsUtility.STATUS, ConstantsUtility.SUCCESS);
				respJson.put(ConstantsUtility.MESSAGE, "Parts Not Found");
			}
		} catch (ParseException | JSONException e) {
			UtilityLogger.error(e.getMessage(), e);
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (ConnectTimeoutException e) {
			UtilityLogger.error(e.getMessage(), e);
			throw new AESException(new Fault(FaultConstants.CONNECTION_TIMEOUT, new Object[] { e.getMessage() }));
		} catch (IOException e) {
			UtilityLogger.error(e.getMessage(), e);
			throw new AESException(new Fault(FaultConstants.HOST_NOT_FOUND, new Object[] { e.getMessage() }));
		} catch (Exception e) {
			UtilityLogger.error(e.getMessage(), e);
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
		return respJson.toString().replaceAll("\n|\r", "").replaceAll("(\\\\r|\\\\n)", "").replaceAll("\\\\/", "/").replaceAll("\\\\", "");
	}

	public String inqRespParseToXml(String respJson, Envelope envelopeData) throws MalformedURLException, ParserConfigurationException,
					TransformerException {

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
			Envelope envelopeData = handler.getOrderReqEnvelope();
			String prefix = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><ProcessMessageResponse xmlns=\"http://www.carpartstechnologies.com/openwebs/\"><ProcessMessageResult>";
			String suffix = "</ProcessMessageResult></ProcessMessageResponse></soap:Body></soap:Envelope>";

			String actualResult = prepareOwsOrdReqData(getOrderPartData(envelopeData), envelopeData);
			actualResult = actualResult.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

			String finalResult = prefix + StringEscapeUtils.escapeXml(actualResult) + suffix;
			// String finalResult = prefix + actualResult + suffix;

			UtilityLogger.warn("Response data: " + finalResult);
			return actualResult;
		} catch (Exception e) {
			UtilityLogger.error(e);
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}

	}

	public String prepareOwsOrdReqData(OrderRequestData ordReqData, Envelope envelopeData) throws ParserConfigurationException, TransformerException {
		String token = "";
		List<OrderRequestPart> ordReqPartList = ordReqData.getParts();
		for (OrderRequestPart ordReqPart : ordReqPartList) {
			ordReqPart.setAdded_to_cart(ordReqPart.getQty());
			Vector<SelectOption> locations;
			if (ordReqPart.getLocations() == null) {
				locations = new Vector<SelectOption>();
				SelectOption selOpt = new SelectOption();
				Quantity quant = new Quantity();
				quant.setRequested(ordReqPart.getQty());
				selOpt.setQuantity(quant);
				if (ordReqPart.getSelLoc() == 0) {
					selOpt.setNetwork(100);
					ordReqPart.setSelLoc(100);
				} else {
					selOpt.setNetwork(ordReqPart.getSelLoc());
				}

				locations.add(selOpt);
			} else {
				locations = ordReqPart.getLocations();
				for (SelectOption selOpt : locations) {
					Quantity quant = new Quantity();
					quant.setRequested(ordReqPart.getQty());
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
		ordReqData.setPoNumber("testPO");
		return orderRequestToSellNetwork(gson.toJson(ordReqData), envelopeData);
	}

	public OrderRequestData getOrderPartData(Envelope envelope) {
		OrderRequestData ordReqData = new OrderRequestData();
		List<OrderRequestPart> obj = new ArrayList<OrderRequestPart>();
		List<com.alliance.ows.model.order.Line> partList = envelope.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getLine();
		for (com.alliance.ows.model.order.Line partData : partList) {
			OrderRequestPart ordPartData = new OrderRequestPart();
			try {
				ordPartData.setLine(Integer.parseInt(partData.getLineNumber()));
			} catch (Exception e) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			try {
				ordPartData.setLineCode(partData.getOrderItem().getItemInfo().getManufacturerInfo().getSupplierManufacturer());
			} catch (Exception e) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			try {
				ordPartData.setPart(partData.getOrderItem().getItemId().getSupplierItemId().getId());
			} catch (Exception e) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			try {
				ordPartData.setSelLoc(Integer.parseInt(partData.getOrderItem().getOrderInfo().getSupplierLocationId()));
			} catch (Exception e) {
				ordPartData.setSelLoc(100);
			}
			try {
				ordPartData.setQty(Integer.parseInt(partData.getOrderQuantity()));
			} catch (Exception e) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}

			obj.add(ordPartData);
		}
		ordReqData.setParts(obj);
		return ordReqData;
	}

	public String orderRequestToSellNetwork(String requestData, Envelope envelopeData) throws ParserConfigurationException, TransformerException {
		return getSellNetworkData(requestData, sellNetworkOrderUrl, ORD, envelopeData);
	}

	public String orderRespParseToXml(String respJson, Envelope envData) throws ParserConfigurationException, TransformerException {

		OrderResponseData obj = gson.fromJson(respJson, OrderResponseData.class);

		return xmlGenerator.getOrderRespXml(obj, envData);
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
		int lineNumber = 0;
		for (Line partData : partList) {
			InquiryRequestPart inqPartData = new InquiryRequestPart();
			try {
				inqPartData.setLine(++lineNumber);
			} catch (Exception e) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			try {
				if (partData.getOrderItem().getItemInfo().getManufacturerInfo().getSupplierManufacturer() != null) {
					inqPartData.setLineCode(partData.getOrderItem().getItemInfo().getManufacturerInfo().getSupplierManufacturer());
				} else {
					inqPartData.setLineCode("");
				}
			} catch (Exception e) {
				inqPartData.setLineCode("");
			}
			try {
				inqPartData.setPart(partData.getOrderItem().getItemId().getSupplierItemId().getId());
			} catch (Exception e) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
			}
			if (partData.getOrderItem().getQuantityInfo() != null && partData.getOrderItem().getQuantityInfo().getRequestedQuantity() != null) {
				try {
					inqPartData.setQty(Integer.parseInt(partData.getOrderItem().getQuantityInfo().getRequestedQuantity()));
				} catch (Exception e) {
					UtilityLogger.error(e.getMessage());
					inqPartData.setQty(1);
				}
			} else {
				inqPartData.setQty(0);
			}
			inqPartData.setDescription("");
			obj.add(inqPartData);
		}
		return obj;
	}
}
