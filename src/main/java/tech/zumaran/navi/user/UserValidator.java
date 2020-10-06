package tech.zumaran.navi.user;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;
import tech.zumaran.navi.security.UserDetailsService;

@Slf4j
@Component
public class UserValidator implements Validator {
	
	@Autowired
	private UserDetailsService userService;
	
	private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "empty", "Name can't be empty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "empty", "Email can't be empty");
		
		if (user.getEmail() == null) {
			return;
		}
		
		if (!validateEmail(user.getEmail()))
			errors.rejectValue("email", "email.format", "Invalid email format");
		
		try {
			userService.findByEmail(user.getEmail());
			errors.rejectValue("email", "email.duplicate", "There's other user registered with this email");
		} catch (Exception e) {
			log.warn("{} {}", e.getMessage(), "User is not duplicate");
		}
		
		if (user.getPassword() == null) {
			errors.rejectValue("password", "empty","Password can't be empty");
			return;
		}
		
		if (user.getPasswordConfirm() == null) {
			errors.rejectValue("password", "Empty","Password confirm can't be empty");
			return;
		}
		
		if (user.getPassword().length() < 8) 
    		errors.rejectValue("password", "password.size", "Password must be at least 8 characters long");
    
    	if (!user.getPasswordConfirm().equals(user.getPassword())) 
    		errors.rejectValue("passwordConfirm", "password.not.match", "Passwords do not match");
        
	}
	
	private boolean validateEmail(String email) {
		if (email == null) 
			return false;
		
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		return pattern.matcher(email).matches();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(User.class);
	}
}

