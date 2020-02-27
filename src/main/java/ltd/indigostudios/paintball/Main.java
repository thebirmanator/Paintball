package ltd.indigostudios.paintball;

//import me.themgrf.arcadecoinsapi.ArcadeCoinsAPI;
import ltd.indigostudios.paintball.commands.*;
import ltd.indigostudios.paintball.listeners.*;
import ltd.indigostudios.paintball.listeners.gamelisteners.GamePlayerLeaveListener;
import ltd.indigostudios.paintball.listeners.gamelisteners.GameStartListener;
import ltd.indigostudios.paintball.objects.equippable.paint.Paint;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.menus.arena.EditorKit;
import ltd.indigostudios.paintball.objects.scoreboards.GameScoreboard;
import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ltd.indigostudios.paintball.listeners.gamelisteners.GameCreateListener;
import ltd.indigostudios.paintball.listeners.gamelisteners.GameEndListener;
import ltd.indigostudios.paintball.listeners.gamelisteners.GamePlayerDeathListener;
import ltd.indigostudios.paintball.listeners.gamelisteners.GamePlayerJoinListener;
import ltd.indigostudios.paintball.listeners.gamelisteners.GamePlayerSpectateListener;
import ltd.indigostudios.paintball.listeners.gamelisteners.PowerUpUseListener;

public class Main extends JavaPlugin {
	
	private JoinGameCommand joincmd = new JoinGameCommand(this);
	private LeaveGameCommand leavecmd = new LeaveGameCommand();
	private GamesCommand gamescmd = new GamesCommand();
	private ArenaEditCommand arenaeditcmd = new ArenaEditCommand(this);
	private PaintCommand paintcmd = new PaintCommand();
	private ViewStatsCommand viewstatscmd = new ViewStatsCommand();
	private GunCommand guncmd = new GunCommand();
	private PaintballCmd paintballCmd = new PaintballCmd();
	//public static ArcadeCoinsAPI coins;
	
	public static String prefix = ChatColor.GREEN + "" + ChatColor.BOLD + "PAINTBALL"+ ChatColor.DARK_GRAY + " ⎜ " + ChatColor.GRAY;
	public static GameMode defaultGamemode = GameMode.SURVIVAL;
	private static Main instance;
	
	public void onEnable() {
		// powerup ideas: super jump, shield reflector thingy, bullet speed increaser
		instance = this;
		//loadConfigs();
		Arena.loadArenas();
		GameSettings.loadSettings();
		GameScoreboard.loadBoardPresets();
		Paint.loadPaints();
		ClickableItem.loadItems();
		
		//coins = getServer().getServicesManager().getRegistration(ArcadeCoinsAPI.class).getProvider();

		getCommand(joincmd.join).setExecutor(joincmd);
		getCommand(leavecmd.leave).setExecutor(leavecmd);
		getCommand(gamescmd.games).setExecutor(gamescmd);
		getCommand(arenaeditcmd.arena).setExecutor(arenaeditcmd);
		getCommand(paintcmd.paint).setExecutor(paintcmd);
		getCommand(viewstatscmd.viewstats).setExecutor(viewstatscmd);
		getCommand(guncmd.gun).setExecutor(guncmd);
		getCommand(paintballCmd.paintballcmd).setExecutor(paintballCmd);
		
		getServer().getPluginManager().registerEvents(new GameCreateListener(), this);
		getServer().getPluginManager().registerEvents(new GameEndListener(), this);
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
		getServer().getPluginManager().registerEvents(new PlayerCommandListener(), this);

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Paintball enabled!");

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

	public static Main getInstance() {
		return instance;
	}
}
