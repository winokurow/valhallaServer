package dto;

import java.util.List;

public class GamesResponse {

	private boolean error;
	private String error_msg;
	private List<Game> game;

	public GamesResponse() {
		super();
	}

	public GamesResponse(boolean error, String error_msg, List<Game> game) {
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

	public List<Game> getGame() {
		return game;
	}

	public void setGame(List<Game> game) {
		this.game = game;
	}

}
