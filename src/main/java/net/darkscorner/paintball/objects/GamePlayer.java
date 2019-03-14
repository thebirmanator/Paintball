package net.darkscorner.paintball.objects;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.darkscorner.paintball.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;

public class GamePlayer {
	// a gameplayer contains a player, their kills for that game, their deaths for that game, and shots fired for that game
	private static Set<GamePlayer> gamePlayers = new HashSet<GamePlayer>();
	private UUID uuid;
	private PaintballGame game;
	private GameStatistics gameStats;
	private GameScoreboard scoreboard;
	private Paint paintColour = Paint.getDefaultPaint();
	private Menu viewingMenu;

	private File playerFile;
	private FileConfiguration config;
	private final String shotsPath = "shots";
	private final String hitsPath = "hits";
	private final String deathsPath = "deaths";
	private final String gamesPlayedPath = "games-played";

	public GamePlayer(File file) {
		playerFile = file;
		config = YamlConfiguration.loadConfiguration(playerFile);

		String uuidString = file.getName().substring(0, file.getName().length() - 4); // string minus the .yml
		uuid = UUID.fromString(uuidString);
		gamePlayers.add(this);
	}

	public GamePlayer(Player player) {
		uuid = player.getUniqueId();
		gamePlayers.add(this);
		
		setStatsBoard(StatsBoard.LOBBY);

		playerFile = new File("plugins/DarkPaintball/playerdata/" + uuid.toString() + ".yml");
		config = YamlConfiguration.loadConfiguration(playerFile);

		// set player stats for new player
		for(int i = 0; i < PlayerStat.values().length; i++) {
			setTotal(PlayerStat.values()[i], 0);
		}
		saveProfile();
	}

	public long getTotal(PlayerStat stat) {
		switch (stat) {
			case GAMES:
				return config.getLong(gamesPlayedPath);
			case DEATHS:
				return config.getLong(deathsPath);
			case KILLS:
				return config.getLong(hitsPath);
			case SHOTS:
				return config.getLong(shotsPath);
			default:
				return -1;
		}
	}

	public void setTotal(PlayerStat stat, long amount) {
		switch (stat) {
			case GAMES:
				config.set(gamesPlayedPath, amount);
				break;
			case DEATHS:
				config.set(deathsPath, amount);
				break;
			case KILLS:
				config.set(hitsPath, amount);
				break;
			case SHOTS:
				config.set(shotsPath, amount);
				break;
			default:
				break;
		}
	}

	public void addToTotal(PlayerStat stat, long amount) {
		setTotal(stat, getTotal(stat) + amount);
	}

	/*
	public long getTotalShots() {
		return config.getLong(shotsPath);
	}

	public void setTotalShots(long shots) {
		config.set(shotsPath, shots);
	}

	public void addShots(long shots) {
		setTotalShots(getTotalShots() + shots);
	}

	public long getTotalHits() {
		return config.getLong(hitsPath);
	}

	public void setTotalHits(long hits) {
		config.set(hitsPath, hits);
	}

	public void addHits(long hits) {
		setTotalHits(getTotalHits() + hits);
	}

	public long getTotalDeaths() {
		return config.getLong(deathsPath);
	}

	public void setTotalDeaths(long deaths) {
		config.set(deathsPath, deaths);
	}

	public void addDeaths(long deaths) {
		setTotalDeaths(getTotalDeaths() + deaths);
	}

	public long getTotalGamesPlayed() {
		return config.getLong(gamesPlayedPath);
	}

	public void setTotalGamesPlayed(long gamesPlayed) {
		config.set(gamesPlayedPath, gamesPlayed);
	}

	public void addGamesPlayed(long gamesPlayed) {
		setTotalGamesPlayed(getTotalGamesPlayed() + gamesPlayed);
	}
*/
	public boolean saveProfile() {
		try {
			config.save(playerFile);
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}
	
	public boolean isInGame() {
		if(game != null) {
			return true;
		}
		return false;
	}

	public PaintballGame getCurrentGame() {
		return game;
	}
	
	public void setCurrentGame(PaintballGame game) {
		this.game = game;
	}
	
	public GameStatistics getStats() {
		return gameStats;
	}
	
	public GameScoreboard getGameScoreboard() {
		return scoreboard;
	}
	
	public void createNewStats() {
		gameStats = new GameStatistics();
	}
	
	public Paint getPaint() {
		return paintColour;
	}
	
	public void setPaint(Paint paint) {
		paintColour = paint;
	}

	public static GamePlayer getGamePlayer(Player player) {
		for(GamePlayer p : gamePlayers) {
			if(p.uuid.equals(player.getUniqueId())) {
				return p;
			}
		}
		return null;
	}

	public static List<GamePlayer> getOrderedByStat(PlayerStat stat) {
		List<GamePlayer> players = new ArrayList<>(gamePlayers);
		Comparator<GamePlayer> comparator;
		comparator = new Comparator<GamePlayer>() {
			@Override
			public int compare(GamePlayer p1, GamePlayer p2) {
				if(p1.getTotal(stat) < p2.getTotal(stat)) {
					return 1;
				} else if(p1.getTotal(stat) > p2.getTotal(stat)) {
					return -1;
				} else {
					return 0;
				}
			}
		};

		players.sort(comparator);

		return players;
	}
	public int getRanking(PlayerStat stat) {
		int ranking = -1;

		List<GamePlayer> players = getOrderedByStat(stat);

		ranking = players.indexOf(this) + 1;
		return ranking;
	}

	public static int getTotalGamePlayers() {
		return gamePlayers.size();
	}

	public Menu getViewingMenu() {
		return viewingMenu;
	}
	
	public boolean isViewingMenu() {
		if(getViewingMenu() != null) {
			return true;
		}
		return false;
	}
	
	public void setViewingMenu(Menu menu) {
		viewingMenu = menu;
	}
	
	public void setStatsBoard(StatsBoard board) {
		this.scoreboard = GameScoreboard.getBoard(board);
		Scoreboard scoreboard = this.scoreboard.generateScoreboard();
		getPlayer().setScoreboard(scoreboard);
		this.scoreboard.update(scoreboard, "%player%", getPlayer().getName());
		this.scoreboard.update(scoreboard, "%crystals%", "" + Main.crystals.getCrystals(getPlayer().getName()));
	}

	
	public void playSound(SoundEffect effect) {
		switch (effect) {
		case BACKWARD_CLICK:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, (float) 0.8);
			break;
		case DEATH:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 1, (float) 1.5);
			break;
		case FORWARD_CLICK:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, (float) 1.2);
			break;
		case HIT:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 2);
			break;
		case RUN_COMMAND:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_STEP, 1, (float) 1.8);
			break;
		case SHOOT:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EGG_THROW, 1, (float) 0.7);
			break;
		case GAME_END:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, (float) 1.6);
			break;
		case GAME_START:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, (float) 1.6);
			break;
		case POWER_UP:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, (float) 2);
			break;
		default:
			break;
		}
	}
	
	public void removePowerUps() {
		if(getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
			getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
		
		if(getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
			getPlayer().removePotionEffect(PotionEffectType.JUMP);
		}
		
		if(getPlayer().hasMetadata("volleypowerup")) {
			getPlayer().removeMetadata("volleypowerup", Main.getPlugin(Main.class));
		}
	}
}
