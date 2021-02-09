package com.paydaytrade.users.service.impl;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.paydaytrade.users.client.StocksServiceClient;
import com.paydaytrade.users.entity.UserEntity;
import com.paydaytrade.users.model.StockOrderModel;
import com.paydaytrade.users.model.UserResponseModel;
import com.paydaytrade.users.model.UserStocksResponseModel;
import com.paydaytrade.users.model.client.StockOrderClientModel;
import com.paydaytrade.users.model.client.StockResponseModel;
import com.paydaytrade.users.model.client.StockUserResponseModel;
import com.paydaytrade.users.repository.UsersRepository;
import com.paydaytrade.users.service.UsersService;
import com.paydaytrade.users.shared.UserDto;

@Service
public class UsersServiceImpl implements UsersService {

	final UsersRepository usersRepository;
	final BCryptPasswordEncoder bCryptPasswordEncoder;
	final StocksServiceClient stocksServiceClient;
	final Environment environment;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	ModelMapper modelMapper = new ModelMapper();

	@Autowired
	public UsersServiceImpl(
			UsersRepository usersRepository, 
			BCryptPasswordEncoder bCryptPasswordEncoder,
			RestTemplate restTemplate, 
			StocksServiceClient stocksServiceClient, 
			Environment environment) {
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.stocksServiceClient = stocksServiceClient;
		this.environment = environment;		
	}
	
	@Override
	public List<UserResponseModel> getUsers() {
		List<UserEntity> userEntities = usersRepository.findAll();
		Type type = new TypeToken<List<UserResponseModel>>(){}.getType();
		logger.info(userEntities.size() + " users returned.");
		return modelMapper.map(userEntities, type);
	}


	@Override
	public UserResponseModel createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		final UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		UserEntity savedUser = usersRepository.save(userEntity);
		logger.info("User created: " + savedUser.getEmail());
		return modelMapper.map(savedUser, UserResponseModel.class);
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final UserEntity userEntity = usersRepository.findByEmail(username);
		if (userEntity == null)	throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), 
				userEntity.getEncryptedPassword(), 
				true, // TODO Set "enabled" parameter "true" after registering e-mail verification																					
				true, true, true, new ArrayList<>());
	}

	
	@Override
	public UserResponseModel getUserByUserId(String userId) {
		final UserEntity userEntity = usersRepository.findByUserId(userId);
		if (userEntity == null)	throw new UsernameNotFoundException("User not found");
		return modelMapper.map(userEntity, UserResponseModel.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		final UserEntity userEntity = usersRepository.findByEmail(email);
		if (userEntity == null)	throw new UsernameNotFoundException(email);
		return modelMapper.map(userEntity, UserDto.class);
	}

	
	@Override
	public UserStocksResponseModel getUserWithStocksByUserId(String userId) {
		final UserResponseModel user = getUserByUserId(userId);
		final UserStocksResponseModel userWithStocks = modelMapper.map(user, UserStocksResponseModel.class);
		final List<StockUserResponseModel> stocks = stocksServiceClient.getStocksByUserId(userId);
		userWithStocks.setStocks(stocks);
		return userWithStocks;
	}

	@Override
	public UserResponseModel depositCashByUserId(String userId, String amount) {
		final UserEntity userEntity = usersRepository.findByUserId(userId);
		Double amountDouble = 0.0;
		try {
			amountDouble = Double.valueOf(amount);
		} catch (NumberFormatException e) {
			logger.error("Number format error: Cash amount");
		}
		
		Double newAmount = userEntity.getCash() + amountDouble;
		String newAmountStrFormatted = new DecimalFormat(".##").format(newAmount);
		newAmount = Double.valueOf(newAmountStrFormatted);
		userEntity.setCash(newAmount);
		UserEntity userEntityUpdated = usersRepository.save(userEntity);
		return modelMapper.map(userEntityUpdated, UserResponseModel.class);
	}
	


	@Override
	public StockOrderModel buyStock(String userId, StockOrderModel order) {
		StockOrderModel stock = getStockBySymbol(order.getSymbol());
		if (order.getPrice() >= stock.getPrice()) {
			try {
				final UserEntity userEntity = usersRepository.findByUserId(userId);
				Double cash = userEntity.getCash();
				int buyQuantity = (int) (cash / stock.getPrice());
				if (order.getQuantity() <= buyQuantity) {
					buyQuantity = order.getQuantity();
				}
				cash = cash - buyQuantity * stock.getPrice();
				String cashStrFormatted = new DecimalFormat(".##").format(cash);
				cash = Double.valueOf(cashStrFormatted);
				userEntity.setCash(cash);
				
				order.setQuantity(buyQuantity);
				order.setPrice(stock.getPrice());
				StockOrderClientModel stockOrder = modelMapper.map(order, StockOrderClientModel.class);
				stockOrder.setUserId(userId);
				stocksServiceClient.saveStock(stockOrder);
				UserEntity userEntityUpdated = usersRepository.save(userEntity);
			} catch (Exception e) {
				order.setQuantity(0);
			}

			
		} else {
			order.setQuantity(0);
		}
		
		return order;
	}

	@Override
	public StockOrderModel sellStock(String userId, StockOrderModel order) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private StockOrderModel getStockBySymbol(String symbol) {
		StockResponseModel stockResponse = stocksServiceClient.getStockBySymbol(symbol);
		return modelMapper.map(stockResponse, StockOrderModel.class);
	}


	
}
