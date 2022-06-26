package com.mayfly.kraken.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractRestClient {

	@Autowired
	private RestTemplate resttemplate;

	@Value("${kraken.rest.baseUrl}")
	private String baseKrakenURI;

	protected RestTemplate getResttemplate() {
		return resttemplate;
	}

	protected String getBaseUrl() {
		return baseKrakenURI;
	}

	protected void checkResponseEntity(ResponseEntity<?> response) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(response.getStatusCode()))
			throw new RuntimeException("Rest API call returned 500 error");
	}

}
