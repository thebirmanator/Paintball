package net.darkscorner.paintball.objects.scoreboards;

public enum StatsBoard {

	LOBBY(0), FREE_FOR_ALL_GAME(1), TEAM_GAME(2), SPECTATE(3);
	
	private final int board;
	
	StatsBoard(int board) {
		this.board = board;
	}
	
	public int getBoard() {
		return board;
	}
}
