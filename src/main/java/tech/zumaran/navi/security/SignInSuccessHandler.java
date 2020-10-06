package tech.zumaran.navi.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import tech.zumaran.navi.user.User;

@Slf4j
@Component
public class SignInSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private UserDetailsService userService;
    
    @Autowired
    private JWTConfig jwt;
    
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
    			.claim("authorities", authorities)
    			.setIssuedAt(new Date(now))
    			.setExpiration(new Date(now + jwt.getExpirationMillis()))
    			.signWith(SignatureAlgorithm.HS512, jwt.getSecret().getBytes())
    			.compact();
    	
        log.info("Welcome " + user.getEmail() + "!");
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader(jwt.getHeader(), jwt.getPrefix() + token);
    }
}
