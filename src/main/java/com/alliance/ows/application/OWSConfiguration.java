package com.alliance.ows.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alliance.ows.service.OWSServiceImpl;
import com.alliance.ows.service.OWSServiceInterface;

/**
 * 
 * @author Gangeswar Vemula
 * 
 */
@Configuration
public class OWSConfiguration {

	@Bean
	public OWSServiceInterface owsServiceInterface() {
		return new OWSServiceImpl();
	}
}
