package com.alliance.ows.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alliance.EchoController;
import com.alliance.logging.UtilityLogger;
import com.alliance.ows.service.OWSServiceInterface;
import com.alliance.utils.ConstantsUtility;

/**
 *
 * @author Krishna Kumar
 * 
 */
@RestController
@RequestMapping("/openwebs/${owsVersion}")
@Configuration
public class OWSController extends EchoController {

	@Autowired
	private OWSServiceInterface owsService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "HTTP POST method endpoint URI for preparing Inquiry Request", nickname = "inquiry", notes = "This endpoint will return `Inquiry Request` for sellNetworkService.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found") })
	@PostMapping(value = "/inquiry", produces = { MediaType.TEXT_XML })
	@ResponseBody
	public String doOWSInq(@RequestBody String reqData) {
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
	@ApiOperation(value = "HTTP POST method endpoint URI for preparing Order equest", nickname = "order", notes = "This endpoint will return `Order Request` for sellNetworkService.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found") })
	@PostMapping(value = "/order", produces = { MediaType.TEXT_XML })
	@ResponseBody
	public String doOWSOrder(@RequestBody String reqData) {
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
