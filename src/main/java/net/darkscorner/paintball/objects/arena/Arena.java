package net.darkscorner.paintball.objects.arena;

import java.io.File;
import java.io.IOException;
import java.util.*;

import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.darkscorner.paintball.Main;
import org.bukkit.entity.Player;

public class Arena implements ArenaSetting {

	// an arena must have a bounded area determined by two locations, at least one spawn point, and a spectating spawn point, and a name
	// it may have multiple spawn points and powerup spawn points, but they are not crucial
	
	private static List<Arena> arenas = new ArrayList<Arena>();
	
	private File file;
	private FileConfiguration config;
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
	
	public Arena(File file, Main main) {
		this.file = file;
		config = YamlConfiguration.loadConfiguration(file);

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
		//Main main = Main.getPlugin(Main.class);
/*
		// powerup spawnpoints
		String powerupBase = "powerup-spawnpoints";
		config.set(powerupBase, null);
		for(int i = 0; i < powerUpLocations.size(); i++) {
			main.locToConfig(config, powerupBase + "." + (i + 1), powerUpLocations.get(i));
		}
		
		// set displayname
		config.set("display-name", name.replace('�', '&'));
		
		// game spawnpoints
		String spawnpointsBase = "game-spawnpoints";
		config.set(spawnpointsBase, null);
		for(int i = 0; i < spawnPoints.size(); i++) {
			main.locToConfig(config, spawnpointsBase + "." + (i + 1), spawnPoints.get(i));
		}
		
		// spectating spawnpoint
		main.locToConfig(config, "spectating-spawnpoint", specPoint);
		
		// lobby
		main.locToConfig(config, "waiting-lobby", preGameLobbyLoc);
		
		// gui item
		config.set("item-for-guis", material.toString());
		
		// creator
		config.set("creator", creator.replace('�', '&'));
		*/
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
		for (Game game : Game.allGames) {
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
	/*
	public Material getMaterial() {
		return material;
	}
	
	public Location getLobbyLocation() {
		return preGameLobbyLoc;
	}
	
	public void setLobbyLocation(Location loc) {
		preGameLobbyLoc = loc;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setIsInUse(boolean isInUse) {
		this.isInUse = isInUse;
	}
	public List<Location> getPowerUpSpawnPoints() {
		return powerUpLocations;
	}
	
	public void giveEditKit(Player player) {
		//EditorKit kit = new EditorKit(player, editHotbar);
		//kit.giveKit();
		
	}
	
	public void removeEditKit(Player player) {
		if(EditorKit.hasKit(player)) {
			EditorKit.getActiveKit(player).removeKit();
		}
	}

	private ItemStack getItem(Material material, String displayName, String leftClick, String rightClick) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "Left-click " + ChatColor.GRAY + leftClick);
		lore.add(ChatColor.WHITE + "Right-click " + ChatColor.GRAY + rightClick);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
		
	}
*/
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

	//TODO: code for checking if in the boundaries
	public boolean isInArena(Player player) {
		Location location = player.getLocation();
		return location.toVector().isInAABB(getBoundaries()[0].toVector(), getBoundaries()[1].toVector());
	}
}
