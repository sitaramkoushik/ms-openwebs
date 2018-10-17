package com.alliance.ows.service;

import org.springframework.stereotype.Component;


/**
 *
 * @author Krishna Kumar
 * 
 */
@Component
public interface OWSServiceInterface {
	
	public String doOWSInq(String bodyString) throws Exception;

	public String doOWSOrder(String bodyString) throws Exception;

}
