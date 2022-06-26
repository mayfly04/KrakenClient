package com.mayfly.krakenclient.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mayfly.kraken.client.model.Outages;
import com.mayfly.kraken.client.model.SiteInfo;

public final class TestUtils {

	
	private static ObjectMapper objMapper = new ObjectMapper();
	
	static {
		objMapper.registerModule(new JavaTimeModule());
		objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	public static Outages getOutagesCargo() throws FileNotFoundException, IOException {
		File file = ResourceUtils.getFile("classpath:Outages.json");
		return objMapper.readValue(file, Outages.class);
	}
	
	
	public static SiteInfo getSiteInfoCargo() throws FileNotFoundException, IOException {
		File file = ResourceUtils.getFile("classpath:SiteInfo.json");		
		return objMapper.readValue(file, SiteInfo.class);
	}
	
	public static SiteInfo getNonMatchSiteInfoCargo() throws FileNotFoundException, IOException {
		File file = ResourceUtils.getFile("classpath:NonMatchSiteInfo.json");		
		return objMapper.readValue(file, SiteInfo.class);
	}
}
