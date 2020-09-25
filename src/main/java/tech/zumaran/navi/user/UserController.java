package tech.zumaran.navi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;

	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@RequestBody User user, BindingResult result) {
		userValidator.validate(user, result);
        if (result.hasErrors()) 
        	return ResponseEntity.badRequest().body(new InvalidUser_Response(user, result.getAllErrors()));
        else
        	return ResponseEntity.ok(new UserSignedUp_Response(userService.signup(user)));
	}
	
	@GetMapping("/account")
    public ResponseEntity<User> accountDetails() {
		return ResponseEntity.ok(userService.getAccountDetails());
    }
}
