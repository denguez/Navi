package tech.zumaran.navi.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.ObjectError;

public class InvalidUser_Response extends UserResponse {
	
	public InvalidUser_Response(User user, List<ObjectError> errors) {
		super(user, errors == null
				? "No message available" 
				: "Invalid user " + user.getEmail() + " " + errors.stream()
					.map(err -> err.getDefaultMessage())
					.collect(Collectors.joining(" ")));
	}
}
