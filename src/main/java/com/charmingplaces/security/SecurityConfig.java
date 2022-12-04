//package com.charmingplaces.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import com.charmingplaces.security.token.FirebaseEntryPoint;
//import com.charmingplaces.security.token.FirebaseFilter;
//import com.charmingplaces.security.token.FirebaseProvider;
//
//@Configuration
//public class SecurityConfig {
////Versión vieja deprecada public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	FirebaseEntryPoint entryPoint;
//
//	@Autowired
//	FirebaseProvider provider;
//	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeRequests().anyRequest().anonymous();
////		http.exceptionHandling().authenticationEntryPoint(entryPoint);
//		http.addFilterBefore(new FirebaseFilter(), BasicAuthenticationFilter.class);
//		http.authenticationProvider(provider);
//		return http.build();
//	}
//
////	@Override
////	protected void configure(HttpSecurity http) throws Exception {
////		http.authorizeRequests().anyRequest().authenticated().and().exceptionHandling()
////				.authenticationEntryPoint(entryPoint);
////		http.addFilterBefore(new FirebaseFilter(), BasicAuthenticationFilter.class);
////	}
////
////	@Override
////	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////		auth.authenticationProvider(provider);
////	}
//
//}
