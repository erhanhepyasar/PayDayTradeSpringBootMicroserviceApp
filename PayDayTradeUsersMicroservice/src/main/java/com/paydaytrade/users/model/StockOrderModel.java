package com.paydaytrade.users.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StockOrderModel {
	
	@NotNull(message="Symbol cannot be null")
	private String symbol;
	
	@NotNull(message="Price cannot be null")
	private Double price;
	
	@NotNull(message="Quantity cannot be null")
	private Integer quantity;

}
