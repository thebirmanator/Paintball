package net.darkscorner.paintball.objects.powerups;

import net.darkscorner.paintball.Main;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class VolleyPowerUp extends PowerUp {

    private static VolleyPowerUp instance;

    private VolleyPowerUp(String effectName) {
        super(effectName);
    }

    @Override
    public void use(Player player) {
        int duration = getDuration();
        Main main = Main.getInstance();
        if(player.hasMetadata("volleypowerup")) { // has the powerup already, add a second one to stack it
            duration = duration + player.getMetadata("volleypowerup").get(0).asInt();
            player.setMetadata("volleypowerup", new FixedMetadataValue(main, duration));
        } else {
            player.setMetadata("volleypowerup", new FixedMetadataValue(main, duration));
            new BukkitRunnable() {

                @Override
                public void run() {
                    if(player.hasMetadata("volleypowerup")) {
                        int remainingDuration = player.getMetadata("volleypowerup").get(0).asInt();
                        if(remainingDuration <= 0) {
                            player.removeMetadata("volleypowerup", main);
                            cancel();
                        } else {
                            player.setMetadata("volleypowerup", new FixedMetadataValue(main, remainingDuration - 20)); // take one second away (20 ticks)
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
}
