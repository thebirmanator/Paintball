package net.darkscorner.paintball.objects;

public class GameStatistics {

	private int kills;
	private int deaths;
	private int shotsFired;
	
	private int killStreak;
	
	public GameStatistics() {
		kills = 0;
		deaths = 0;
		shotsFired = 0;
		killStreak = 0;

	}
	
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
	}
}
