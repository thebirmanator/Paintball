package net.darkscorner.paintball.objects.scoreboards;

public enum StatsBoard {

	LOBBY(0), INGAME(1), SPECTATE(2);
	
	private final int board;
	
	StatsBoard(int board) {
		this.board = board;
	}
	
	public int getBoard() {
		return board;
	}
}
