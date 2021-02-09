package com.paydaytrade.users.model.client;

import lombok.Data;

@Data
public class StockResponseModel {
	
	private String symbol;
	private String name;
	private Double price;
	private String currency;
	private String stockExchange;

}
