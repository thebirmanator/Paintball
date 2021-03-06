package ltd.indigostudios.paintball.objects.player;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import ltd.indigostudios.paintball.objects.scoreboards.GameScoreboard;
import ltd.indigostudios.paintball.objects.scoreboards.StatsBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerGameStatistics {

    private PlayerProfile owner;
    private GameSettings game;
    private Map<PlayerInGameStat, Integer> stats;
    //private Scoreboard scoreboard;
    private int currentKillStreak;
    private boolean spectating;
	/*
	public PlayerGameStatistics(GameSettings game, PlayerProfile owner) {
		this.owner = owner;
		this.game = game;
		stats = new HashMap<>();
		for (PlayerInGameStat stat : PlayerInGameStat.values()) {
			stats.put(stat, 0);
		}
		currentKillStreak = 0;
		spectating = false;
	}*/

    public PlayerGameStatistics(GameSettings game, PlayerProfile owner, boolean spectating) {
        this.owner = owner;
        this.game = game;
        stats = new HashMap<>();
        for (PlayerInGameStat stat : PlayerInGameStat.values()) {
            stats.put(stat, 0);
        }
        currentKillStreak = 0;
        this.spectating = spectating;
    }

    //TODO: past game statistics
    public PlayerGameStatistics(UUID uuid) {

    }

    public boolean isSpectating() {
        return spectating;
    }

    public GameSettings getGame() {
        return game;
    }

    public int getCurrentKillStreak() {
        return currentKillStreak;
    }

    public void setCurrentKillStreak(int amount) {
        currentKillStreak = amount;
    }

    public void addToKillStreak(int amount) {
        setCurrentKillStreak(getCurrentKillStreak() + amount);
    }

    public int getStat(PlayerInGameStat stat) {
        return stats.getOrDefault(stat, 0);
    }

    public void setStat(PlayerInGameStat stat, int amount) {
        stats.replace(stat, amount);
        if (game instanceof TeamGame) {
            GameScoreboard.getBoard(owner, StatsBoard.TEAM_GAME).update(stat.toVariable());
        } else {
            GameScoreboard.getBoard(owner, StatsBoard.FREE_FOR_ALL_GAME).update(stat.toVariable());
        }
        //update(stat.toPlaceholder(), amount + "");
    }

    public void addToStat(PlayerInGameStat stat, int amount) {
        amount = getStat(stat) + amount;
        setStat(stat, amount);
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
