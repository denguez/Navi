package tech.zumaran.navi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tech.zumaran.navi.user.UserService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthProvider authProvider;
    
    @Autowired 
    private SignInSuccesHandler loginSuccess;
    
    @Autowired
    private SignInFailureHandler loginFailure;

    /*@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    	/*.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    	.and()*/
		.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/signup").permitAll()
			//.antMatchers(Endpoint.ADMIN + "/**", Endpoint.USER + "/**"/*, Endpoint.ROLE + "/**"*/)
			//	.hasAuthority(Authority.ADMIN.name())
			.anyRequest().authenticated()
		.and()
		.formLogin()
			//.loginPage(Endpoint.SIGN_IN)
			.usernameParameter("email")
			.loginProcessingUrl("/signin")
            .successHandler(loginSuccess)
            .failureHandler(loginFailure)
			.permitAll()
		.and()
		/*.logout()
			.logoutUrl(Endpoint.SIGN_OUT)
			.logoutSuccessHandler(logoutSuccessHandler)
			.and()*/
			
		/*.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.and()*/
    		
    		.csrf().disable();
    }
    
    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    	auth.authenticationProvider(authProvider);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public AccessDeniedHandler accessDeniedHandler() {
    	return new AccessDenied();
    }*/
        
}
