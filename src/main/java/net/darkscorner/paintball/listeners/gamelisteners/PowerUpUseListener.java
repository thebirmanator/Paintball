package net.darkscorner.paintball.listeners.gamelisteners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.PowerUpEffect;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.events.PowerUpUseEvent;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.PowerUp;

public class PowerUpUseListener implements Listener {

	private Main main;
	public PowerUpUseListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onUse(PowerUpUseEvent event) {
		GamePlayer gp = event.getPlayer();
		gp.playSound(SoundEffect.POWER_UP);
		PowerUpEffect effect = event.getPowerUp().getEffect();
		int duration = event.getPowerUp().getDuration();
		
		Player player = gp.getPlayer();
		switch (effect) {
		case JUMP:
			if(player.hasPotionEffect(PotionEffectType.JUMP)) {
				 duration = duration + player.getPotionEffect(PotionEffectType.JUMP).getDuration();
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 2), true);
			break;
		case SPEED:
			if(player.hasPotionEffect(PotionEffectType.SPEED)) {
				 duration = duration + player.getPotionEffect(PotionEffectType.SPEED).getDuration();
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 2), true);
			break;
		case VOLLEY:
			if(player.hasMetadata("volleypowerup")) { // has the powerup already, add a second one to stack it
				duration = duration + player.getMetadata("volleypowerup").get(0).asInt();
				player.setMetadata("volleypowerup", new FixedMetadataValue(main, duration));
			} else {
				player.setMetadata("volleypowerup", new FixedMetadataValue(main, duration));
				new BukkitRunnable() {
					
					@Override
					public void run() {
						int remainingDuration = player.getMetadata("volleypowerup").get(0).asInt();
						if(remainingDuration <= 0) {
							player.removeMetadata("volleypowerup", main);
							cancel();
						} else {
							player.setMetadata("volleypowerup", new FixedMetadataValue(main, remainingDuration - 20)); // take one second away (20 ticks)
						}
						
					}
				}.runTaskTimer(main, 0, 20);
			}
			break;
		default:
			break;
		}
		
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