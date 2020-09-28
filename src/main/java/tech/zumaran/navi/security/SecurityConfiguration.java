package tech.zumaran.navi.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private UserDetailsService userService;
    
    @Autowired
    private AuthProvider authProvider;
    
    @Autowired 
    private SignInSuccessHandler loginSuccess;
    
    @Autowired
    private SignInFailureHandler loginFailure;
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	.sessionManagement()
    		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    	.and()
		.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/signup").permitAll()
			.anyRequest().authenticated()
		.and()
		.formLogin()
			.usernameParameter("email")
			.loginProcessingUrl("/signin")
            .successHandler(loginSuccess)
            .failureHandler(loginFailure)
			.permitAll()
		.and()
		.exceptionHandling()
			.authenticationEntryPoint((req, resp, exc) -> {
				log.info(exc.getMessage());
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			})
		.and()
		.csrf()
			.disable();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    	auth.authenticationProvider(authProvider);
    }
}
