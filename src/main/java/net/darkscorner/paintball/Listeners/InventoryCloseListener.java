package net.darkscorner.paintball.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.darkscorner.paintball.Objects.GamePlayer;

public class InventoryCloseListener implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			if(GamePlayer.getGamePlayer(player).isViewingMenu()) {
				GamePlayer.getGamePlayer(player).setViewingMenu(null);
			}
		}
	}
}
