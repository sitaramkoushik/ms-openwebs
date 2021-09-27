package com.alliance.ows.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.alliance.ApplicationService;

/**
 *
 * @author ManikantaReddy
 * 
 */

@PropertySource("classpath:application-${server.env}.properties")
@SpringBootApplication
public class OWSApplication extends ApplicationService {

	public static void main(String[] args) {
		SpringApplication.run(OWSApplication.class, args);
	}
}