package com.paydaytrade.stocks.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import yahoofinance.Stock;

public interface YahooStockService {
	
	Stock findStock(final String symbol);
	BigDecimal findPrice(final Stock stock) throws IOException;
	List<Stock> findStocks();
	List<Stock> findStocks(final List<String> symbols);
	void refreshStocks(final int refreshPeriodInSeconds);

}
