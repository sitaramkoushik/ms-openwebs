package com.alliance.ows.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.alliance.fault.AESException;
import com.alliance.fault.Fault;
import com.alliance.fault.FaultConstants;
import com.alliance.logging.UtilityLogger;
import com.alliance.myplace.token.TokenService;
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
import com.alliance.sellNetwork.SellNetworkResponse;
import com.alliance.utils.ConstantsUtility;
import com.alliance.utils.MPFPResourceReader;
import com.alliance.utils.RestUtil;
import com.alliance.utils.UserCache;
import com.alliance.utils.Utils;
import com.google.gson.Gson;

import okhttp3.Request;

/**
 *
 * @author Krishna Kumar
 * 
 */
public class OWSServiceImpl implements OWSServiceInterface {

	private static String sellNetworkInqUrl;
	private static String sellNetworkOrderUrl;
	private static Gson gson;
	private static int TIMEOUT_MILLIS;
	private static RequestConfig requestConfig = null;
	private static OwsXmlGenerator xmlGenerator;
	private static String tokenPassword;
	private static String tokenBaseURL;
	private static String x_api_key;
	private static final int INQ = 1;
	private static final int ORD = 2;
	private static String userName;
	private static String password;
	private static final String DEVICEID = "OpenWebs";
	private static Base64.Decoder decoder = Base64.getDecoder();
	private static String salespadUrl;
	static {
		xmlGenerator = new OwsXmlGenerator();
		MPFPResourceReader mpfpReader = MPFPResourceReader.getInstance();
		tokenPassword = mpfpReader.getString("security.token.password");
		
		sellNetworkInqUrl = mpfpReader.getString("sellnetwork.inquire.url");
		sellNetworkOrderUrl = mpfpReader.getString("sellnetwork.order.url");

		salespadUrl = mpfpReader.getString("salespad.url");
		
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
		userName = new String(decoder.decode(mpfpReader.getString("cachetoken.username")));
		password = new String(decoder.decode(mpfpReader.getString("cachetoken.password")));
	}

	@Override
	public String doOWSInq(String bodyString) throws Exception {

		long startTime = System.currentTimeMillis();
		String screenName = "";
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
/*			if (bodyString.contains("ow-o:ProcessPurchaseOrder")) {

				UtilityLogger.warn("Order request data: " + bodyString);

				OrderHandler handler = new OrderHandler();
				parser.parse(in, handler);
				Envelope envelopeData = handler.getOrderReqEnvelope();

				screenName = getScreenName(envelopeData, false);
				OrderRequestData orderRequestData = getOrderPartData(envelopeData, token);
				
				actualResult = prepareOwsOrdReqData(orderRequestData, envelopeData);

			} else {*/

				UtilityLogger.warn("Inquiry request data: " + bodyString);

				InquiryHandler handler = new InquiryHandler();
				parser.parse(in, handler);
				Envelope envelopeData = handler.getEnvelopeData();

				screenName = getScreenName(envelopeData, true);
				List<InquiryRequestPart> inquiryRequestParts = getPartData(envelopeData);
				
				actualResult = prepareOwsInqReqData(inquiryRequestParts, envelopeData);
			

			actualResult = actualResult.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

			String finalResult = prefix + StringEscapeUtils.escapeXml(actualResult) + suffix;

			UtilityLogger.warn("Response returning in " + (System.currentTimeMillis() - startTime) + "ms for " + screenName + ". " + finalResult);
			return finalResult;
		} catch (AESException e) {
			e.printStackTrace();
			UtilityLogger.warn("AESException while getting the Response returning in " + (System.currentTimeMillis() - startTime) + "ms for "
							+ screenName);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			UtilityLogger.warn("Exception while getting the Response returning in " + (System.currentTimeMillis() - startTime) + "ms for "
							+ screenName);
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
	}

