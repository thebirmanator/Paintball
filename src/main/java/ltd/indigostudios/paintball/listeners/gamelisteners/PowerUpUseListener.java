package ltd.indigostudios.paintball.listeners.gamelisteners;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.PowerUpUseEvent;
import ltd.indigostudios.paintball.objects.games.GameState;
import ltd.indigostudios.paintball.objects.powerups.PowerUp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
		//int spawnDelay = random.nextInt(powerup.getMaxSpawnDelay());
		int spawnDelay = ThreadLocalRandom.current().nextInt(21, powerup.getMaxSpawnDelay());
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
			if (event.getGame().getGameState() != GameState.ENDED) {
				powerup.spawnPowerUp(spawn);
			}

		}, spawnDelay);
	}
}
