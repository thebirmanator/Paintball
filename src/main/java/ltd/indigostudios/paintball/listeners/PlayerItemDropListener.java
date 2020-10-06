package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerItemDropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (PlayerProfile.getGamePlayer(event.getPlayer()).isInGame()) {
            event.setCancelled(true);
        }
    }
}
