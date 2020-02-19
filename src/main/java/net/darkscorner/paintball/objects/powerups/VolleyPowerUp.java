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
    public boolean hasEffect(Player player) {
        return player.hasMetadata(meta);
    }

    public int getRemainingDuration(Player player, boolean inSeconds) {
        if (!hasEffect(player)) return 0;
        int ticks = player.getMetadata(meta).get(0).asInt();
        if (inSeconds) {
            return ticks / 20;
        } else {
            return ticks;
        }
    }

    @Override
    public void use(Player player) {
        int duration = getDuration();
        Main main = Main.getInstance();
        /*
        if (hasEffect(player)) { // has the powerup already, add a second one to stack it
            duration = duration + player.getMetadata(meta).get(0).asInt();
            player.setMetadata(meta, new FixedMetadataValue(main, duration));
        } else {
            player.setMetadata(meta, new FixedMetadataValue(main, duration));
            new BukkitRunnable() {

                @Override
                public void run() {
                    if(hasEffect(player)) {
                        int remainingDuration = player.getMetadata(meta).get(0).asInt();
                        if(remainingDuration <= 0) {
                            remove(player);
                            cancel();
                        } else {
                            player.setMetadata(meta, new FixedMetadataValue(main, remainingDuration - 20)); // take one second away (20 ticks)
                        }
                    } else {
                        cancel();
                    }

                }
            }.runTaskTimer(main, 0, 20);
        }*/
        player.setMetadata(meta, new FixedMetadataValue(main, duration));
        // Start a timer if they don't yet have the effect
        if (!hasEffect(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (hasEffect(player)) {
                        if (getRemainingDuration(player, false) <= 0) {
                            remove(player);
                            cancel();
                        } else {
                            player.setMetadata(meta, new FixedMetadataValue(main, getRemainingDuration(player, false) - 20)); // take one second away (20 ticks)
                        }
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0, 20);
        }
    }

    @Override
    public void remove(Player player) {
        player.removeMetadata(meta, Main.getInstance());
    }

    static VolleyPowerUp getInstance(String effectName) {
        if (instance == null) {
            instance = new VolleyPowerUp(effectName);
        }
        return instance;
    }
}
