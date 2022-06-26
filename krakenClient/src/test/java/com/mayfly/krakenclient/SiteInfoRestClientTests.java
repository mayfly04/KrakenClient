package com.mayfly.krakenclient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mayfly.kraken.client.SiteInfoRestClient;
import com.mayfly.kraken.client.model.SiteInfo;
import com.mayfly.krakenclient.utils.TestUtils;

@ExtendWith({MockitoExtension.class})
@DisplayName("Add Unit test cases SiteInfo Rest Client")
public class SiteInfoRestClientTests {

	
	@Mock
	private RestTemplate resttemplate;
	
	@InjectMocks
	private SiteInfoRestClient siteInfoRestClient;
	
	
	@BeforeEach
	public void loadSiteInfoData() throws Exception {
		when(resttemplate.getForEntity(Mockito.anyString(), Mockito.any(),Mockito.anyString()))
		.thenReturn(new ResponseEntity<Object>(TestUtils.getSiteInfoCargo(), HttpStatus.OK));

	}
	
	
	@Test
	public void testSiteInfoRestClientSuccess() {
		SiteInfo siteInfo =  siteInfoRestClient.getSiteInfo("");
		assertNotNull(siteInfo);
		assertNotNull(siteInfo.getName());
		
		assertFalse(siteInfo.getDevices().isEmpty());
		
		siteInfo.getDevices().forEach(dev ->{
			assertNotNull(dev.getId());
			assertNotNull(dev.getName());
		});

	}
	
	

	@Test
	public void testSiteInfoRestClientError() throws Exception {
		when(resttemplate.getForEntity(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
		.thenReturn(new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR));
		
		assertThrows(RuntimeException.class, () -> siteInfoRestClient.getSiteInfo(""));
	}
}
