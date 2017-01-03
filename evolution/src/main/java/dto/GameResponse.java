package dto;

public class GameResponse {

	private boolean error;
	private String error_msg;
	private Game game;

	public GameResponse() {
		super();
	}

	public GameResponse(boolean error, String error_msg, Game game) {
		super();
		this.error = error;
		this.error_msg = error_msg;
		this.game = game;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
