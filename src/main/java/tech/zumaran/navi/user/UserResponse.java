package tech.zumaran.navi.user;

abstract class UserResponse {

	private String message;

	private User user;
	
	public UserResponse(User user, String message) {
		this.user = user;
		this.message =  message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public User getUser() {
		return user;
	}
}
