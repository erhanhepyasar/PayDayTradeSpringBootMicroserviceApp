package com.paydaytrade.users.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.paydaytrade.users.entity.UserEntity;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByUserId(String userId);

	UserEntity findByEmail(String email);
	
    @Override
    List<UserEntity> findAll();
	
	
}
