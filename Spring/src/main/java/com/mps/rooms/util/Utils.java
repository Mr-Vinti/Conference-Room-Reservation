package com.mps.rooms.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class Utils {
	public static String getUserEmail() {
		return ((Jwt) ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal())
				.getClaimAsString("preferred_username");
	}
}
