package ltd.indigostudios.paintball.objects.powerups;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
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
        // Start a timer if they don't yet have the effect
        if (!hasEffect(player)) {
            PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (hasEffect(player) || playerProfile.isInGame()) {
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
        // Set metadata after the scheduler, otherwise hasEffect will always be true
        player.setMetadata(meta, new FixedMetadataValue(main, duration));
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
