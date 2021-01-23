package com.mps.rooms.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.StringUtils;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
	private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Override
	public AbstractAuthenticationToken convert(final Jwt source) {
		
		 Collection<? extends GrantedAuthority> groupAuthorities
         = getGroupIds(source).stream()
         .map(authority -> new SimpleGrantedAuthority(authority))
         .collect(Collectors.toList());

		
		Collection<GrantedAuthority> authorities = Stream
				.concat(defaultGrantedAuthoritiesConverter.convert(source).stream(),
						groupAuthorities.stream())
				.collect(Collectors.toSet());
		return new JwtAuthenticationToken(source, authorities);
	}
	
	@SuppressWarnings("unchecked")
	private Collection<String> getGroupIds(Jwt jwt) {
		String claimName = "groups";

		Object authorities = jwt.getClaim(claimName);
		if (authorities instanceof String) {
			if (StringUtils.hasText((String) authorities)) {
				return Arrays.asList(((String) authorities).split(" "));
			} else {
				return Collections.emptyList();
			}
		} else if (authorities instanceof Collection) {
			return (Collection<String>) authorities;
		}

		return Collections.emptyList();
	}
}