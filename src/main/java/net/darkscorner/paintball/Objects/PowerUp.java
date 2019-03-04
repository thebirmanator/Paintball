package net.darkscorner.paintball.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.PowerUpEffect;

public class PowerUp {

	// powerup consists of a block type, and its effects
	private static List<PowerUp> powerUpList = new ArrayList<PowerUp>();
	
	private Material material;
	private PowerUpEffect effect;
	private int duration;
	private Particle particleType;
	
	private Main main;
	
	private static int maxSpawnDelay;
	
	public PowerUp(FileConfiguration config) {
		maxSpawnDelay = config.getInt("max-powerup-spawn-delay") * 20;
	}
	
	public PowerUp(Material material, PowerUpEffect effect, int duration, Particle particleType) {
		this.material = material;
		this.effect = effect;
		this.duration = duration;
		this.particleType = particleType;
		
		main = Main.getPlugin(Main.class);
		
		powerUpList.add(this);
	}
	
	public int getMaxSpawnDelay() {
		return maxSpawnDelay;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public PowerUpEffect getEffect() {
		return effect;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void spawnPowerUp(Location loc) {
		loc.getBlock().setType(material);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Location particleLoc = loc.clone();
				particleLoc.setX(particleLoc.getX() + 0.5);
				particleLoc.setZ(particleLoc.getZ() + 0.5);
				if(!loc.getBlock().getType().equals(material)) { // if no longer a powerup block, stop spawning particles
					cancel();
				}
				loc.getWorld().spawnParticle(particleType, particleLoc, 20, 0.6, 1, 0.6, 0.01);
			}
		}.runTaskTimer(main, 0, 10);
	}
	
	public static List<PowerUp> getPowerUps() {
		return powerUpList;
	}
	
	public static boolean isPowerUpBlock(Block block) {
		for(PowerUp powerup : powerUpList) {
			if(block.getType() == powerup.getMaterial()) {
				return true;
			}
		}
		return false;
	}
	
	public static PowerUp getPowerUpBlock(Block block) {
		for(PowerUp powerup : powerUpList) {
			if(block.getType() == powerup.getMaterial()) {
				return powerup;
			}
		}
		return null;
	}
}
