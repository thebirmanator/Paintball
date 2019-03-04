package net.darkscorner.paintball;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.themgrf.darkcrystals.DarkCrystals;
import net.darkscorner.paintball.commands.ArenaEditCommand;
import net.darkscorner.paintball.commands.JoinGameCommand;
import net.darkscorner.paintball.commands.LeaveGameCommand;
import net.darkscorner.paintball.commands.PaintCommand;
import net.darkscorner.paintball.commands.GamesCommand;
import net.darkscorner.paintball.listeners.BlockBreakListener;
import net.darkscorner.paintball.listeners.BlockPlaceListener;
import net.darkscorner.paintball.listeners.CrystalChangeListener;
import net.darkscorner.paintball.listeners.EntityDamageListener;
import net.darkscorner.paintball.listeners.FoodChangeListener;
import net.darkscorner.paintball.listeners.InventoryClickListener;
import net.darkscorner.paintball.listeners.InventoryCloseListener;
import net.darkscorner.paintball.listeners.JoinListener;
import net.darkscorner.paintball.listeners.PlayerChatListener;
import net.darkscorner.paintball.listeners.PlayerInteractListener;
import net.darkscorner.paintball.listeners.PlayerItemDropListener;
import net.darkscorner.paintball.listeners.PlayerLeaveListener;
import net.darkscorner.paintball.listeners.ProjectileHitListener;
import net.darkscorner.paintball.listeners.gamelisteners.GameCreateListener;
import net.darkscorner.paintball.listeners.gamelisteners.GameEndListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerDeathListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerJoinListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerLeaveListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerSpectateListener;
import net.darkscorner.paintball.listeners.gamelisteners.GameStartListener;
import net.darkscorner.paintball.listeners.gamelisteners.PowerUpUseListener;
import net.darkscorner.paintball.objects.Arena;
import net.darkscorner.paintball.objects.Paint;
import net.darkscorner.paintball.objects.PaintballGame;
import net.darkscorner.paintball.objects.PowerUp;
import net.darkscorner.paintball.objects.arenaeditors.EditorKit;
import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;

public class Main extends JavaPlugin {
	
	private JoinGameCommand joincmd = new JoinGameCommand(this);
	private LeaveGameCommand leavecmd = new LeaveGameCommand();
	private GamesCommand gamescmd = new GamesCommand();
	private ArenaEditCommand arenaeditcmd = new ArenaEditCommand(this);
	private PaintCommand paintcmd = new PaintCommand();
	public static DarkCrystals crystals;
	
	public static String prefix = ChatColor.GREEN + "" + ChatColor.BOLD + "PAINTBALL"+ ChatColor.DARK_GRAY + " ⎜ " + ChatColor.GRAY;
	public static GameMode defaultGamemode = GameMode.SURVIVAL;
	
