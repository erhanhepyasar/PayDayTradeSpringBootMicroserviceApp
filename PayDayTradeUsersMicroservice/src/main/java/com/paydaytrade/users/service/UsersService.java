package com.paydaytrade.users.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.paydaytrade.users.model.StockOrderModel;
import com.paydaytrade.users.model.UserResponseModel;
import com.paydaytrade.users.model.UserStocksResponseModel;
import com.paydaytrade.users.shared.UserDto;

public interface UsersService extends UserDetailsService{
	
	UserResponseModel createUser(UserDto userDto);
	List<UserResponseModel> getUsers();
	UserResponseModel getUserByUserId(String userId);
	UserDto getUserByEmail(String email);
	UserStocksResponseModel getUserWithStocksByUserId(String userId);
	UserResponseModel depositCashByUserId(String userId, String amount);
	StockOrderModel buyStock(String userId, StockOrderModel order);
	StockOrderModel sellStock(String userId, StockOrderModel order);
	
}
