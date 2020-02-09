package net.darkscorner.paintball;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

//import me.themgrf.arcadecoinsapi.ArcadeCoinsAPI;
import net.darkscorner.paintball.commands.*;
import net.darkscorner.paintball.listeners.*;
import net.darkscorner.paintball.objects.arena.Arena;
import net.darkscorner.paintball.objects.equippable.guns.*;
import net.darkscorner.paintball.objects.equippable.paint.Paint;
import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.darkscorner.paintball.listeners.gamelisteners.GameCreateListener;
import net.darkscorner.paintball.listeners.gamelisteners.GameEndListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerDeathListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerJoinListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerLeaveListener;
import net.darkscorner.paintball.listeners.gamelisteners.GamePlayerSpectateListener;
import net.darkscorner.paintball.listeners.gamelisteners.GameStartListener;
import net.darkscorner.paintball.listeners.gamelisteners.PowerUpUseListener;
import net.darkscorner.paintball.objects.menus.arena.EditorKit;
import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;

public class Main extends JavaPlugin {
	
	private JoinGameCommand joincmd = new JoinGameCommand(this);
	private LeaveGameCommand leavecmd = new LeaveGameCommand();
	private GamesCommand gamescmd = new GamesCommand();
	private ArenaEditCommand arenaeditcmd = new ArenaEditCommand(this);
	private PaintCommand paintcmd = new PaintCommand();
	private ViewStatsCommand viewstatscmd = new ViewStatsCommand();
	private GunCommand guncmd = new GunCommand();
	//public static ArcadeCoinsAPI coins;
	
	public static String prefix = ChatColor.GREEN + "" + ChatColor.BOLD + "PAINTBALL"+ ChatColor.DARK_GRAY + " ⎜ " + ChatColor.GRAY;
	public static GameMode defaultGamemode = GameMode.SURVIVAL;
	private static Main instance;
	
	public void onEnable() {
		// powerup ideas: super jump, shield reflector thingy
		instance = this;
		loadConfigs();
		ClickableItem.loadItems();
		
		//coins = getServer().getServicesManager().getRegistration(ArcadeCoinsAPI.class).getProvider();

		//new StandardGun(createGun(Material.GOLDEN_HOE, ChatColor.YELLOW + "Standard-issue"), GunType.STANDARD).setDefault();
		//new MachineGun(createGun(Material.IRON_HOE, ChatColor.YELLOW + "Machine gun: rapid fire"), GunType.MACHINE_GUN);
		//new ShotGun(createGun(Material.STONE_HOE, ChatColor.YELLOW + "Shotgun: cluster shots"), GunType.SHOTGUN);
		//new SniperGun(createGun(Material.DIAMOND_HOE, ChatColor.YELLOW + "Sniper: fast shots"), GunType.SNIPER);

		getCommand(joincmd.join).setExecutor(joincmd);
		getCommand(leavecmd.leave).setExecutor(leavecmd);
		getCommand(gamescmd.games).setExecutor(gamescmd);
		getCommand(arenaeditcmd.arena).setExecutor(arenaeditcmd);
		getCommand(paintcmd.paint).setExecutor(paintcmd);
		getCommand(viewstatscmd.viewstats).setExecutor(viewstatscmd);
		getCommand(guncmd.gun).setExecutor(guncmd);
		
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
		getServer().getPluginManager().registerEvents(new CoinChangeListener(), this);
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Paintball enabled!");

	}
	
	public void loadConfigs() {
		File configFolder = new File("plugins/" + this.getName());
		if(!configFolder.exists()) {
			configFolder.mkdirs();
		}
		
		String[] necessaryFiles = {"main.yml", "custompaints.yml", "scoreboards.yml", "arenas", "playerdata"};
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
				} else if(necessaryFiles[i].equals("playerdata")) {
					File[] playerFiles = necessaryFile.listFiles();
					for(int j = 0; j < playerFiles.length; j++) {
						new PlayerProfile(playerFiles[j]);
					}
				} else { // is not the arena folder
					FileConfiguration config = YamlConfiguration.loadConfiguration(necessaryFile);
					if(necessaryFiles[i].equals("main.yml")) {
						//new Game(config, this);
						//loadPowerUps(config);
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
				} else if(necessaryFiles[i].equals("playerdata")) {
					necessaryFile.mkdirs();
				} else { // create a config file
					saveResource(necessaryFiles[i], true);
					FileConfiguration config = YamlConfiguration.loadConfiguration(necessaryFile);
					if(necessaryFiles[i].equals("main.yml")) {
						//new Game(config, this);
						//loadPowerUps(config);
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
	
	/*public ArcadeCoinsAPI getArcadeCoinsAPI() {
		return coins;
	}*/
	/*
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
	}*/
	
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

	private ItemStack createGun(Material material, String gunDescription) {
		// create and give the gun
		ItemStack paintballGun = new ItemStack(material);
		ItemMeta meta = paintballGun.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "PAINTBALL GUN");

		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(gunDescription);
		lore.add(ChatColor.WHITE + "Right-click" + ChatColor.GRAY + " to shoot!");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		paintballGun.setItemMeta(meta);

		return paintballGun;

	}

	public static Main getInstance() {
		return instance;
	}
}
