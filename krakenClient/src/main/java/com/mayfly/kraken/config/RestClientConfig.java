package com.mayfly.kraken.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

	@Value("${kraken.rest.header.key}")
	private String securityHeaderKey;

	private final static String KRAKEN_HEADER_SEC_KEY = "x-api-key";
	
	private static final Logger log = LoggerFactory.getLogger(RestClientConfig.class);

	@Bean
	public RestTemplate restTemplate() {
		log.info("Initializing Rest Template");
		return new RestTemplateBuilder()
				.interceptors((HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
					request.getHeaders().set(KRAKEN_HEADER_SEC_KEY, securityHeaderKey);
					return execution.execute(request, body);
				}).build();
	}
}