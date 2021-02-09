package com.paydaytrade.stocks.model;

import lombok.Data;

@Data
public class StockResponseModel {
	
	private String symbol;	
	private String name;
	private Double price;
	private String currency;
	private String stockExchange;

}
