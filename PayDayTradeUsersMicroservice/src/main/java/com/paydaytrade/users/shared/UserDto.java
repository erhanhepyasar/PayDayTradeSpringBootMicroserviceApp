package com.paydaytrade.users.shared;

import java.io.Serializable;
import java.util.List;

import com.paydaytrade.users.model.client.StockUserResponseModel;

import lombok.Data;

@Data
public class UserDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5101454271490934830L;
	
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private Double cash;
	private String password;
	private String encryptedPassword;
	

}
