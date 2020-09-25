package tech.zumaran.navi.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SignInSuccesHandler implements AuthenticationSuccessHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(SignInSuccesHandler.class);
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
    		HttpServletResponse response, Authentication auth) {
    	
        LOG.info("Welcome " + auth.getName() + "!");
		response.setStatus(HttpServletResponse.SC_OK);
    }
}
