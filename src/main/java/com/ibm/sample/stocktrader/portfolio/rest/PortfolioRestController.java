/*
       Copyright 2017-2019 IBM Corp All Rights Reserved

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ibm.sample.stocktrader.portfolio.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.sample.stocktrader.portfolio.model.Client;
import com.ibm.sample.stocktrader.portfolio.model.Portfolio;
import com.ibm.sample.stocktrader.portfolio.model.PortfolioUpdate;
import com.ibm.sample.stocktrader.portfolio.model.Stock;
import com.ibm.sample.stocktrader.portfolio.model.StockQuote;
import com.ibm.sample.stocktrader.portfolio.model.TradeHistory;
import com.ibm.sample.stocktrader.portfolio.repository.JdbcClientRepository;
import com.ibm.sample.stocktrader.portfolio.repository.JdbcPortfolioRepository;
import com.ibm.sample.stocktrader.portfolio.repository.JdbcStockRepository;
import com.ibm.sample.stocktrader.portfolio.repository.PortfolioRepository;

@RestController
public class PortfolioRestController {
	
	@Autowired
	JdbcClientRepository jdbcClientRepository;
	
	@Autowired
	JdbcStockRepository jdbcStockRepository;
	
	@Autowired
	JdbcPortfolioRepository jdbcPortfolioRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	Logger logger = LoggerFactory.getLogger(PortfolioRestController.class);
	
	private StockQuoteClient stockQuoteClient;
	
	private TradeHistoryClient tradeHistpryClient;
	
	@Value("${app.stockquote.host}")
	private String stockQuoteServiceHost;
	
	@Value("${app.tradehistory.host}")
	private String tradeHistoryServiceHost;
	
	@GetMapping("/stocks/{id}")
    public List<Stock> getStocks(@PathVariable String id){
        int portfolioId = Integer.parseInt(id);
        return jdbcStockRepository.findByPortfolio(portfolioId)	;
    }
	
	@GetMapping("/portfolios/{id}")
    public Portfolio getPortfolio(@PathVariable String id){
        int portfolioId = Integer.parseInt(id);
        return jdbcPortfolioRepository.findById(portfolioId).get()	;
    }
	
	@GetMapping("/portfolios")
    public List<Portfolio> getPortfolios(){
        return jdbcPortfolioRepository.findAll();
    }
	
	@PutMapping(path = "/portfolios", consumes = "application/json", produces = "application/json")
    public Portfolio updatePortfolio(@RequestBody PortfolioUpdate update) {
		
		String symbolsParam = null;
		List<String> symbols = jdbcStockRepository.findSymbolsByPortfolio(update.getPortfolioId());
		if ((symbols != null) && (symbols.size() > 0)) {
			StringBuilder strBuilder = new StringBuilder(update.getSymbol());
			for (String symbol : symbols) {
				if (!symbol.equals(update.getSymbol()))
				   strBuilder.append("," + symbol);
			}
			symbolsParam = strBuilder.toString();
			
		}
		else 
			symbolsParam = update.getSymbol();
		
		logger.info("symbolsParam is " + symbolsParam);
		stockQuoteClient = new StockQuoteClient(restTemplate, stockQuoteServiceHost);
		List<StockQuote> quotes = stockQuoteClient.getQuote(symbolsParam);
		for (StockQuote quote : quotes) {
			if (!"ok".equals(quote.getStatus())) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,quote.getReason());
			}
		}
		
		
		logger.info("StockQuote services returned " + quotes.size() + " quotes");
        Portfolio updatedPortfolio =  jdbcPortfolioRepository.tradeEquities(update, quotes).get();
        TradeHistory lastTrade = updatedPortfolio.getLastTrade();
        lastTrade.setTransactionSource("Portfolio Service");
        TradeHistoryClient tradeHistoryClient = new TradeHistoryClient(restTemplate, tradeHistoryServiceHost);
        tradeHistoryClient.storeTradeHistory(updatedPortfolio.getLastTrade());
        return updatedPortfolio;
        
    }
	

	@PostMapping(path = "/portfolios", consumes = "application/json", produces = "application/json")
	 //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio createPortfolio(@RequestBody Client client){
		Portfolio portfolio = new Portfolio();
		client.setClientId(UUID.randomUUID().toString());
		portfolio.setClientId(client.getClientId());
        jdbcPortfolioRepository.save(portfolio, client);   
        return jdbcPortfolioRepository.findByClientId(client.getClientId()).get();
    }
	
	@GetMapping("/readiness")
    public String  ready() {
        return "SpringBoot Portfolio microservice ready !";
    }


}
