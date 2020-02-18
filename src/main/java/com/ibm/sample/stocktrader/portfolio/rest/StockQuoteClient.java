package com.ibm.sample.stocktrader.portfolio.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ibm.sample.stocktrader.portfolio.model.StockQuote;
 
public class StockQuoteClient {
	
	private static final String RESOURCE_PATH = "/stock-quote";
	
	private String REQUEST_URI;
	private RestTemplate restTemplate;
	
	public StockQuoteClient(RestTemplate restTemplate, String host) {
		this.restTemplate = restTemplate;
		this.REQUEST_URI = host + RESOURCE_PATH;
		
	}
	
	public List<StockQuote> getQuote(String symbols) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
			    .fromUriString(REQUEST_URI)
			    // Add query parameter
			    .queryParam("symbols", symbols);
		
		ResponseEntity<List<StockQuote>> response = restTemplate.exchange(uriBuilder.toUriString(),HttpMethod.GET,null,new ParameterizedTypeReference<List<StockQuote>>(){});
			   
		List<StockQuote> stockQuotes = response.getBody();
		
		return stockQuotes;
	}

}
