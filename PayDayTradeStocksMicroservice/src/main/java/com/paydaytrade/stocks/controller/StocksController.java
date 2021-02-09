package com.paydaytrade.stocks.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paydaytrade.stocks.entity.StockEntity;
import com.paydaytrade.stocks.model.StockOrderClientModel;
import com.paydaytrade.stocks.model.StockResponseModel;
import com.paydaytrade.stocks.model.UserStockResponseModel;
import com.paydaytrade.stocks.service.StocksService;
import com.paydaytrade.stocks.service.YahooStockService;


@RestController
@RequestMapping("stocks")
public class StocksController {
	
	@Autowired
	StocksService stocksService;
	
	@Autowired
	YahooStockService yahooStockService;
	
	@Autowired
	private Environment env;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
		

	@GetMapping("status")
	public String status() {
		return "Stocks MS is working on port: " + env.getProperty("local.server.port");
	}
	
	@GetMapping
	public List<StockResponseModel> getStocks(){
		List<StockResponseModel> stocks = stocksService.getStocks();
		if(stocks == null || stocks.isEmpty())
        {
            return new ArrayList<StockResponseModel>();
        }
        
        logger.info("Returning " + stocks.size() + " stocks");
        return stocks;
	}
	
	@GetMapping("{symbol}")
	public StockResponseModel getStockBySymbol(@PathVariable String symbol){
        return stocksService.getStockBySymbol(symbol);
	}
	
	

    @GetMapping("users/{id}")
    public List<UserStockResponseModel> getStocksByUserId(@PathVariable String id) {
        List<UserStockResponseModel> stockEntities = stocksService.getStocksByUserId(id);        
        logger.info("Returning " + stockEntities.size() + " stocks");
        return stockEntities;
    }
    
	@GetMapping("users/{id}/{symbol}")
	public UserStockResponseModel getStockByUserIdAndSymbol(
			@PathVariable String id, 
			@PathVariable String symbol) {
		return stocksService.getStockByUserIdAndSymbol(id, symbol);
	}
	
	@PostMapping
	public StockOrderClientModel saveStock(@RequestBody StockOrderClientModel stockOrder) {
		return stocksService.saveStock(stockOrder);
	}
	

}
