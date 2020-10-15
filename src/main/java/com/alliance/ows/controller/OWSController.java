package com.alliance.ows.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/openwebs/${owsVersion:}")
@Api(tags = "openWebs", description = "openWebs API to perform operation related to openWebs inquiry and order.")

@Configuration
public class OWSController extends EchoController {

	@Autowired
	private OWSServiceInterface owsService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "OpenWebs Request For Inquiry", nickname = "inquiry",
					notes = "This API accepts the OpenWebs request and returns the INQ response.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found") })
	@PostMapping(value = "/inquiry", produces = { MediaType.TEXT_XML })
	@ResponseBody
	public String doOWSInq(
					@ApiParam(value = "The string as `reqData` is used as payload to get inquiry request.",
									required = true) @RequestBody String reqData) {
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
	@ApiOperation(value = "OpenWebs Request For Order", nickname = "order",
					notes = "This API accepts the OpenWebs request and returns the Order response.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found") })
	@PostMapping(value = "/order", produces = { MediaType.TEXT_XML })
	@ResponseBody
	public String doOWSOrder(
					@ApiParam(value = "The string as `reqData` is used as payload to get order request.") @RequestBody String reqData) {
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