	private String getScreenName(Envelope envelopeData, boolean isInquiry) {
		try {

			if (isInquiry) {
				return envelopeData.getBody().getAddReqForQuote().getDataArea().getRequestForQuote().getOaHeader().getParties().getCustomerParty()
								.getPartyId().getId();
			} else {
				return envelopeData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getOwoHeader().getParties()
								.getCustomerParty().getPartyId().getId();
			}

		} catch (Exception e) {
			UtilityLogger.error("Exception occurred in getScreenName: " + e.getMessage());
			throw new AESException(new Fault(FaultConstants.V3_TOKEN_CREATION_FAILED, new Object[] { e.getMessage() }));
		}
	}


	@SuppressWarnings("rawtypes")
	public String prepareOwsInqReqData(List<InquiryRequestPart> partData, Envelope envelopeData) throws ParserConfigurationException,
					TransformerException {

		InquiryRequestData inqReqData = new InquiryRequestData();
		inqReqData.setParts(partData);

		String token = new JSONObject(TokenService.getTokenFromTokenCache(userName, password)).getJSONObject(ConstantsUtility.DATA).getString(ConstantsUtility.IDTOKEN);
		org.json.simple.JSONObject userData = TokenService.getUserIdAndOrgIdByScreenName(getScreenName(envelopeData, true), token);

		String sellnetworkUserId = "";

		if (userData != null && userData.size() > 0 && userData.containsKey(ConstantsUtility.DATA)) {
			
			HashMap data = (HashMap) userData.get(ConstantsUtility.DATA);
			Integer userId = (Integer) data.get("userId");
			Integer orgId = (Integer) data.get("orgId");
			sellnetworkUserId = userId + "_" + DEVICEID + "_" + orgId;
			createSalepadRepUserInfo(orgId, userId, DEVICEID, token);
		}
		inqReqData.setToken(token);
		inqReqData.setLookupType("IIS");
		inqReqData.setLookupInUse("3");
		inqReqData.setScat("99");
		inqReqData.setSindex("0");
		inqReqData.setStype("");
		inqReqData.setService("OpenWebs");
		inqReqData.setStart("0");
		inqReqData.setSearchType("RECHECK_ALL");
		return inqRequestToSellNetwork(gson.toJson(inqReqData), envelopeData, token, sellnetworkUserId);
	}

	private HttpResponse getHttpResponse(String requestData, String url, String token, String userId) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			List<NameValuePair> postParameters = new ArrayList<>();
			postParameters.add(new BasicNameValuePair("userId", userId));

