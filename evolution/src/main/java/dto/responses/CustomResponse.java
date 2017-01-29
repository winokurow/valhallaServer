package dto.responses;

public class CustomResponse<T> {

	private boolean error;
	private String error_msg;
	private T data;

	public CustomResponse() {
		super();
	}

	public CustomResponse(boolean error, String error_msg, T data) {
		super();
		this.error = error;
		this.error_msg = error_msg;
		this.data = data;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
