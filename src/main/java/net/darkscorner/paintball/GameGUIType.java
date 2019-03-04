package net.darkscorner.paintball;

public enum GameGUIType {

	GAMESLIST("Available Games"), PLAYERS("Players"), GAMEOPTIONS("Game Options");
	
	private final String gui;
	
	GameGUIType(String gui) {
		this.gui = gui;
	}
	
	public String getGUIName() {
		return gui;
	}
}
