package tech.zumaran.navi.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.zumaran.navi.user.User;

@AllArgsConstructor
public class SignUpResponse {
	@Getter private String message;
	@Getter private User user;
}
