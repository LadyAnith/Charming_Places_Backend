//package com.charmingplaces.security.model;
//
//import java.util.Collection;
//import java.util.Collections;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//
//import com.google.firebase.auth.UserRecord;
//
//public class UserModel implements Authentication {
//	private static final long serialVersionUID = 1L;
//
//	private UserRecord userRecord;
//
//	public UserModel(UserRecord userRecord) {
//		this.userRecord = userRecord;
//	}
//
//	@Override
//	public String getName() {
//		return userRecord.getDisplayName();
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Collections.emptyList();
//	}
//
//	@Override
//	public Object getCredentials() {
//		return null;
//	}
//
//	@Override
//	public Object getDetails() {
//		return null;
//	}
//
//	@Override
//	public Object getPrincipal() {
//		return null;
//	}
//
//	@Override
//	public boolean isAuthenticated() {
//		return false;
//	}
//
//	@Override
//	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
//		// Empty content
//	}
//
//
//}
