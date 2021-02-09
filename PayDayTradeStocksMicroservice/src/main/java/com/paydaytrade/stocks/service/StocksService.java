package com.paydaytrade.stocks.service;

import java.util.List;

import com.paydaytrade.stocks.model.StockOrderClientModel;
import com.paydaytrade.stocks.model.StockResponseModel;
import com.paydaytrade.stocks.model.UserStockResponseModel;

public interface StocksService {
	
	List<StockResponseModel> getStocks();
	StockResponseModel getStockBySymbol(String symbol);
	List<UserStockResponseModel> getStocksByUserId(String userId);
	UserStockResponseModel getStockByUserIdAndSymbol(String id, String symbol);
	StockOrderClientModel saveStock(StockOrderClientModel stockOrder);
	
}
