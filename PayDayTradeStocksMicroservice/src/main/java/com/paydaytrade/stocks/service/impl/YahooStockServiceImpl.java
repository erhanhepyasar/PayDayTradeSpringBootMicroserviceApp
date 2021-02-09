package com.paydaytrade.stocks.service.impl;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.paydaytrade.stocks.service.YahooStockService;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//@AllArgsConstructor //For contructor injection
@Service
public class YahooStockServiceImpl implements YahooStockService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	List<String> symbols = new ArrayList<>(
			List.of("GE", "HD", "MSFT", "GS", "JPM", "V", "CVX", "MRK", "AAPL", "VZ", "AMGN", "CRM", "PG",
			"DIS", "TRV", "IBM", "CAT", "MCD", "INTC", "MMM", "WBA", "AXP", "HON", "WMT", "BA", "KO", "UNH", "JNJ",
			"DOW", "CSCO", "NKE")); 
	
	private List<Stock> stocks;
	

	public Stock findStock(final String symbol) { 
		Stock stock = null;
	
		try {
			
			stock = YahooFinance.get(symbol);
		
		} catch (IOException e) {
		
			logger.error(e.getLocalizedMessage());
		}

		return stock;
	}
	
	public List<Stock> findStocks() {
		
		return findStocks(symbols);
	}
	
	public List<Stock> findStocks(final List<String> symbols) {
		
		return symbols.stream().map(this::findStock).filter(Objects::nonNull).collect(Collectors.toList());
	}


	public BigDecimal findPrice(final Stock stock) throws IOException {

		return stock.getQuote(true).getPrice();
	}
	
	public void refreshStocks(final int refreshPeriodInSeconds) {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		scheduler.scheduleAtFixedRate(() -> findStocks(symbols), 0, refreshPeriodInSeconds, SECONDS);
	}
	

}
