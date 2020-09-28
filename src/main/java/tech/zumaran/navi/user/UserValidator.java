package tech.zumaran.navi.user;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import tech.zumaran.navi.security.UserDetailsService;

@Component
public class UserValidator implements Validator {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserValidator.class);
	
	@Autowired
	private UserDetailsService userService;
	
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Empty",
				"Field name can't be empty.");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Empty",
				"Field email can't be empty.");
		
		if (user.getEmail() == null) {
			return;
		}
		
		if (!validateEmail(user.getEmail()))
			errors.rejectValue("email", "Email.format", "Invalid email format.");
		
		try {
			userService.findByEmail(user.getEmail());
			errors.rejectValue("email", "Email.duplicate", "There's other user registered with this email.");
		} catch (Exception e) {
			LOG.warn(e.getMessage());
		}
		
		if (user.getPassword() == null) {
			errors.rejectValue("password", "Empty","Field password can't be empty.");
			return;
		}
		
		if (user.getPasswordConfirm() == null) {
			errors.rejectValue("password", "Empty","Field password confirm can't be empty.");
			return;
		}
		
		if (user.getPassword().length() < 8) 
    		errors.rejectValue("password", "Password.size", "Password must be at least 8 characters long.");
    
    	if (!user.getPasswordConfirm().equals(user.getPassword())) 
    		errors.rejectValue("passwordConfirm", "Password.diff", "Passwords do not match");
        
	}
	
	boolean validateEmail(String email) {
		if (email == null) return false;
		final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(email).matches();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(User.class);
	}
}