	public void onEnable() {
		// powerup ideas: super jump, shield reflector thingy
		
		loadConfigs();
		
		crystals = getServer().getServicesManager().getRegistration(DarkCrystals.class).getProvider();
		
		getCommand(joincmd.join).setExecutor(joincmd);
		getCommand(leavecmd.leave).setExecutor(leavecmd);
		getCommand(gamescmd.games).setExecutor(gamescmd);
		getCommand(arenaeditcmd.arena).setExecutor(arenaeditcmd);
		getCommand(paintcmd.paint).setExecutor(paintcmd);
		
		getServer().getPluginManager().registerEvents(new GameCreateListener(), this);
		getServer().getPluginManager().registerEvents(new GameEndListener(this), this);
		getServer().getPluginManager().registerEvents(new GamePlayerDeathListener(this), this);
		getServer().getPluginManager().registerEvents(new GamePlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new GamePlayerLeaveListener(), this);
		getServer().getPluginManager().registerEvents(new GamePlayerSpectateListener(), this);
		getServer().getPluginManager().registerEvents(new GameStartListener(this), this);
		getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		getServer().getPluginManager().registerEvents(new ProjectileHitListener(this), this);
		getServer().getPluginManager().registerEvents(new PowerUpUseListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
		getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
		getServer().getPluginManager().registerEvents(new FoodChangeListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerItemDropListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerLeaveListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
		getServer().getPluginManager().registerEvents(new CrystalChangeListener(), this);
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Paintball enabled!");

	}
	
	public void loadConfigs() {
		File configFolder = new File("plugins/" + this.getName());
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		String[] necessaryFiles = {"main.yml", "custompaints.yml", "scoreboards.yml", "arenas"};
		List<File> configFiles = Arrays.asList(configFolder.listFiles());
		for(int i = 0; i < necessaryFiles.length; i++) { // cycle through important file names
			File necessaryFile = new File(getDataFolder(), necessaryFiles[i]);
			if(configFiles.contains(necessaryFile)) { // contains the important file
				if(necessaryFiles[i].equals("arenas")) { // is the arena folder
					File[] arenaFiles = necessaryFile.listFiles();
					for(int j = 0; j < arenaFiles.length; j++) { // cycle through arena files
						getServer().getConsoleSender().sendMessage(arenaFiles[j].getName());
						new Arena(arenaFiles[j], this);
					}
				} else { // is not the arena folder
					FileConfiguration config = YamlConfiguration.loadConfiguration(necessaryFile);
					if(necessaryFiles[i].equals("main.yml")) {
						new PaintballGame(config, this);
						loadPowerUps(config);
					} else if(necessaryFiles[i].equals("custompaints.yml")) {
						new Paint(config, this);
						loadPaints(config);
					} else if(necessaryFiles[i].equals("scoreboards.yml")) {
						GameScoreboard.createFromConfig(config);
					}
					getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Loaded file " + necessaryFiles[i]);
				}
			} else { // does not contain the important file; create it now
				if(necessaryFiles[i].equals("arenas")) { // create an arenas FOLDER and default arena config
					saveResource(necessaryFiles[i] + "/GlowyBoi.yml", true);
					new Arena(new File(getDataFolder(), necessaryFiles[i] + "/GlowyBoi.yml"), this);
				} else { // create a config file
					saveResource(necessaryFiles[i], true);
					FileConfiguration config = YamlConfiguration.loadConfiguration(necessaryFile);
					if(necessaryFiles[i].equals("main.yml")) {
						new PaintballGame(config, this);
						loadPowerUps(config);
					} else if(necessaryFiles[i].equals("custompaints.yml")) {
						new Paint(config, this);
						loadPaints(config);
					}
				}
				getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Could not find file " + necessaryFiles[i] + "; created a new file");
			}
		}
	}
	
	public void onDisable() {
		for(Arena arena : Arena.getArenas()) {
			arena.saveConfig();
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(EditorKit.hasKit(p)) {
				EditorKit.getActiveKit(p).removeKit();
			}
		}
	}
	
	public DarkCrystals getCrystalsAPI() {
		return crystals;
	}
	
	public Location configToLoc(FileConfiguration config, String configPath) {
		World world = Bukkit.getWorld(config.getString(configPath + ".world"));
		int x = config.getInt(configPath + ".x");
		int y = config.getInt(configPath + ".y");
		int z = config.getInt(configPath + ".z");
		int pitch = config.getInt(configPath + ".pitch");
		int yaw = config.getInt(configPath + ".yaw");
		Location loc = new Location(world, x, y, z, pitch, yaw);
		return loc;
	}
	
	public void locToConfig(FileConfiguration config, String configPath, Location loc) {
		String world = loc.getWorld().getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float pitch = loc.getPitch();
		float yaw = loc.getYaw();
		config.set(configPath + ".world", world);
		config.set(configPath + ".x", x);
		config.set(configPath + ".y", y);
		config.set(configPath + ".z", z);
		config.set(configPath + ".pitch", pitch);
		config.set(configPath + ".yaw", yaw);
	}
	
	private void loadPowerUps(FileConfiguration config) {
		new PowerUp(config);
		Set<String> powerupSections = config.getConfigurationSection("powerups").getKeys(false);
		for(String powerupString : powerupSections) {
			if(PowerUpEffect.valueOf(powerupString) != null) {
				PowerUpEffect effect = PowerUpEffect.valueOf(powerupString);
				String materialString = config.getString("powerups." + powerupString + ".block-type");
				if(Material.getMaterial(materialString) != null) {
					Material material = Material.getMaterial(materialString);
					int duration = config.getInt("powerups." + powerupString + ".duration") * 20; // converted into ticks
					String particleString = config.getString("powerups." + powerupString + ".particle-type");
					if(Particle.valueOf(particleString) != null) {
						Particle particle = Particle.valueOf(particleString);
						new PowerUp(material, effect, duration, particle);
					}
				} else {
					getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid powerup material: " + ChatColor.GRAY + materialString);
				}
			} else {
				getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid powerup effect: " + ChatColor.GRAY + powerupString);
			}
		}
	}
	
	private void loadPaints(FileConfiguration config) {
		Set<String> customPaintNames = config.getConfigurationSection("custom-paints").getKeys(false);
		for(String paintName : customPaintNames) {
			String displayIconString = config.getString("custom-paints." + paintName + ".display-icon");
			if(Material.getMaterial(displayIconString) != null) {
				Material displayIcon = Material.getMaterial(displayIconString);
				List<String> materialStrings = config.getStringList("custom-paints." + paintName + ".paints");
				List<Material> paintMaterials = new ArrayList<Material>();
				for(String materialString : materialStrings) {
					if(Material.getMaterial(materialString) != null) {
						Material material = Material.getMaterial(materialString);
						paintMaterials.add(material);
					} else {
						getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid paint material: " + ChatColor.GRAY + materialString);
					}
				}
				new Paint(paintName, displayIcon, paintMaterials);
			}
		}
	}
}
