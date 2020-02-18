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
package com.ibm.sample.stocktrader.portfolio.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Stock {
	
	private int portfolioId;
	private String symbol;
	private int shares;
	private BigDecimal price = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	private BigDecimal total = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	private BigDecimal commission = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	private Timestamp lastQuoted;
	
	public Stock() {
		
	}
	
	public Stock(int portfolioId, String symbol,int shares,BigDecimal price, BigDecimal total, BigDecimal commission, Timestamp lastQuoted)  {
		this.portfolioId = portfolioId;
		this.symbol = symbol;
		this.shares = shares;
		this.price = price;
		this.total = total;
		this.commission = commission;
		this.lastQuoted = lastQuoted;
		
	}
	
	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public int getPortfolioId() {
		return portfolioId;
	}
	
	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Timestamp getLastQuoted() {
		return lastQuoted;
	}
	public void setLastQuoted(Timestamp lastQuoted) {
		this.lastQuoted = lastQuoted;
	}
	

}
