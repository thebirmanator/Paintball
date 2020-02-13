package net.darkscorner.paintball.objects.games;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.darkscorner.paintball.objects.arena.Arena;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.Locations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import net.darkscorner.paintball.Main;
import net.md_5.bungee.api.ChatColor;

public interface Game {

	String invulnerableMeta = "invulnerable";
	Set<Game> allGames = new HashSet<>();
/*
	private Main main;
	private static FileConfiguration gameConfig;
	private static int startPlayerAmount;
	private static int maxPlayerAmount;
	private static int gameTime;
	private static Location lobbySpawn;
	private static int coinsForKill;
	private static Set<Material> blacklistedPaintMaterials = new HashSet<Material>();
	private static int paintRadius;
	private static int respawnTime;
	*/
	// a game can consist of
	// an arena
	// set of in game players
	// set of spectating players
	// the games current state
	/*
	private static Set<Game> allGames = new HashSet<Game>();
	private GameState gameState = GameState.IDLE;
	private Set<GamePlayer> inGamePlayers = new HashSet<GamePlayer>();
	private Set<GamePlayer> spectatingPlayers = new HashSet<GamePlayer>();
	private Arena arena;
	*/
	//private static List<String> normalDeathMsgs = new ArrayList<String>();
	//private static List<String> suicideDeathMsgs = new ArrayList<String>();
	
	//private int currentTaskID = -1;
	
	//public Game(FileConfiguration mainConfig, Main main) {
		//gameConfig = mainConfig;
		//this.main = main;

	//startPlayerAmount = gameConfig.getInt("players-to-start-game");
	//maxPlayerAmount = gameConfig.getInt("max-players-per-game");
	//gameTime = gameConfig.getInt("game-time-in-seconds");
	//lobbySpawn = main.configToLoc(gameConfig, "lobby-spawnpoint");
	//coinsForKill = gameConfig.getInt("base-crystals-on-kill");
		/*
		List<String> blacklistStrings = gameConfig.getStringList("blacklisted-blocks");
		for(String materialString : blacklistStrings) {
			if(Material.getMaterial(materialString) != null) {
				Material blackListed = Material.getMaterial(materialString);
				blacklistedPaintMaterials.add(blackListed);
			} else {
				main.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid blacklist material found: " + ChatColor.GRAY + materialString);
			}
		} */
	//paintRadius = gameConfig.getInt("paint-radius");
	//respawnTime = gameConfig.getInt("seconds-to-respawn");
		/*
		normalDeathMsgs = gameConfig.getStringList("death-messages.normal");
		for(int i = 0; i < normalDeathMsgs.size(); i++) {
			String coloured = ChatColor.translateAlternateColorCodes('&', normalDeathMsgs.get(i));
			normalDeathMsgs.set(i, coloured);
		}
		
		suicideDeathMsgs = gameConfig.getStringList("death-messages.suicide");
		for(int i = 0; i < suicideDeathMsgs.size(); i++) {
			String coloured = ChatColor.translateAlternateColorCodes('&', suicideDeathMsgs.get(i));
			suicideDeathMsgs.set(i, coloured);
		}*/
	//}
	/*
	public Game(Main main, Arena arena) {
		this.main = main;
		this.arena = arena;
		allGames.add(this);
		
		this.main.getServer().getPluginManager().callEvent(new GameCreateEvent(this.main, this));
	}*/
	FileConfiguration getGameConfig();

	default int getStartPlayerAmount() {
		return getGameConfig().getInt("players-to-start-game");
	}
	
	default int getMaxPlayerAmount() {
		return getGameConfig().getInt("max-players-per-game");
	}
	
	default int getGameTimeLength() {
		return getGameConfig().getInt("game-time-in-seconds");
	}
	
	default Location getLobbySpawn() {
		return Locations.stringToLoc(getGameConfig().getString("lobby-spawnpoint"));
	}
	
	default int getCoinsPerKill() {
		return getGameConfig().getInt("base-crystals-on-kill");
	}

	//TODO: make this not load everytime the method runs
	default Set<Material> getUnpaintableMaterials() {
		List<String> blacklistStrings = getGameConfig().getStringList("blacklisted-blocks");
		Set<Material> blacklistedMaterials = new HashSet<>();
		for (String materialString : blacklistStrings) {
			if (Material.getMaterial(materialString, false) != null) {
				Material blackListed = Material.getMaterial(materialString, false);
				blacklistedMaterials.add(blackListed);
			} else {
				Main.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid blacklist material found: " + ChatColor.GRAY + materialString);
			}
		}
		return blacklistedMaterials;
	}
	
	default int getPaintRadius() {
		return getGameConfig().getInt("paint-radius");
	}
	
	default int getRespawnTime() {
		return getGameConfig().getInt("seconds-to-respawn");
	}

	GameState getGameState();

	void setGameState(GameState state);

	void waitForPlayers(boolean start);

	void countdown(boolean start);

	void startGame();

	Set<PlayerProfile> getPlayers(boolean isPlaying);

	void addPlayer(PlayerProfile player, boolean setSpec);

	void removePlayer(PlayerProfile player);

	void setToSpectating(PlayerProfile player);

	Set<PlayerProfile> getAllPlayers();

	void endGame();

	Arena getArena();

	/*
	public static Set<Game> getGames() {
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
		Game game = this;
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
	}*/
	//TODO: fancy forEach or something?
	default List<String> getNormalDeathMsgs() {
		List<String> msgs = getGameConfig().getStringList("death-messages.normal");
		for (int i = 0; i < msgs.size(); i++) {
			String coloured = ChatColor.translateAlternateColorCodes('&', msgs.get(i));
			msgs.set(i, coloured);
		}
		return msgs;
	}
	
	default List<String> getSuicideDeathMsgs() {
		List<String> msgs = getGameConfig().getStringList("death-messages.suicide");
		for (int i = 0; i < msgs.size(); i++) {
			//TODO: make colouriser into util
			String coloured = ChatColor.translateAlternateColorCodes('&', msgs.get(i));
			msgs.set(i, coloured);
		}
		return msgs;
	}
	//TODO: move to a utils
	default String formatTime(int time) {
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

	static Game getSettings() {
		return new FreeForAllGame();
	}
}
