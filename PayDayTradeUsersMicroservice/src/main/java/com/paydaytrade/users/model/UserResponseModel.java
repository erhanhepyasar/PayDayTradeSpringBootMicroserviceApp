package com.paydaytrade.users.model;

import lombok.Data;

@Data
public class UserResponseModel {
	
	private String firstName;
	private String lastName;
	private String email;
	private Double cash;

}
