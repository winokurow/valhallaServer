package dto;

public class StringResponse {

	private boolean error;
	private String error_msg;
	private String text;

	public StringResponse() {
		super();
	}

	public StringResponse(boolean error, String error_msg, String text) {
		super();
		this.error = error;
		this.error_msg = error_msg;
		this.text = text;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
