package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChangeListener implements Listener {

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PlayerProfile.getGamePlayer(player).isInGame()) {
                event.setCancelled(true);
            }
        }
    }
}
