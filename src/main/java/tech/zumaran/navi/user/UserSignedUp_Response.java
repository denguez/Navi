package tech.zumaran.navi.user;

public class UserSignedUp_Response extends UserResponse {

	public UserSignedUp_Response(User user) {
		super(user, "User signed up successfully " + user.getEmail());
	}

}
