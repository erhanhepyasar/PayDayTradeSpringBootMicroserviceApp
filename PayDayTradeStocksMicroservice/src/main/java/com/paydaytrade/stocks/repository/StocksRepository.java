package com.paydaytrade.stocks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.paydaytrade.stocks.entity.StockEntity;

public interface StocksRepository extends CrudRepository<StockEntity, Long> {
	
	List<StockEntity> findByUserId(String userId);

}
