package com.mayfly.kraken.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.mayfly.kraken.client.model.SiteInfo;

@Component
public class SiteInfoRestClient extends AbstractRestClient {

	private static final Logger log = LoggerFactory.getLogger(SiteInfoRestClient.class);

	@Retryable(value = RuntimeException.class, maxAttempts = 2, backoff = @Backoff(delay = 2000))
	public SiteInfo getSiteInfo(String siteId) {
		log.info("Fetching site info for id :{}", siteId);
		String requestUrl = getBaseUrl() + "/site-info/{siteId}";
		ResponseEntity<SiteInfo> outagesEntity = getResttemplate().getForEntity(requestUrl, SiteInfo.class, siteId);
		checkResponseEntity(outagesEntity);
		log.info("Site info for {} successfully retrieved..", siteId);
		return outagesEntity.getBody();
	}
	
	

}
