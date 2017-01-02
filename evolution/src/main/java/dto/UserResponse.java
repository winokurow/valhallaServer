package dto;

public class UserResponse {

	private boolean error;
	private String error_msg;
	private User user;

	public UserResponse() {
		super();
	}

	public UserResponse(boolean error, String error_msg, User user) {
		super();
		this.error = error;
		this.error_msg = error_msg;
		this.user = user;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
