package net.darkscorner.paintball.objects.powerups;

import net.darkscorner.paintball.Main;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class VolleyPowerUp extends PowerUp {

    private static VolleyPowerUp instance;
    private static String meta = "volley";

    private VolleyPowerUp(String effectName) {
        super(effectName);
    }

    @Override
    public void use(Player player) {
        int duration = getDuration();
        Main main = Main.getInstance();
        if(player.hasMetadata(meta)) { // has the powerup already, add a second one to stack it
            duration = duration + player.getMetadata(meta).get(0).asInt();
            player.setMetadata(meta, new FixedMetadataValue(main, duration));
        } else {
            player.setMetadata(meta, new FixedMetadataValue(main, duration));
            new BukkitRunnable() {

                @Override
                public void run() {
                    if(player.hasMetadata(meta)) {
                        int remainingDuration = player.getMetadata(meta).get(0).asInt();
                        if(remainingDuration <= 0) {
                            player.removeMetadata(meta, main);
                            cancel();
                        } else {
                            player.setMetadata(meta, new FixedMetadataValue(main, remainingDuration - 20)); // take one second away (20 ticks)
                        }
                    } else {
                        cancel();
                    }

                }
            }.runTaskTimer(main, 0, 20);
        }
    }

    static VolleyPowerUp getInstance(String effectName) {
        if (instance == null) {
            instance = new VolleyPowerUp(effectName);
        }
        return instance;
    }

    public static boolean hasPowerUp(Player player) {
        return player.hasMetadata(meta);
    }
}
