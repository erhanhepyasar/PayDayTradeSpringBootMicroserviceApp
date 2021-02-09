package com.paydaytrade.stocks.model;

import lombok.Data;

@Data
public class StockOrderClientModel {
	
	private String symbol;
	private Integer quantity;
	private String userId;

}
