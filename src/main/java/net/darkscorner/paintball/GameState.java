package net.darkscorner.paintball;

public enum GameState {

	IDLE(0), COUNTDOWN(1), STARTED(2), ENDED(3);
	
	private final int state;
	
	GameState(int state) {
		this.state = state;
	}
	
	public int getGameState() {
		return state;
	}
}
