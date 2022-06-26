package com.mayfly.krakenclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mayfly.kraken.client.SiteOutagesRestClient;
import com.mayfly.kraken.client.model.EnhancedOutagesInner;

@ExtendWith({MockitoExtension.class})
@DisplayName("Add Unit test cases SiteOutages Rest Client")
public class SiteOutagesRestClientTests {

	@Mock
	private RestTemplate resttemplate;
	
	@InjectMocks
	private SiteOutagesRestClient siteOutagesRestClient;
	
	@Captor
    private ArgumentCaptor<List<EnhancedOutagesInner>> enhancedOutagesCaptor;
	
	@Test
	public void testPostAllSiteOutagesSuccess() {
		when(resttemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString()))
						.thenReturn(new ResponseEntity<Object>(new String("TestResponse"), HttpStatus.OK));
		
		siteOutagesRestClient.postAllSiteOutages(buildRequestCargo(), "test");
	}
	
	
	@Test
	public void testPostAllSiteOutagesSuccessPayloadCheck() {
		when(resttemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString()))
						.thenReturn(new ResponseEntity<Object>(new String("TestResponse"), HttpStatus.OK));
		
		siteOutagesRestClient.postAllSiteOutages(buildRequestCargo(), "test");
		
		verify(resttemplate).postForEntity(Mockito.anyString(), enhancedOutagesCaptor.capture(), Mockito.any(), Mockito.anyString());
		
		assertEquals(1, enhancedOutagesCaptor.getValue().size());
	}
	
	@Test
	public void testPostAllSiteOutagesError() {
		when(resttemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString()))
						.thenReturn(new ResponseEntity<Object>(new String("TestResponse"), HttpStatus.INTERNAL_SERVER_ERROR));
		
		assertThrows(RuntimeException.class, () -> siteOutagesRestClient.postAllSiteOutages(buildRequestCargo(), "test"));
	}
	
	
	private List<EnhancedOutagesInner> buildRequestCargo(){
		EnhancedOutagesInner enhancedOutagesInner = 
				new EnhancedOutagesInner("testId", LocalDateTime.now(), LocalDateTime.now().plusDays(30), "testName");	
		return new ArrayList<EnhancedOutagesInner>(Collections.singletonList(enhancedOutagesInner));
	}
	
	
}
