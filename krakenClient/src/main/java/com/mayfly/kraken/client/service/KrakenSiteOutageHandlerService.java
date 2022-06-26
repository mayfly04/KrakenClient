package com.mayfly.kraken.client.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mayfly.kraken.client.OutagesRestClient;
import com.mayfly.kraken.client.SiteInfoRestClient;
import com.mayfly.kraken.client.SiteOutagesRestClient;
import com.mayfly.kraken.client.model.EnhancedOutagesInner;
import com.mayfly.kraken.client.model.Outages;
import com.mayfly.kraken.client.model.OutagesInner;
import com.mayfly.kraken.client.model.SiteInfo;

@Service
public class KrakenSiteOutageHandlerService {

	public final static LocalDateTime OFFSET_DATE_TIME = LocalDateTime.parse("2022-01-01T00:00:00.000Z",
			DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH));

	private static final Logger log = LoggerFactory.getLogger(KrakenSiteOutageHandlerService.class);

	@Autowired
	private OutagesRestClient outagesRestClient;

	@Autowired
	private SiteInfoRestClient siteInfoRestClient;

	@Autowired
	private SiteOutagesRestClient siteOutagesRestClient;

	@PostConstruct
	private void kickStartSiteOutageProcess() {
		processSiteOutages("norwich-pear-tree");
	}

	public void processSiteOutages(String siteId) {
		log.info("Kick Starting the site outage reporting process...");
		List<OutagesInner> outageList = getValidOutages(outagesRestClient.getOutages());
		SiteInfo siteInfo = getSiteInfoById(siteId);
		List<EnhancedOutagesInner> enhancedOutages = new ArrayList<>();

		siteInfo.getDevices().forEach(site -> {
			List<OutagesInner> outages = getOutagesById(outageList, site.getId());
			if (!CollectionUtils.isEmpty(outages)) {
				enhancedOutages.addAll(buildOutagesCargo(outages, site.getName()));
			}
			// Better performance side of it
			outageList.removeAll(outages);
		});

		if (CollectionUtils.isEmpty(enhancedOutages))
			throw new RuntimeException("Could not complete the reporting outage process, no matching data found");

		siteOutagesRestClient.postAllSiteOutages(enhancedOutages, siteId);
		log.info("Site outage reporting for id: {} successfully completed...", siteId);
	}

	private SiteInfo getSiteInfoById(String siteId) {
		SiteInfo siteInfo = siteInfoRestClient.getSiteInfo(siteId);
		if (CollectionUtils.isEmpty(siteInfo.getDevices())) {
			throw new RuntimeException("Invalid or empty site info retrieved, cannot continue the process");
		}
		return siteInfo;
	}

	private List<OutagesInner> getValidOutages(Outages outages) {
		List<OutagesInner> outageList = outages.stream().filter(outage -> Objects.nonNull(outage))
				.filter(Predicate.not(outage -> outage.getBegin().isBefore(OFFSET_DATE_TIME)))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(outageList)) {
			throw new RuntimeException("Empty Outage list retrieved after the filtering, cannot continue the process");
		}

		return outageList;
	}

	private List<OutagesInner> getOutagesById(List<OutagesInner> outageList, String id) {
		return outageList.stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
	}

	private List<EnhancedOutagesInner> buildOutagesCargo(List<OutagesInner> outages, String name) {
		return outages.stream().map(i -> new EnhancedOutagesInner(i.getId(), i.getBegin(), i.getEnd(), name))
				.collect(Collectors.toList());
	}

}
