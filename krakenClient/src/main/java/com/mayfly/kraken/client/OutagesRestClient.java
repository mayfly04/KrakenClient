package com.mayfly.kraken.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import com.mayfly.kraken.client.model.Outages;

@Component
public class OutagesRestClient extends AbstractRestClient {
	
	private static final Logger log = LoggerFactory.getLogger(OutagesRestClient.class);
	
	@Retryable( value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
	public Outages getOutages() {
		log.info("Starting to fetch all Outages ..");
		String requestUrl = getBaseUrl() + "/outages";
		ResponseEntity<Outages> outagesEntity  = getResttemplate().getForEntity(requestUrl, Outages.class);
		//Can be refactored  to generic call
		checkResponseEntity(outagesEntity);
		log.info("Outage fetch Data call successfully executed...");
		return outagesEntity.getBody();
	}
	
}
