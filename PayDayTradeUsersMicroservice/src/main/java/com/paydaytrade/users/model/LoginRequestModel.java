package com.paydaytrade.users.model;

import lombok.Data;

@Data
public class LoginRequestModel {
	private String email;
	private String password;
}
