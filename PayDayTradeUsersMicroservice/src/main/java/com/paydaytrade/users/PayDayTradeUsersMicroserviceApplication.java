package com.paydaytrade.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@EnableCircuitBreaker
public class PayDayTradeUsersMicroserviceApplication {
	
	@Autowired
	Environment environment;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(PayDayTradeUsersMicroserviceApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@LoadBalanced  // Client side load balancing for RestTemplate
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
	
	@Bean
	feign.Logger.Level feignLoggerLevel() {
		
		return feign.Logger.Level.FULL;
	}
	
	@Bean
	@Profile("production")
	public String createProductionBean() {
		logger.info("**************** Production bean created: " + environment.getProperty("app.profile.env") + " ************************");
		return "Production bean";
	}

	
	@Bean
	@Profile("default")
	public String createDevelopmentBean() {
		logger.info("********************* Development bean created: " + environment.getProperty("app.profile.env") + " ************************");
		return "Development bean";
	}
	
}
