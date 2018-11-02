package com.alliance.ows.controller;

import java.net.URLDecoder;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alliance.EchoController;
import com.alliance.logging.UtilityLogger;
import com.alliance.ows.service.OWSServiceInterface;
import com.alliance.utils.ConstantsUtility;

/**
 *
 * @author Krishna Kumar
 * 
 */
@Controller
@RequestMapping("/openwebs")
@Configuration
public class OWSController extends EchoController {

	@Autowired
	private OWSServiceInterface owsService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/inquiry", method = RequestMethod.POST, produces = { MediaType.TEXT_XML })
	@ResponseBody
	public String doOWSInq(@RequestParam(value = "reqData", required = true) String reqData,
					@RequestParam(value = "userName", required = false) String userName,
					@RequestParam(value = "password", required = false) String password) {
		try {
			return owsService.doOWSInq(reqData);
		} catch (Exception e) {
			UtilityLogger.error(e.getMessage(), e);
			JSONObject respJson = new JSONObject();
			respJson.put(ConstantsUtility.STATUS, ConstantsUtility.FAILED);
			respJson.put(ConstantsUtility.MESSAGE, e.getMessage());
			return respJson.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order", method = RequestMethod.POST, produces = { MediaType.TEXT_XML })
	@ResponseBody
	public String doOWSOrder(@RequestParam(value = "reqData", required = true) String reqData,
					@RequestParam(value = "userName", required = false) String userName,
					@RequestParam(value = "password", required = false) String password) {
		try {
			return owsService.doOWSOrder(reqData);
		} catch (Exception e) {
			UtilityLogger.error(e.getMessage(), e);
			JSONObject respJson = new JSONObject();
			respJson.put(ConstantsUtility.STATUS, ConstantsUtility.FAILED);
			respJson.put(ConstantsUtility.MESSAGE, e.getMessage());
			return respJson.toString();
		}
	}
}
