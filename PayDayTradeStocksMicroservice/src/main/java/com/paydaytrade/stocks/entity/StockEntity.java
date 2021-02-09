package com.paydaytrade.stocks.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="stocks")
public class StockEntity implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7349448222344049359L;
    
	@Id
	@GeneratedValue
	private Long id;
		
	@Column(nullable=false, length=5)
	private String symbol;
	
	@Column(nullable=false)
	private Integer quantity;
	
	@Column(nullable=false)
	private String userId;
	
	

}