			uriBuilder.addParameters(postParameters);
			HttpPost httpPost = new HttpPost(uriBuilder.build());
			HttpClient httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(requestData);
			httpPost.addHeader(ConstantsUtility.AUTHORIZATION, token);
			httpPost.setEntity(stringEntity);
			httpPost.getRequestLine();
			httpPost.setConfig(requestConfig);
			response = httpClient.execute(httpPost);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}


	public String inqRequestToSellNetwork(String requestData, Envelope envelopeData, String token, String userId) {
		return getSellNetworkData(requestData, sellNetworkInqUrl, INQ, envelopeData, token, userId);
	}

	private String getSellNetworkData(String requestData, String sellNetworkURL, int reqType, Envelope envelopeData, String token, String userId) {
		JSONObject respJson = new JSONObject();
		try {
			long startTime = System.currentTimeMillis();
			HttpResponse response = getHttpResponse(requestData, sellNetworkURL, token, userId);
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
						UtilityLogger.warn("SellNetwork response received in " + (System.currentTimeMillis() - startTime) + "ms");

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

	private String inqRespParseToXml(String respJson, Envelope envelopeData) throws MalformedURLException, ParserConfigurationException,
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

			String token = "";
			String sellnetworkUserId = "";
			try {
				token = new JSONObject(TokenService.getTokenFromTokenCache(userName, password)).getJSONObject(ConstantsUtility.DATA).getString(ConstantsUtility.IDTOKEN);
				org.json.simple.JSONObject userData = TokenService.getUserIdAndOrgIdByScreenName(getScreenName(envelopeData, false), token);

				if (userData != null && userData.size() > 0 && userData.containsKey(ConstantsUtility.DATA)) {
					HashMap data = (HashMap) userData.get(ConstantsUtility.DATA);
					Integer userId = (Integer) data.get("userId");
					Integer orgId = (Integer) data.get("orgId");
					sellnetworkUserId = userId + "_" + DEVICEID + "_" + orgId;
					createSalepadRepUserInfo(orgId, userId, DEVICEID, token);
				}
			} catch (JSONException e1) {
				throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e1.getMessage() }));
			}
			OrderRequestData orderRequestData = getOrderPartData(envelopeData, token, sellnetworkUserId);
			
			String actualResult = prepareOwsOrdReqData(orderRequestData, envelopeData, token, sellnetworkUserId);			
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


	@SuppressWarnings("rawtypes")
	public String prepareOwsOrdReqData(OrderRequestData ordReqData, Envelope envelopeData, String token, String sellnetworkUserId) throws ParserConfigurationException, TransformerException {
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

		ordReqData.setToken(token);
		ordReqData.setComment("test");
		String poNumber = "";
		try {
			poNumber = envelopeData.getOrdBody().getProcessPurchaseOrder().getDataArea().getPurchaseOrder().getOwoHeader().getDocuments()
							.getCustomerDocumentId().getId();
		} catch (Exception e) {
			poNumber = "";
		}
		ordReqData.setPoNumber(poNumber);
		ordReqData.setService("OpenWebs");
		ordReqData.setScat("99");
		return orderRequestToSellNetwork(gson.toJson(ordReqData), envelopeData, token, sellnetworkUserId);
	}

	private OrderRequestData getOrderPartData(Envelope envelope, String token, String userId) {
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
			ordPartData.setSelLoc(100);
			try {
				
				JSONObject sellNetResponse = getSellNetworkInfo(token, userId);
				String networkResp = partData.getOrderItem().getOrderInfo().getSupplierLocationId();
				if (sellNetResponse != null) {

					Iterator<String> locations = sellNetResponse.keys();
					while (locations.hasNext()) {
						String seqNo = locations.next();
						String locData = (String) sellNetResponse.get(seqNo);
						SellNetworkResponse sellNetwork = new Gson().fromJson(locData, SellNetworkResponse.class);
						if (sellNetwork.getCalledDflt().equalsIgnoreCase(networkResp)) {
							ordPartData.setSelLoc(Integer.parseInt(seqNo));
							break;
						}
					}
				
				}
				UtilityLogger.warn("LocationId: "+ordPartData.getSelLoc()+" for location: "+partData.getOrderItem().getOrderInfo().getSupplierLocationId());
			} catch (Exception e) {
				UtilityLogger.error("Exception occurred while setting setSelLoc: " + e.getMessage());
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


	public String orderRequestToSellNetwork(String requestData, Envelope envelopeData, String token, String userId) throws ParserConfigurationException, TransformerException {
		return getSellNetworkData(requestData, sellNetworkOrderUrl, ORD, envelopeData, token, userId);
	}

	private String orderRespParseToXml(String respJson, Envelope envData) throws ParserConfigurationException, TransformerException {

		OrderResponseData obj = gson.fromJson(respJson, OrderResponseData.class);

		return xmlGenerator.getOrderRespXml(obj, envData);
	}

	/**
	 * This method will get the V3 token by screenName
	 * @param screenName
	 * @return token
	 * @throws JSONException
	 */
	private String getTokenByScreenName(String screenName) throws JSONException {
		String json = null;
		JSONObject loginData = new JSONObject();
		long startTime = System.currentTimeMillis();
		try {
			loginData.put("username", screenName);
			loginData.put("password", tokenPassword);
			loginData.put("useSecretKey", true);
		} catch (JSONException e1) {
			throw new AESException(new Fault(FaultConstants.V3_TOKEN_CREATION_FAILED, new Object[] { e1.getMessage() }));
		}

		HttpPost httpPost = new HttpPost(URI.create(tokenBaseURL + "/userservice/active"));
		httpPost.addHeader(ConstantsUtility.API_KEY, x_api_key);
		String token = "";
		try {
			HttpClient httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(loginData.toString());
			httpPost.getRequestLine();
			httpPost.setEntity(stringEntity);
			HttpResponse response = httpClient.execute(httpPost);
			json = EntityUtils.toString(response.getEntity());
			token = new JSONObject(json).getString(ConstantsUtility.TOKEN);
			UtilityLogger.warn("Got the token in " + (System.currentTimeMillis() - startTime) + "ms for " + screenName);

		} catch (ParseException e) {
			UtilityLogger.error("Could not get the token (ParseException) in: " + (System.currentTimeMillis() - startTime) + "ms, " + e.getCause());
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (IOException e) {
			UtilityLogger.error("Could not get the token (IOException) in: " + (System.currentTimeMillis() - startTime) + "ms, " + e.getCause());
			throw new AESException(new Fault(FaultConstants.OWS_GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (Exception e) {
			UtilityLogger.error("Could not get the token (Exception) in: " + (System.currentTimeMillis() - startTime) + "ms, " + e.getCause());
			throw new AESException(new Fault(FaultConstants.V3_TOKEN_CREATION_FAILED, new Object[] { e.getMessage() }));
		}

		return token;
	}

	private List<InquiryRequestPart> getPartData(Envelope envelope) {
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
	
	/**
	 * Get sellNetwork information using user token 
	 * @param userToken
	 * @return sellNetwork JSON Array
	 */
	private JSONObject getSellNetworkInfo(String userToken, String userId) {
		JSONObject sellnetwork = null;
		try {
			setHeaderToken(userToken);
			com.alliance.utils.UserInfo info = Utils.getUserProfile(userToken, userId);
			String orgId = String.valueOf(info.getOrgId());
			sellnetwork = UserCache.getSellNetworkInfo(orgId);
		} catch (Exception e) {
			UtilityLogger.error("Exception occurred while getting userInfo from token: " + e.getMessage());
		}
		return sellnetwork;
	}

	@SuppressWarnings("static-access")
	private String getTokenServiceUserInfo(String token) {
		UriComponents uri = UriComponentsBuilder.newInstance().fromUriString(tokenBaseURL).path("/tokenservice/get").queryParam("token", token)
						.build();
		Request request = null;
		if (token != null) {
			request = new Request.Builder().url(uri.toUriString()).addHeader(ConstantsUtility.API_KEY, x_api_key).build();
		}
		String userData = "";
		try {
			userData = RestUtil.client.newCall(request).execute().body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userData;
	}
	
	public static String createSalepadRepUserInfo(Integer orgId, Integer userId, String deviceId, String token) throws JSONException {
		String json = null;
		JSONObject loginData = new JSONObject();
		try {
			loginData.put(ConstantsUtility.ORGID, orgId);
			loginData.put(ConstantsUtility.USERID, userId);
			loginData.put("deviceId", deviceId);
		} catch (JSONException e1) {
			throw new AESException(new Fault(FaultConstants.GENERIC_ERROR, new Object[] { e1.getMessage() }));
		}
		
		HttpPost httpPost = new HttpPost(URI.create(salespadUrl));
		try {
			HttpClient httpClient = HttpClients.createDefault();
			StringEntity stringEntity = new StringEntity(loginData.toString());
			httpPost.getRequestLine();
			httpPost.addHeader(ConstantsUtility.AUTHORIZATION, token);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			
			HttpResponse response = httpClient.execute(httpPost);
			json = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			UtilityLogger.error(e.getCause());
			throw new AESException(new Fault(FaultConstants.GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (IOException e) {
			UtilityLogger.error(e.getCause());
			throw new AESException(new Fault(FaultConstants.GENERIC_ERROR, new Object[] { e.getMessage() }));
		} catch (Exception e) {
			UtilityLogger.error(e.getCause());
			throw new AESException(new Fault(FaultConstants.GENERIC_ERROR, new Object[] { e.getMessage() }));
		}
		return json;
	}
	
	private static void setHeaderToken(String token) {
		HttpServletRequest httpRequest = null;
		try {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
			Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
			httpRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
			httpRequest.setAttribute(ConstantsUtility.AUTHORIZATION, token);
			Assert.state(httpRequest != null, "Could not find current HttpServletRequest");
		} catch (Exception e) {
			UtilityLogger.error("Error while setting the Token from Header");
		}
	}
}
