package net.darkscorner.paintball.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.events.GameCreateEvent;
import net.darkscorner.paintball.events.GameEndEvent;
import net.darkscorner.paintball.events.GamePlayerJoinEvent;
import net.darkscorner.paintball.events.GamePlayerLeaveEvent;
import net.darkscorner.paintball.events.GameSpectateEvent;
import net.darkscorner.paintball.events.GameStartEvent;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class PaintballGame {

	public static String invulnerableMeta = "invulnerable";

	private Main main;
	private static FileConfiguration gameConfig;
	private static int startPlayerAmount;
	private static int maxPlayerAmount;
	private static int gameTime;
	private static Location lobbySpawn;
	private static int crystalsForKill;
	private static Set<Material> blacklistedPaintMaterials = new HashSet<Material>();
	private static int paintRadius;
	private static int respawnTime;
	
	// a game can consist of
	// an arena
	// set of in game players
	// set of spectating players
	// the games current state
	
	private static Set<PaintballGame> allGames = new HashSet<PaintballGame>();
	private GameState gameState = GameState.IDLE;
	private Set<GamePlayer> inGamePlayers = new HashSet<GamePlayer>();
	private Set<GamePlayer> spectatingPlayers = new HashSet<GamePlayer>();
	private Arena arena;
	
	private static List<String> normalDeathMsgs = new ArrayList<String>();
	private static List<String> suicideDeathMsgs = new ArrayList<String>();
	
	private int currentTaskID = -1;
	
	public PaintballGame(FileConfiguration mainConfig, Main main) {
		gameConfig = mainConfig;
		this.main = main;
		
		startPlayerAmount = gameConfig.getInt("players-to-start-game");
		maxPlayerAmount = gameConfig.getInt("max-players-per-game");
		gameTime = gameConfig.getInt("game-time-in-seconds");
		lobbySpawn = main.configToLoc(gameConfig, "lobby-spawnpoint");
		crystalsForKill = gameConfig.getInt("base-crystals-on-kill");
		List<String> blacklistStrings = gameConfig.getStringList("blacklisted-blocks");
		for(String materialString : blacklistStrings) {
			if(Material.getMaterial(materialString) != null) {
				Material blackListed = Material.getMaterial(materialString);
				blacklistedPaintMaterials.add(blackListed);
			} else {
				main.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid blacklist material found: " + ChatColor.GRAY + materialString);
			}
		} 
		paintRadius = gameConfig.getInt("paint-radius");
		respawnTime = gameConfig.getInt("seconds-to-respawn");
		
		normalDeathMsgs = gameConfig.getStringList("death-messages.normal");
		for(int i = 0; i < normalDeathMsgs.size(); i++) {
			String coloured = ChatColor.translateAlternateColorCodes('&', normalDeathMsgs.get(i));
			normalDeathMsgs.set(i, coloured);
		}
		
		suicideDeathMsgs = gameConfig.getStringList("death-messages.suicide");
		for(int i = 0; i < suicideDeathMsgs.size(); i++) {
			String coloured = ChatColor.translateAlternateColorCodes('&', suicideDeathMsgs.get(i));
			suicideDeathMsgs.set(i, coloured);
		}
	}
	
	public PaintballGame(Main main, Arena arena) {
		this.main = main;
		this.arena = arena;
		allGames.add(this);
		
		this.main.getServer().getPluginManager().callEvent(new GameCreateEvent(this.main, this));
	}
	
	public int getStartPlayerAmount() {
		return startPlayerAmount;
	}
	
	public int getMaxPlayerAmount() {
		return maxPlayerAmount;
	}
	
	public int getGameTimeLength() {
		return gameTime;
	}
	
	public static Location getLobbySpawn() {
		return lobbySpawn;
	}
	
	public int getCrystalsPerKill() {
		return crystalsForKill;
	}
	
	public static Set<Material> getUnpaintableMaterials() {
		return blacklistedPaintMaterials;
	}
	
	public static int getPaintRadius() {
		return paintRadius;
	}
	
	public int getRespawnTime() {
		return respawnTime;
	}
	
	public static Set<PaintballGame> getGames() {
		return allGames;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void setGameState(GameState state) {
		gameState = state;
	}
	
	public int startWaitingForPlayers() {
		
		gameState = GameState.IDLE;
		BukkitRunnable waitingTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(gameState == GameState.IDLE && inGamePlayers.size() < startPlayerAmount) {
					for(GamePlayer p : inGamePlayers) {
						p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.GOLD + "Waiting for " + ChatColor.YELLOW + (startPlayerAmount - inGamePlayers.size()) + ChatColor.GOLD + " player(s).").create());
					}
				} else {
					cancel();
				}
				
			}
		};
		waitingTask.runTaskTimer(main, 0, 20);
		int taskID = waitingTask.getTaskId();
		return taskID;
	}
	
	public void startCountdown() {
		gameState = GameState.COUNTDOWN;
		
		currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			int countdownTime = 20;
			@Override
			public void run() {
				if(getInGamePlayers().size() >= startPlayerAmount) {
					if(countdownTime > 0) {
						for(GamePlayer p : getAllPlayers()) {
							p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.GOLD + "" + ChatColor.BOLD + countdownTime).create());
						}
						countdownTime--;
					} else if(countdownTime <= 0) {
						cancelCurrentTask();
						startGame();
					}
				} else { // not enough people to start
					cancelCurrentTask();
					startWaitingForPlayers();
				}
				
			}
		}, 0, 20);
	}
	
	public void cancelCurrentTask() {
		Bukkit.getScheduler().cancelTask(currentTaskID);
		currentTaskID = -1;
	}
	
	public void startGame() {
		
		// start the game timer
		PaintballGame game = this;
		currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			int currentTime = 0;
			@Override
			public void run() {
				int timeRemaining = gameTime - currentTime;
				currentTime++;
			
				for(GamePlayer p : getAllPlayers()) {
					p.getGameScoreboard().update(p.getPlayer().getScoreboard(), "%timeleft%", "" + formatTime(timeRemaining));
				}
				
				if(currentTime > gameTime) {
					endGame();
					cancelCurrentTask();
					for(GamePlayer p : getAllPlayers()) {
						p.getGameScoreboard().update(p.getPlayer().getScoreboard(), "%timeleft%", "" + "ENDED");
					}
				}
				
			}
		}, 0, 20);

		for(GamePlayer p : getAllPlayers()) {
			// tell everyone that the game has started
			p.getPlayer().sendTitle(ChatColor.GREEN + "Go!", "", 5, 20, 5);
			p.playSound(SoundEffect.GAME_START);
			
			p.getGameScoreboard().update(p.getPlayer().getScoreboard(), "%timeleft%", "" + 0);
		}
		
		setGameState(GameState.STARTED);
		main.getServer().getPluginManager().callEvent(new GameStartEvent(game));
		
	}
	
	public Set<GamePlayer> getInGamePlayers() {
		return inGamePlayers;
	}
	
	public Set<GamePlayer> getSpectatingPlayers() {
		return spectatingPlayers;
	}
	
	public void addPlayer(GamePlayer player, boolean setSpec) {
		if(!setSpec) {
			inGamePlayers.add(player);
			player.setCurrentGame(this);
			player.createNewStats();
			player.setStatsBoard(StatsBoard.INGAME);
			Scoreboard bukkitBoard = player.getPlayer().getScoreboard();
			player.getGameScoreboard().update(bukkitBoard, "%arena%", arena.getName());
			player.getGameScoreboard().update(bukkitBoard, "%shots%", "" + 0);
			player.getGameScoreboard().update(bukkitBoard, "%kills%", "" + 0);
			player.getGameScoreboard().update(bukkitBoard, "%deaths%", "" + 0);
			player.getGameScoreboard().update(bukkitBoard, "%timeleft%", "NOT STARTED");
			
			main.getServer().getPluginManager().callEvent(new GamePlayerJoinEvent(player, this));
		} else {
			player.setCurrentGame(this);
			setToSpectating(player);
		}
	}
	
	public void removePlayer(GamePlayer player) {
		if(inGamePlayers.contains(player)) {
			inGamePlayers.remove(player);
			player.setCurrentGame(null);
		} else {
			spectatingPlayers.remove(player);
			player.setCurrentGame(null);
		}
		
		main.getServer().getPluginManager().callEvent(new GamePlayerLeaveEvent(player, this));
	}
	
	public void setToSpectating(GamePlayer player) {
		inGamePlayers.remove(player);
		spectatingPlayers.add(player);
		player.getPlayer().setGameMode(GameMode.SPECTATOR);
		Location specPoint = arena.getSpectatingPoint();
		player.getPlayer().teleport(specPoint);
		
		player.setStatsBoard(StatsBoard.SPECTATE);
		Scoreboard bukkitBoard = player.getPlayer().getScoreboard();
		player.getGameScoreboard().update(bukkitBoard, "%arena%", arena.getName());
		player.getGameScoreboard().update(bukkitBoard, "%timeleft%", "NOT STARTED");
		main.getServer().getPluginManager().callEvent(new GameSpectateEvent(player, this));
	}

	public void makeInvulnerable(Player player, int time) {
		player.setMetadata(invulnerableMeta, new FixedMetadataValue(main, true));
		ItemStack helmet = new ItemStack(Material.GRAY_STAINED_GLASS);
		ItemMeta meta = helmet.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Paintball Helmet");
		helmet.setItemMeta(meta);
		player.getInventory().setHelmet(helmet);
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

			@Override
			public void run() {
				makeVulnerable(player);
			}
		}, time);
	}

	public void makeVulnerable(Player player) {
		if(player.hasMetadata(invulnerableMeta)) {
			player.removeMetadata(invulnerableMeta, main);
			player.getInventory().setHelmet(null);
		}
	}

	public Set<GamePlayer> getAllPlayers() {
		Set<GamePlayer> allPlayers = new HashSet<GamePlayer>();
		allPlayers.addAll(inGamePlayers);
		allPlayers.addAll(spectatingPlayers);
		return allPlayers;
	}
	
	public Arena getUsedArena() {
		return arena;
	}
	
	public void endGame() {
		gameState = GameState.ENDED;
		
		main.getServer().getPluginManager().callEvent(new GameEndEvent(this));
		
		for(GamePlayer p : getAllPlayers()) {
			p.getPlayer().sendTitle(ChatColor.GREEN + "Game Over!", "", 5, 20, 5);
			p.playSound(SoundEffect.GAME_END);
		}
		
		allGames.remove(this);
	}
	
	public static List<String> getNormalDeathMsgs() {
		return normalDeathMsgs;
	}
	
	public static List<String> getSuicideDeathMsgs() {
		return suicideDeathMsgs;
	}
	
	private String formatTime(int time) {
		int minutes = (time % 3600) / 60;
		int seconds = time % 60;
		
		String timeString = "";
		if(minutes > 0 ) {
			timeString = String.format("%02dm %02ds", minutes, seconds);
		} else {
			timeString = String.format("%02ds", seconds);
		}
		return timeString;
	}
}
