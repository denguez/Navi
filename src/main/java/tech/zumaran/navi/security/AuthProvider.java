package tech.zumaran.navi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import tech.zumaran.navi.user.User;

@Component
public class AuthProvider implements AuthenticationProvider {
	
    @Autowired
    private UserDetailsService userService;
    
    @Autowired
    private BCryptPasswordEncoder crypto;

	@Override
	public Authentication authenticate(Authentication attempt) throws AuthenticationException {
		checkEmpty(attempt);
		User user = userService.findByEmail(attempt.getName());
		UsernamePasswordAuthenticationToken auth = generateToken(user);
		
		if (crypto.matches(attempt.getCredentials().toString(), auth.getCredentials().toString())) {
			return auth;
		} else {
			throw new AuthenticationServiceException("Failed authentication");
		}
	}
	
	private void checkEmpty(Authentication attempt) throws AuthenticationException {
		if (attempt == null) 
			throw new AuthenticationCredentialsNotFoundException("Authentication is empty");
		
		if (attempt.getPrincipal() == null) 
			throw new AuthenticationCredentialsNotFoundException("Missing email");
		
		if (attempt.getCredentials() == null) 
			throw new AuthenticationCredentialsNotFoundException("Missing credentials");
	}
	
	private static UsernamePasswordAuthenticationToken generateToken(User user) {
		return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
