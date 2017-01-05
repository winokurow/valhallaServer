package data;

public enum GameStatus {
	WAITING("WAITING"), ENDED("ENDED");

	private final String value;

	GameStatus(String value) {
		this.value = value;
	}

	public String asString() {
		return this.value;
	}
}
