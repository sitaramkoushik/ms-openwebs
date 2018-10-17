package com.alliance.ows.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alliance.EchoController;
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
	@RequestMapping(value = "/inquiry", method = RequestMethod.POST)
	@ResponseBody
	public String doOWSInq(@RequestBody String bodyString) {
		try {
			return owsService.doOWSInq(bodyString);
		} catch (Exception e) {
			JSONObject respJson = new JSONObject();
			respJson.put(ConstantsUtility.STATUS, ConstantsUtility.FAILED);
			respJson.put(ConstantsUtility.MESSAGE, e.getMessage());
			return respJson.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	@ResponseBody
	public String doOWSOrder(@RequestBody String bodyString) {
		try {
			return owsService.doOWSOrder(bodyString);
		} catch (Exception e) {
			JSONObject respJson = new JSONObject();
			respJson.put(ConstantsUtility.STATUS, ConstantsUtility.FAILED);
			respJson.put(ConstantsUtility.MESSAGE, e.getMessage());
			return respJson.toString();
		}
	}
}
