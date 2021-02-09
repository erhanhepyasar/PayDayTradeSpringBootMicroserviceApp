package com.paydaytrade.stocks.service.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paydaytrade.stocks.entity.StockEntity;
import com.paydaytrade.stocks.model.StockOrderClientModel;
import com.paydaytrade.stocks.model.StockResponseModel;
import com.paydaytrade.stocks.model.UserStockResponseModel;
import com.paydaytrade.stocks.model.helper.StockModelMapper;
import com.paydaytrade.stocks.repository.StocksRepository;
import com.paydaytrade.stocks.service.StocksService;
import com.paydaytrade.stocks.service.YahooStockService;

import yahoofinance.Stock;

@Service
public class StocksServiceImpl implements StocksService {

	@Autowired
	YahooStockService yahooStockService;
	
	@Autowired
	StocksRepository stocksRepository; 
	
	ModelMapper modelMapper = new ModelMapper();
	
	@Override
	public List<StockResponseModel> getStocks() {
		List<Stock> stocks = yahooStockService.findStocks();
		return StockModelMapper.mapStockToStockResponseModel(stocks);
	}

	@Override
	public StockResponseModel getStockBySymbol(String symbol) {
		Stock stock = yahooStockService.findStock(symbol);
		return StockModelMapper.mapStockToStockResponseModel(stock);
	}

	@Override
	public List<UserStockResponseModel> getStocksByUserId(String userId) {
		List<StockEntity> stockEntities = stocksRepository.findByUserId(userId);
		Type type = new TypeToken<List<UserStockResponseModel>>(){}.getType();
		return modelMapper.map(stockEntities, type);
	}

	@Override
	public UserStockResponseModel getStockByUserIdAndSymbol(String id, String symbol) {
		List<UserStockResponseModel> stocksByUserId = this.getStocksByUserId(id);
		List<UserStockResponseModel> stocksByUserIdAndSymbol = 
				stocksByUserId.stream()
				.filter(stock -> stock.getSymbol().equals(symbol))
				.collect(Collectors.toList());
		
		return stocksByUserIdAndSymbol.size() > 0 ? stocksByUserIdAndSymbol.get(0) : null;
	}

	@Override
	public StockOrderClientModel saveStock(StockOrderClientModel stockOrder) {
		// TODO Auto-generated method stub
		
		// Check if update or save
		StockEntity stockEntity = null;
		List<StockEntity> stocksByUserId = stocksRepository.findByUserId(stockOrder.getUserId());
		if (stocksByUserId != null && stocksByUserId.size() > 0) {
			List<StockEntity> stocksByStockId = stocksByUserId.stream()
				.filter(stock -> stock.getSymbol().equals(stockOrder.getSymbol()))
				.collect(Collectors.toList());
			
			if (stocksByStockId.size() > 0) {
				stockEntity = stocksByStockId.get(0);
			}
		}
		
		// Update
		if (stockEntity != null) {
			stockEntity.setQuantity(stockEntity.getQuantity() + stockOrder.getQuantity());
		// Save
		} else {
			stockEntity = new StockEntity();
			stockEntity.setUserId(stockOrder.getUserId());
			stockEntity.setQuantity(stockOrder.getQuantity());
			stockEntity.setSymbol(stockOrder.getSymbol());
		}
		
//		FIXME StockEntity stockEntity = modelMapper.map(stockOrder, StockEntity.class);
		StockEntity savedStockEntity = stocksRepository.save(stockEntity);
		StockOrderClientModel stockClient = modelMapper.map(savedStockEntity, StockOrderClientModel.class);
		
		return stockClient;
	}
 

	

}
