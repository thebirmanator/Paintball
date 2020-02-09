package net.darkscorner.paintball.objects.powerups;

import net.darkscorner.paintball.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public interface PowerUpSettings {

    ConfigurationSection mainConfig = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "main.yml")).getConfigurationSection("powerups");

    ConfigurationSection getConfig();

    default int getMaxSpawnDelay() {
        return mainConfig.getParent().getInt("max-powerup-spawn-delay", 1) * 20;
    }

    default Material getMaterial() {
        return Material.valueOf(getConfig().getString("block-type"));
    }

    default int getDuration() {
        return getConfig().getInt("duration") * 20;
    }

    default Particle getParticle() {
        return Particle.valueOf(getConfig().getString("particle-type"));
    }

    default void spawnPowerUp(Location loc) {
        Main main = Main.getInstance();
        loc.getBlock().setType(getMaterial());

        new BukkitRunnable() {

            @Override
            public void run() {
                Location particleLoc = loc.clone();
                particleLoc.setX(particleLoc.getX() + 0.5);
                particleLoc.setZ(particleLoc.getZ() + 0.5);
                if(!loc.getBlock().getType().equals(getMaterial())) { // if no longer a powerup block, stop spawning particles
                    cancel();
                }
                loc.getWorld().spawnParticle(getParticle(), particleLoc, 20, 0.6, 1, 0.6, 0.01);
            }
        }.runTaskTimer(main, 0, 10);
    }

    void use(Player player);
}
