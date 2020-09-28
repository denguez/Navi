package tech.zumaran.navi.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import tech.zumaran.navi.authority.Authority;
import tech.zumaran.navi.user.User;

@Slf4j
@Component
public class SignInSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private UserDetailsService userService;
    
    @Value("${jwt.header}")
	private String header;
	
	@Value("${jwt.prefix}")
	private String prefix;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration:#{24*60*60}}")
	private int expiration;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response, Authentication auth) {
    	
    	User user = userService.updateLastLogin(auth.getName());
    	
    	List<String> authorities = user.getAuthorities().stream()
    			.map(a -> a.getAuthority())
    			.collect(Collectors.toList());
    	
    	long now = System.currentTimeMillis();
    	
    	String token = Jwts.builder()
    			.setSubject(user.getEmail())
    			.claim("id", user.getId())
    			.claim(Authority.class.getSimpleName(), authorities)
    			.setIssuedAt(new Date(now))
    			.setExpiration(new Date(now + expiration))
    			.signWith(SignatureAlgorithm.HS512, secret.getBytes())
    			.compact();
    	
        log.info("Welcome " + user.getEmail() + "!");
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader(header, prefix + token);
    }
}
