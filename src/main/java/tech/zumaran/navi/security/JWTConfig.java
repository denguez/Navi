package tech.zumaran.navi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class JWTConfig {

	@Value("${jwt.header}")
	private String header;
	
	@Value("${jwt.prefix}")
	private String prefix;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private int expiration;
	
	int getExpirationMillis() {
		return expiration * 1000;
	}
}
