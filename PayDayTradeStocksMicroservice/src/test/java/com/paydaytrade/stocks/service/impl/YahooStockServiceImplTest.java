package com.paydaytrade.stocks.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paydaytrade.stocks.service.YahooStockService;

import yahoofinance.Stock;


@SpringBootTest
public class YahooStockServiceImplTest {
	
	@Autowired
	private YahooStockService yahooStockService;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	List<String> symbols = new ArrayList<>(
			List.of("GE", "HD", "MSFT", "GS", "JPM", "V", "CVX", "MRK", "AAPL", "VZ", "AMGN", "CRM", "PG",
			"DIS", "TRV", "IBM", "CAT", "MCD", "INTC", "MMM", "WBA", "AXP", "HON", "WMT", "BA", "KO", "UNH", "JNJ",
			"DOW", "CSCO", "NKE")); 
	
	List<Stock> stocks;
	

	@Test
	void findStockTest() throws IOException {
		logger.info("****************************************************");
		Stock stock = yahooStockService.findStock("GE");;
		BigDecimal price = yahooStockService.findPrice(stock);
		logger.info(stock.getSymbol());
		logger.info(stock.getName());
		logger.info(price.toString());
		logger.info(stock.getCurrency());
		logger.info(stock.getStockExchange());
		logger.info("****************************************************");
	}
	
	@Test
	void findStocksTest() {
		logger.info("=====================================================");
		stocks = yahooStockService.findStocks(symbols);
		findPrices();
		logger.info("=====================================================");
	}
	
	
	private void findPrices() {
		stocks.forEach(stock -> {
			try {
				BigDecimal price = yahooStockService.findPrice(stock);
				logger.info(price.toString());
				
			} catch (IOException e) {
				
			}
		});	
	}

	@Test
	void refreshStocksTest() {
		yahooStockService.refreshStocks(60);
		
	}

}
