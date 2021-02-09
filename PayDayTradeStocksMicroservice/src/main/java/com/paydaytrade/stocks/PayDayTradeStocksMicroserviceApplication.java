package com.paydaytrade.stocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class PayDayTradeStocksMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayDayTradeStocksMicroserviceApplication.class, args);
	}
	
	@Bean
	@LoadBalanced  // Client side load balancing for RestTemplate
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}

}
