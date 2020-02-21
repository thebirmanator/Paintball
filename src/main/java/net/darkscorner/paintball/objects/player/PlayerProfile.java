package net.darkscorner.paintball.objects.player;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.darkscorner.paintball.objects.games.GameSettings;
import net.darkscorner.paintball.objects.equippable.guns.Gun;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;

public class PlayerProfile implements PlayerSettings {
	// a gameplayer contains a player, their kills for that game, their deaths for that game, and shots fired for that game
	private static Set<PlayerProfile> playerProfiles = new HashSet<>();
	private UUID uuid;
	private PlayerGameStatistics currentGameStats;

	private File playerFile;
	private FileConfiguration config;

	public PlayerProfile(File file) {
		playerFile = file;
		config = YamlConfiguration.loadConfiguration(playerFile);

		String uuidString = file.getName().substring(0, file.getName().length() - 4); // string minus the .yml
		uuid = UUID.fromString(uuidString);
		playerProfiles.add(this);
	}

	private PlayerProfile(Player player) {
		uuid = player.getUniqueId();
		playerProfiles.add(this);

		// TODO: not hardcode file path
		playerFile = new File("plugins/Paintball/playerdata/" + uuid.toString() + ".yml");
		try {
			playerFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = YamlConfiguration.loadConfiguration(playerFile);

		// set player stats for new player
		for(int i = 0; i < PlayerStat.values().length; i++) {
			setTotal(PlayerStat.values()[i], 0);
		}
		setGun(Gun.getDefault());
		saveProfile();
	}

	public long getTotal(PlayerStat stat) {
		return config.getLong(stat.getStatPath(), -1);
	}

	public void setTotal(PlayerStat stat, long amount) {
		config.set(stat.getStatPath(), amount);
	}

	public void addToTotal(PlayerStat stat, long amount) {
		setTotal(stat, getTotal(stat) + amount);
	}

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
		return currentGameStats != null;
	}

	//TODO: make gamestats account for spectating games
	public GameSettings getCurrentGame() {
		return currentGameStats.getGame();
	}
	
	public PlayerGameStatistics getCurrentGameStats() {
		return currentGameStats;
	}

	public void clearCurrentGameStats() {
		currentGameStats = null;
	}
	
	public void createNewStats(GameSettings game) {
		currentGameStats = new PlayerGameStatistics(game, this);
	}

	@Override
	public FileConfiguration getConfig() {
		return config;
	}

	public static PlayerProfile getGamePlayer(OfflinePlayer player) {
		// Check if player is already loaded in
		for (PlayerProfile p : playerProfiles) {
			if (p.uuid.equals(player.getUniqueId())) {
				return p;
			}
		}
		// Player is not loaded in, check if they have a player file
		File dataFile = new File(Main.getInstance().getDataFolder(), "/playerdata/" + player.getUniqueId().toString() + ".yml");
		if (dataFile.exists()) {
			return new PlayerProfile(dataFile);
		}
		// They do not have a loaded file, attempt to create one
		if (player.isOnline()) {
			return new PlayerProfile(player.getPlayer());
		}
		return null;
	}

	public void unload() {
		saveProfile();
		playerProfiles.remove(this);
	}

	public static List<PlayerProfile> getOrderedByStat(PlayerStat stat) {
		List<PlayerProfile> players = new ArrayList<>(playerProfiles);
		Comparator<PlayerProfile> comparator;
		comparator = (p1, p2) -> {
			if(p1.getTotal(stat) < p2.getTotal(stat)) {
				return 1;
			} else if(p1.getTotal(stat) > p2.getTotal(stat)) {
				return -1;
			} else {
				return 0;
			}
		};

		players.sort(comparator);

		return players;
	}
	public int getRanking(PlayerStat stat) {
		int ranking = -1;

		List<PlayerProfile> players = getOrderedByStat(stat);

		ranking = players.indexOf(this) + 1;
		return ranking;
	}

	public static int getTotalGamePlayers() {
		return playerProfiles.size();
	}

	/*
	public GameMenu getViewingMenu() {
		return viewingGameMenu;
	}
	
	public boolean getViewingGameMenu() {
		if(getViewingMenu() != null) {
			return true;
		}
		return false;
	}
	
	public void setViewingGameMenu(GameMenu gameMenu) {
		viewingGameMenu = gameMenu;
	}*/
	/*
	public void setStatsBoard(StatsBoard board) {
		this.scoreboard = GameScoreboard.getBoard(board);
		Scoreboard scoreboard = this.scoreboard.generateScoreboard();
		getPlayer().setScoreboard(scoreboard);
		this.scoreboard.update(scoreboard, "%player%", getPlayer().getName());
		//this.scoreboard.update(scoreboard, "%crystals%", "" + Main.coins.getCoins(getPlayer().getName()));
	}
/*
	public Gun getGun() {
		if(gun == null) {
			if(config.getString(equippedGunPath) != null) {
				//gun = Gun.getGun(GunType.valueOf(config.getString(equippedGunPath)));
			} else {
				//setGun(Gun.getGun(GunType.STANDARD));
			}
		}
		return Gun.getDefault();
	}

	public void setGun(Gun gun) {
		this.gun = gun;
		//config.set(equippedGunPath, gun.getType().toString());
		//saveProfile();
	}*/

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
	/*
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
	}*/
}
