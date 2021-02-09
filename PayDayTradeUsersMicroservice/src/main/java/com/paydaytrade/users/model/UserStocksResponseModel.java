package com.paydaytrade.users.model;

import java.util.List;

import com.paydaytrade.users.model.client.StockUserResponseModel;

import lombok.Data;

@Data
public class UserStocksResponseModel {
	
	private String userId;
	private Double cash;
	private List<StockUserResponseModel> stocks;
	

}
