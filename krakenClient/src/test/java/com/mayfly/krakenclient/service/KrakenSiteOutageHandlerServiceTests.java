package com.mayfly.krakenclient.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.mayfly.kraken.client.OutagesRestClient;
import com.mayfly.kraken.client.SiteInfoRestClient;
import com.mayfly.kraken.client.SiteOutagesRestClient;
import com.mayfly.kraken.client.model.EnhancedOutagesInner;
import com.mayfly.kraken.client.model.Outages;
import com.mayfly.kraken.client.service.KrakenSiteOutageHandlerService;
import com.mayfly.krakenclient.utils.TestUtils;

@ExtendWith({MockitoExtension.class})
@DisplayName("Add Unit test cases KrakenSiteOutageHandler Service")
public class KrakenSiteOutageHandlerServiceTests {

	
	@Captor
    private ArgumentCaptor<List<EnhancedOutagesInner>> enhancedOutagesCaptor;
	
	@Captor
    private ArgumentCaptor<String> siteIdCaptor;
	
	@Mock
	private OutagesRestClient outagesRestClient ;
	
	@Mock
	private SiteInfoRestClient siteInfoRestClient;
	
	@Mock
	private SiteOutagesRestClient siteOutagesRestClient;
	
	@InjectMocks
	private KrakenSiteOutageHandlerService krakenSiteOutageHandlerService;
	
	private static String siteId = "testId";
	
	
	@Test
	public void testProcessSiteOutagesSuccess() throws Exception {
		when(outagesRestClient.getOutages()).thenReturn(TestUtils.getOutagesCargo());
		when(siteInfoRestClient.getSiteInfo(siteId)).thenReturn(TestUtils.getSiteInfoCargo());
		doNothing().when(siteOutagesRestClient).postAllSiteOutages(Mockito.anyList(), Mockito.anyString());
		
		krakenSiteOutageHandlerService.processSiteOutages(siteId);
		verify(siteOutagesRestClient).postAllSiteOutages(enhancedOutagesCaptor.capture(), siteIdCaptor.capture());
		
		assertEquals(siteId, siteIdCaptor.getValue());
		List<EnhancedOutagesInner> requestCargo = enhancedOutagesCaptor.getValue();
		assertFalse(requestCargo.isEmpty());
		
		assertEquals(3, requestCargo.size());
		
		requestCargo.forEach(cargo -> {
			assertNotNull(cargo.getId());
			assertNotNull(cargo.getEnd());
			assertNotNull(cargo.getName());
			assertNotNull(cargo.getBegin());
			assertTrue(cargo.getBegin().isAfter(KrakenSiteOutageHandlerService.OFFSET_DATE_TIME));		
		});
	}
	
	@Test
	public void testProcessSiteOutageEmptyOutageDataError() {
		when(outagesRestClient.getOutages()).thenReturn(new Outages());
		
		assertThrows(RuntimeException.class, () -> krakenSiteOutageHandlerService.processSiteOutages(siteId));
	}
	
	@Test
	public void testProcessSiteOutageEmptySiteInfoError() throws Exception {
		when(outagesRestClient.getOutages()).thenReturn(TestUtils.getOutagesCargo());
		when(siteInfoRestClient.getSiteInfo(siteId)).thenReturn(null);
		
		assertThrows(RuntimeException.class, () -> krakenSiteOutageHandlerService.processSiteOutages(siteId));
	}
	
	
	@Test
	public void testProcessSiteOutageEmptySiteOutageCargoError() throws Exception {
		when(outagesRestClient.getOutages()).thenReturn(TestUtils.getOutagesCargo());
		when(siteInfoRestClient.getSiteInfo(siteId)).thenReturn(TestUtils.getNonMatchSiteInfoCargo());
		
		assertThrows(RuntimeException.class, () -> krakenSiteOutageHandlerService.processSiteOutages(siteId));
	}
	
}
