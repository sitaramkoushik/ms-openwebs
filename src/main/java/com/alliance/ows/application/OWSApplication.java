package com.alliance.ows.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alliance.ApplicationService;

/**
 *
 * @author ManikantaReddy
 * 
 */

@SpringBootApplication
public class OWSApplication extends ApplicationService {

	public static void main(String[] args) {
		SpringApplication.run(OWSApplication.class, args);
	}
}