package net.darkscorner.paintball.objects.arena;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.darkscorner.paintball.objects.games.GameSettings;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.darkscorner.paintball.Main;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Arena implements ArenaSetting {

	// an arena must have a bounded area determined by two locations, at least one spawn point, and a spectating spawn point, and a name
	// it may have multiple spawn points and powerup spawn points, but they are not crucial
	
	private static List<Arena> arenas = new ArrayList<Arena>();
	
	private File file;
	private FileConfiguration config;
	private Vector[] minMaxVectors;
	private ArenaLobby arenaLobby;
	//private boolean isInUse;
	/*
	private List<Location> powerUpLocations = new ArrayList<Location>();
	private String name;
	private List<Location> spawnPoints = new ArrayList<Location>();
	private Location specPoint;
	private Location preGameLobbyLoc;
	private Material material;
	private String creator;
	private boolean teamsEnabled;
	private Location[] boundaryMarkers;
	/*
	private ItemStack powerupEditor = getItem(Material.BEACON, ChatColor.BLUE + "Powerup Locations", "to add location.", "to remove location.");
	private ItemStack spawnpointsEditor = getItem(Material.GRASS_BLOCK, ChatColor.GREEN + "Spawnpoint Locations", "to add location.", "to remove location.");
	
	private ItemStack specPointEditor = getItem(Material.BEDROCK, ChatColor.GOLD + "Spectate Location", "to set location, or", "to set location.");
	private ItemStack lobbyEditor = getItem(Material.REDSTONE_TORCH, ChatColor.RED + "Lobby Location", "to set location, or", "to set location.");
	
	private ItemStack nameEditor = getItem(Material.NAME_TAG, ChatColor.LIGHT_PURPLE + "Arena Name", "to change the name, or", "to change it.");
	private ItemStack creatorEditor = getItem(Material.BOOK, ChatColor.AQUA + "Creator Name", "to change creator name, or", "to change it.");
	private ItemStack doneItem = getItem(Material.RABBIT_FOOT, ChatColor.YELLOW + "Done", "to exit edit mode, or", "to exit.");

	/*
	private ArenaEditorItem[] editHotbar = {
			new PowerupLocationArenaEditor(powerupEditor, this),
			new SpawnpointsArenaEditor(spawnpointsEditor, this),
			null,
			new SpecPointArenaEditor(specPointEditor, this),
			new LobbyArenaEditor(lobbyEditor, this),
			null,
			new NameArenaEditor(nameEditor, this),
			new CreatorArenaEditor(creatorEditor, this),
			new DoneArenaEditor(doneItem, this)
	};*/
	
	private Arena(File file) {
		this.file = file;
		config = YamlConfiguration.loadConfiguration(file);
		arenaLobby = new ArenaLobby(this, config.getConfigurationSection("waiting-lobby"));

		//Set<String> powerUpSections = config.getConfigurationSection("powerup-spawnpoints").getKeys(false);
		//for(String section : powerUpSections) {
		//	Location loc = main.configToLoc(config, "powerup-spawnpoints." + section);
		//	powerUpLocations.add(loc);
		//}
		//name = ChatColor.translateAlternateColorCodes('&', config.getString("display-name"));
		//Set<String> spawnSections = config.getConfigurationSection("game-spawnpoints").getKeys(false);
		//for(String section : spawnSections) {
		//	Location loc = main.configToLoc(config, "game-spawnpoints." + section);
		//	spawnPoints.add(loc);
		//}
		//specPoint = main.configToLoc(config, "spectating-spawnpoint");
		//isInUse = false;
		//preGameLobbyLoc = main.configToLoc(config, "waiting-lobby");

		//if(Material.getMaterial(config.getString("item-for-guis")) != null) {
		//	material = Material.getMaterial(config.getString("item-for-guis"));
		//} else {
		//	material = Material.STONE;
		//}

		//creator = config.getString("creator");
		
		arenas.add(this);

	}
	
	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Arena(String fileName) {
		fileName.replace(" ", "");
		file = new File("plugins/DarkPaintball/arenas/", fileName + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
		saveConfig();
	}

	public Arena(String name, List<Location> spawnPoints, Location specPoint, List<Location> powerUpLocations, Location preGameLobbyLoc, Material material, String creator) {
		/*this.name = name;
		this.spawnPoints = spawnPoints;
		this.specPoint = specPoint;
		this.powerUpLocations = powerUpLocations;
		this.preGameLobbyLoc = preGameLobbyLoc;
		this.material =  material;
		this.creator = creator;*/
		//isInUse = false;

		String fileName = ChatColor.stripColor(name);
		fileName.replace(" ", "");
		file = new File("plugins/DarkPaintball/arenas/", fileName + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
		saveConfig();
		
	}
	
	// GETTERS
	/*
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSimpleName() {
		return ChatColor.stripColor(name);
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public List<Location> getSpawnPoints() {
		return spawnPoints;
	}
	
	public Location getSpectatingPoint() {
		return specPoint;
	}
	
	public void setSpectatingPoint(Location loc) {
		specPoint = loc;
	}
	*/
	public boolean isInUse() {
		for (GameSettings game : GameSettings.allGames) {
			if (game.getArena().equals(this)) {
				return true;
			}
		}
		return false;
	}

	public boolean isEditing(Player player) {
		Menu menu = Menu.getViewing(player);
		return menu instanceof ArenaEditorMenu;
	}

	public static Arena getEditing(Player player) {
		for (Arena arena : getArenas()) {
			if (arena.isEditing(player)) {
				return arena;
			}
		}
		return null;
	}

	public void remove() {
		arenas.remove(this);
		file.delete();
	}

	public static List<Arena> getArenas() {
		return arenas;
	}
	
	public static Arena getArena(String name) {
		for(Arena arena : arenas) {
			if(arena.getName().equals(name)) {
				return arena;
			}
		}
		return null;
	}
	/*
	public static void addArena(Arena arena) {
		arenas.add(arena);
	}*/

	@Override
	public FileConfiguration getConfig() {
		return config;
	}

	@Override
	public ArenaLobby getLobby() {
		return arenaLobby;
	}

	public boolean isInArena(Player player) {
		Location location = player.getLocation();
		Vector minVector = new Vector(0, 0, 0);
		Vector maxVector = new Vector(0, 0, 0);
		if (minMaxVectors == null) {
			minMaxVectors = new Vector[2];
			Location bound0 = getBoundaries()[0];
			Location bound1 = getBoundaries()[1];
			// x
			//TODO: clean this up?
			if (bound0.getBlockX() < bound1.getBlockX()) {
				minVector.setX(bound0.getBlockX());
				maxVector.setX(bound1.getBlockX());
			} else {
				minVector.setX(bound1.getBlockX());
				maxVector.setX(bound0.getBlockX());
			}
// y
			if (bound0.getBlockY() < bound1.getBlockY()) {
				minVector.setY(bound0.getBlockY());
				maxVector.setY(bound1.getBlockY());
			} else {
				minVector.setY(bound1.getBlockY());
				maxVector.setY(bound0.getBlockY());
			}
// z
			if (bound0.getBlockZ() < bound1.getBlockZ()) {
				minVector.setZ(bound0.getBlockZ());
				maxVector.setZ(bound1.getBlockZ());
			} else {
				minVector.setZ(bound1.getBlockZ());
				maxVector.setZ(bound0.getBlockZ());
			}
			minMaxVectors[0] = minVector;
			minMaxVectors[1] = maxVector;
		}
		return location.toVector().isInAABB(minMaxVectors[0], minMaxVectors[1]);
	}

	public static void loadArenas() {
		Main main = Main.getInstance();
		File arenasFolder = new File(main.getDataFolder(), "arenas");
		if (arenasFolder.exists()) {
			File[] arenaFiles = arenasFolder.listFiles();
			for (File arenaFile : arenaFiles) {
				// Valid YML file
				if (arenaFile.getName().endsWith(".yml")) {
					Arena arena = new Arena(arenaFile);
					main.getServer().getConsoleSender().sendMessage(Text.format("&aLoaded arena " + arena.getName()));
				}
			}
			// No arenas were created based off of files. Warn console!
			if (arenas.size() < 1) {
				main.getServer().getConsoleSender().sendMessage(Text.format("&cNo arenas found! Games will not start with no valid arenas."));
			}
		} else {
			main.saveResource(arenasFolder + "/GlowyBoi.yml", true);
			main.getServer().getConsoleSender().sendMessage(Text.format("&eNo arenas folder found! Generated a new one with a default arena."));
		}
	}
}
