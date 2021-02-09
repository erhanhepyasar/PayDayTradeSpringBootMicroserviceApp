package com.paydaytrade.users.security;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paydaytrade.users.service.UsersService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private final Environment environment;
	private final UsersService usersService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public WebSecurity(Environment environment, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder)
	{
		this.environment = environment;
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/**").hasIpAddress(this.getLocalIpAddress())
			.and()
			.addFilter(getAuthenticationFilter());
		http.headers().frameOptions().disable(); // let h2 console page can be seen
	}
	
	private AuthenticationFilter getAuthenticationFilter() throws Exception	{
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(usersService, environment, authenticationManager());
//		authenticationFilter.setAuthenticationManager(authenticationManager()); 
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }
	
	private String getLocalIpAddress() {
		String ipAdress = "";
		
		try {
			ipAdress = InetAddress.getLocalHost().getHostAddress().trim();
		} catch (UnknownHostException e) {
			ipAdress = environment.getProperty("gateway.ip");
		}
		
		logger.info("Local IP Adress: " + ipAdress);
		
		return ipAdress;
	}


}
