package com.paydaytrade.stocks.model.helper;

import java.util.List;
import java.util.stream.Collectors;

import com.paydaytrade.stocks.model.StockResponseModel;

import yahoofinance.Stock;

public class StockModelMapper {
	
	public static StockResponseModel mapStockToStockResponseModel(Stock stock) {
		StockResponseModel stockResponseModel = new StockResponseModel();
		
		stockResponseModel.setSymbol(stock.getSymbol());
		stockResponseModel.setName(stock.getName());
		stockResponseModel.setPrice(stock.getQuote().getPrice().doubleValue());
		stockResponseModel.setCurrency(stock.getCurrency());
		stockResponseModel.setStockExchange(stock.getStockExchange());
		
		return stockResponseModel;
	}
	
	public static List<StockResponseModel> mapStockToStockResponseModel(List<Stock> stocks) {
		return stocks.stream()
					 .map(StockModelMapper::mapStockToStockResponseModel)
					 .collect(Collectors.toList());
		
	}

}
