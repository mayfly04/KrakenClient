package com.mayfly.kraken.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.mayfly.kraken.client.model.EnhancedOutagesInner;

@Component
public class SiteOutagesRestClient extends AbstractRestClient {

	private static final Logger log = LoggerFactory.getLogger(SiteOutagesRestClient.class);

	@Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
	public void postAllSiteOutages(List<EnhancedOutagesInner> siteOutagesCargo, String siteId) {
		log.info("Starting to send all processed site outage data.");
		String requestUrl = getBaseUrl() + "/site-outages/{siteId}";

		ResponseEntity<Object> siteOutageResponse = getResttemplate().postForEntity(requestUrl, siteOutagesCargo,
				Object.class, siteId);
		
		checkResponseEntity(siteOutageResponse);

		if (HttpStatus.OK.equals(siteOutageResponse.getStatusCode()))
			log.info("Site outage data for id {} successfully sent.", siteId);

	}

}
