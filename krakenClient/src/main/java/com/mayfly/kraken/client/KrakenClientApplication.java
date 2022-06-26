package com.mayfly.kraken.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

import com.mayfly.kraken.config.RestClientConfig;

@SpringBootApplication
@Import({RestClientConfig.class})
@EnableRetry
public class KrakenClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrakenClientApplication.class, args);
	}

}
