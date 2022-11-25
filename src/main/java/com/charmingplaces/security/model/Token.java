package com.example.apirestfirebase.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class Token extends AbstractAuthenticationToken {

	private String token;

	public Token(String token) {
		super(null);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

}
