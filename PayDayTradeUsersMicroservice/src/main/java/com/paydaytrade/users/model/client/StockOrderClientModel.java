package com.paydaytrade.users.model.client;

import lombok.Data;

@Data
public class StockOrderClientModel {
	
	private String symbol;
	private Integer quantity;
	private String userId;

}
