package net.darkscorner.paintball.objects.player;

import net.darkscorner.paintball.objects.games.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerGameStatistics {

	private Game game;
	private Map<PlayerInGameStat, Integer> stats;
	//private int kills;
	//private int deaths;
	//private int shotsFired;
	
	private int currentKillStreak;
	
	public PlayerGameStatistics(Game game) {
		this.game = game;
		stats = new HashMap<>();
		for (PlayerInGameStat stat : PlayerInGameStat.values()) {
			stats.put(stat, 0);
		}
		currentKillStreak = 0;
	}

	//TODO: past game statistics
	public PlayerGameStatistics(UUID uuid) {

	}

	public Game getGame() {
		return game;
	}

	public int getCurrentKillStreak() {
		return currentKillStreak;
	}

	public int getStat(PlayerInGameStat stat) {
		return stats.getOrDefault(stat, 0);
	}

	public void setStat(PlayerInGameStat stat, int amount) {
		stats.replace(stat, amount);
	}

	public void addToStat(PlayerInGameStat stat, int amount) {
		amount = getStat(stat) + amount;
		stats.replace(stat, amount);
	}
	/*
	public int getKills() {
		return kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public int getNumShotsFired() {
		return shotsFired;
	}
	
	public void setKills(int kills) {
		this.kills += kills;
	}
	
	public void addKill() {
		kills++;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	public void addDeath() {
		deaths++;
	}
	
	public void setShotsFired(int shots) {
		this.shotsFired = shots;
	}
	
	public void addShot() {
		shotsFired++;
	}
	
	public int getKillStreak() {
		return killStreak;
	}
	
	public void setKillStreak(int streak) {
		killStreak = streak;
	}*/
}
