package com.paydaytrade.users.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paydaytrade.users.client.StocksServiceClient;
import com.paydaytrade.users.model.CreateUserRequestModel;
import com.paydaytrade.users.model.StockOrderModel;
import com.paydaytrade.users.model.UserResponseModel;
import com.paydaytrade.users.model.client.StockUserResponseModel;
import com.paydaytrade.users.service.UsersService;
import com.paydaytrade.users.shared.UserDto;

@RestController
@RequestMapping("users")
public class UsersController {
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	StocksServiceClient stocksServiceClient;
	
	@Autowired
	Environment env;

	ModelMapper modelMapper = new ModelMapper(); 
	

	@GetMapping("status")
	public String status() {
		return "Users MS is working on port: " + env.getProperty("local.server.port");
	}
	
	@PostMapping
	public ResponseEntity<UserResponseModel> createUser(
			@Valid @RequestBody CreateUserRequestModel userDetails) {		
		
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserResponseModel createdUser = usersService.createUser(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
	@GetMapping
	public ResponseEntity<List<UserResponseModel>> getUsers() {
		List<UserResponseModel> users = usersService.getUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}	
	
    @GetMapping("{userId}")
    public ResponseEntity<UserResponseModel> getUserById(@PathVariable("userId") String userId) {
        UserResponseModel user = usersService.getUserByUserId(userId); 
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("email/{email}")
    public ResponseEntity<UserResponseModel> getUserByEmail(@PathVariable("email") String email) {
           UserDto userDto = usersService.getUserByEmail(email);
           UserResponseModel user = modelMapper.map(userDto, UserResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    
    @GetMapping("{userId}/stocks")
    public ResponseEntity<List<StockUserResponseModel>> getStocksByUserId(
    		@PathVariable("userId") String userId) {
    	List<StockUserResponseModel> stocks = stocksServiceClient.getStocksByUserId(userId);
    	return ResponseEntity.status(HttpStatus.OK).body(stocks);
    }
    
    @GetMapping("{userId}/stocks/{symbol}")
    public ResponseEntity<StockUserResponseModel> getStockByUserIdAndSymbol(
    		@PathVariable("userId") String userId, 
    		@PathVariable("symbol") String symbol) {
		
    	StockUserResponseModel stock = stocksServiceClient.getStockByUserIdAndSymbol(userId, symbol);
    	return ResponseEntity.status(HttpStatus.OK).body(stock);
    }
    
    @PostMapping("{userId}/cash/{amount}")
    public ResponseEntity<UserResponseModel> depositCashByUserId(
    		@PathVariable("userId") String userId, 
			@PathVariable("amount") String amount) {
    	UserResponseModel returnedUser = usersService.depositCashByUserId(userId, amount);
    	return ResponseEntity.status(HttpStatus.OK).body(returnedUser);
    }
    
    @PostMapping("{userId}/stocks/buy")
    public ResponseEntity<StockOrderModel> buyStock(
    		@PathVariable("userId") String userId,
    		@Valid @RequestBody StockOrderModel order
    		) {
    	StockOrderModel returnedOrder = usersService.buyStock(userId, order);
    	return ResponseEntity.status(HttpStatus.OK).body(returnedOrder);
    }
    
    @PostMapping("{userId}/stocks/sell")
    public ResponseEntity<StockOrderModel> sellStock(
    		@PathVariable("userId") String userId,
    		@Valid @RequestBody StockOrderModel order
    		) {
    	StockOrderModel returnedOrder = usersService.sellStock(userId, order);
    	return ResponseEntity.status(HttpStatus.OK).body(returnedOrder);
    }
}
