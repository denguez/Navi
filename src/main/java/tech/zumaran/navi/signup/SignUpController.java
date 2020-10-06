package tech.zumaran.navi.signup;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.zumaran.navi.user.User;
import tech.zumaran.navi.user.UserValidator;

@RestController
public class SignUpController {
	
	@Autowired
	private SignUpService signUpService;
	
	@Autowired
	private UserValidator userValidator;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponse> signup(@RequestBody User user, BindingResult result) {
		userValidator.validate(user, result);
		
        if (result.hasErrors()) {
        	String errors = result.getAllErrors().stream()
    							.map(err -> err.getDefaultMessage())
    							.collect(Collectors.joining(", "));
        	
        	return ResponseEntity.badRequest().body(new SignUpResponse("Invalid user " + errors, user));
        } else {
        	return ResponseEntity.ok(new SignUpResponse("Signed up successfully!", signUpService.signup(user)));
        }
	}
	
}
