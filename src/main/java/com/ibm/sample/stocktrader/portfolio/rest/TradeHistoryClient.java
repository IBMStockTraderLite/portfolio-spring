package com.ibm.sample.stocktrader.portfolio.rest;

import org.springframework.web.client.RestTemplate;

import com.ibm.sample.stocktrader.portfolio.model.TradeHistory;

public class TradeHistoryClient {
	
	
private static final String RESOURCE_PATH = "/trade-history/trade";
	
	private String REQUEST_URI;
	private RestTemplate restTemplate;
	
	public TradeHistoryClient(RestTemplate restTemplate, String host) {
		this.restTemplate = restTemplate;
		this.REQUEST_URI = host + RESOURCE_PATH;
		
	}
	
	public TradeHistory storeTradeHistory(TradeHistory tradeHistory) {
		
		return restTemplate.postForObject(REQUEST_URI, tradeHistory, TradeHistory.class);
		
	}
	
	

}
