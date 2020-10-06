package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.objects.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            Menu menu = Menu.getViewing(player);
            if (menu != null) {
                menu.close(player);
            }
			/*
			if(PlayerProfile.getGamePlayer(player).getViewingGameMenu()) {
				PlayerProfile.getGamePlayer(player).setViewingGameMenu(null);
			}*/
        }
    }
}
