package com.paydaytrade.users.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6931182370393085543L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String userId;

	@Column(nullable=true, length=50)
	private String firstName;
	
	@Column(nullable=true, length=50)
	private String lastName;
	
	@Column(nullable=false, length=120, unique=true)
	private String email;
	
	@Column(nullable=false, unique=true)	
	private String encryptedPassword;
	
	@Column(nullable=false)	
	private Double cash;

}
