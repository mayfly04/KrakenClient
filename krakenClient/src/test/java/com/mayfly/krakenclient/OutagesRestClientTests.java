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

import com.mayfly.kraken.client.OutagesRestClient;
import com.mayfly.kraken.client.model.Outages;
import com.mayfly.krakenclient.utils.TestUtils;

@ExtendWith({MockitoExtension.class})
@DisplayName("Add Unit test cases Outages Rest Client")
public class OutagesRestClientTests {

	@Mock
	private RestTemplate resttemplate;
	
	@InjectMocks
	private OutagesRestClient outagesRestClient;

	@BeforeEach
	public void loadData() throws Exception {
		when(resttemplate.getForEntity(Mockito.anyString(), Mockito.any()))
		.thenReturn(new ResponseEntity<Object>(TestUtils.getOutagesCargo(), HttpStatus.OK));

	}
	
	@Test
	public void testOutageRestClientSuccess() {
		Outages outages =  outagesRestClient.getOutages();
		assertNotNull(outages);
		assertFalse(outages.isEmpty());
		
		outages.forEach(outage -> {
			assertNotNull(outage.getId());
			assertNotNull(outage.getBegin());
			assertNotNull(outage.getEnd());
		});
	}
	
	@Test
	public void testOutageRestClientError() throws Exception {
		when(resttemplate.getForEntity(Mockito.anyString(), Mockito.any()))
		.thenReturn(new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR));
		
		assertThrows(RuntimeException.class, () -> outagesRestClient.getOutages());
	}
	
}
