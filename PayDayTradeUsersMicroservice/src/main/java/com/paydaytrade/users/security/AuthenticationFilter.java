package com.paydaytrade.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paydaytrade.users.model.LoginRequestModel;
import com.paydaytrade.users.model.UserResponseModel;
import com.paydaytrade.users.service.UsersService;
import com.paydaytrade.users.shared.UserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UsersService usersService;
	private Environment environment;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public AuthenticationFilter(UsersService usersService, 
			Environment environment, 
			AuthenticationManager authenticationManager) {
		this.usersService = usersService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}
	
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
  
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginRequestModel.class);
            
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	String userName = ((User) auth.getPrincipal()).getUsername();
    	UserDto user = usersService.getUserByEmail(userName);
    	
//    	Generating Json Web Token
        String token = Jwts.builder()
                .setSubject(user.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret") )
                .compact();
        
//      Adding parameters to Http Response Headers
        res.addHeader("token", token);
        res.addHeader("userId", user.getUserId());
        
        logger.info("Json web token generated.");
    } 
    
	
}