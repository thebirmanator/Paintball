package net.darkscorner.paintball.listeners.gamelisteners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.darkscorner.paintball.GunType;
import net.darkscorner.paintball.objects.guns.Gun;
import net.darkscorner.paintball.objects.guns.ShotGun;
import net.darkscorner.paintball.objects.guns.SniperGun;
import net.darkscorner.paintball.objects.guns.StandardGun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GameStartEvent;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.PaintballGame;
import net.darkscorner.paintball.objects.PowerUp;

public class GameStartListener implements Listener {

	private Main main;
	public GameStartListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		PaintballGame game = event.getGame();
		
		Random random = new Random();
		List<Location> availableSpawnpoints = new ArrayList<Location>();
		availableSpawnpoints.addAll(game.getUsedArena().getSpawnPoints());
		for(GamePlayer gp : game.getInGamePlayers()) {
			// set gamemode, find a spawnpoint
			gp.getPlayer().setGameMode(Main.defaultGamemode);
			
			if(availableSpawnpoints.size() == 0) { // all spawn points have been used, add them all back to be used again
				availableSpawnpoints.addAll(game.getUsedArena().getSpawnPoints());
			}
			
			// find a spawnpoint
			int spawnIndex = random.nextInt(availableSpawnpoints.size());
			Location spawn = availableSpawnpoints.get(spawnIndex).clone();
			availableSpawnpoints.remove(spawnIndex);
			spawn = spawn.add(0.5, 0, 0.5);
			gp.getPlayer().teleport(spawn);
			
			gp.getGun().giveTo(gp.getPlayer());
		}

		// spawn powerups at their spawn points
		List<Location> powerupSpawns = game.getUsedArena().getPowerUpSpawnPoints();
		for(Location spawn : powerupSpawns) {
			// choose a random powerup
			int powerupIndex = random.nextInt(PowerUp.getPowerUps().size());
			PowerUp powerup = PowerUp.getPowerUps().get(powerupIndex);
			// choose how long to wait to spawn it
			int spawnDelay = random.nextInt(powerup.getMaxSpawnDelay());
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				@Override
				public void run() {
					if(event.getGame().getGameState() != GameState.ENDED) {
						powerup.spawnPowerUp(spawn);
					}
					
				}
			}, spawnDelay);
			
		}
	}
}
