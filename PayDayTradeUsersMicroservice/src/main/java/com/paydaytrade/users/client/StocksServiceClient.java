package com.paydaytrade.users.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.paydaytrade.users.model.client.StockOrderClientModel;
import com.paydaytrade.users.model.client.StockResponseModel;
import com.paydaytrade.users.model.client.StockUserResponseModel;

@FeignClient(name = "stocks-ms", fallback = StocksFallback.class)
public interface StocksServiceClient {

	@GetMapping("stocks/{symbol}")
	public StockResponseModel getStockBySymbol(@PathVariable String symbol);
	
	
	@GetMapping("stocks/users/{id}")
	public List<StockUserResponseModel> getStocksByUserId(@PathVariable String id);
	
	@GetMapping("stocks/users/{id}/{symbol}")
	public StockUserResponseModel getStockByUserIdAndSymbol(@PathVariable String id, @PathVariable String symbol);

	@PostMapping("stocks")
	public StockOrderClientModel saveStock(@RequestBody StockOrderClientModel stockOrder);
	
}

/*******************************************************
 * 				Circuit Breaker						   *
 *******************************************************/
@Component
class StocksFallback implements StocksServiceClient {
	
	@Override
	public StockResponseModel getStockBySymbol(String symbol) {
		return new StockResponseModel();
	}

	@Override
	public List<StockUserResponseModel> getStocksByUserId(String id) {
		return new ArrayList<>();
	}

	@Override
	public StockUserResponseModel getStockByUserIdAndSymbol(String id, String symbol) {
		return new StockUserResponseModel();
	}

	@Override
	public StockOrderClientModel saveStock(StockOrderClientModel stockOrder) {
		return new StockOrderClientModel();
	}






}
