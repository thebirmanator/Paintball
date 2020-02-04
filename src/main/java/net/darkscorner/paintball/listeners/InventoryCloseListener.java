package net.darkscorner.paintball.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.darkscorner.paintball.objects.player.PlayerProfile;

public class InventoryCloseListener implements Listener {
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			if(PlayerProfile.getGamePlayer(player).getViewingGameMenu()) {
				PlayerProfile.getGamePlayer(player).setViewingGameMenu(null);
			}
		}
	}
}
