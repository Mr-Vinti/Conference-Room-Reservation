package com.mps.rooms.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final String ALL_USERS_GROUP_ID = "f4993bb0-813b-4aa1-8e9d-dfd61f0408dd";

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/api/**").hasAnyAuthority("SCOPE_access_as_user", ALL_USERS_GROUP_ID);

		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true);

		http.authorizeRequests().anyRequest().permitAll();

		http.csrf().disable();
		
		http.cors();
		
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new CustomJwtAuthenticationConverter());

	}
}