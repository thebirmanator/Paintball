package net.darkscorner.paintball.listeners.gamelisteners;

import java.util.Random;

import net.darkscorner.paintball.objects.powerups.PowerUp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.objects.games.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.PowerUpUseEvent;

public class PowerUpUseListener implements Listener {

	private Main main;
	public PowerUpUseListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onUse(PowerUpUseEvent event) {
		// planning a new spawned powerup at this ones location
		Location spawn = event.getLocation();
		Random random = new Random();
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
