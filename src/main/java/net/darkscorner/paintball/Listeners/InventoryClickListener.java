package net.darkscorner.paintball.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.darkscorner.paintball.Commands.ArenaEditCommand;
import net.darkscorner.paintball.Objects.GamePlayer;
import net.darkscorner.paintball.Objects.Menus.Menu;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Inventory clickedInv = event.getClickedInventory();
		if(event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if(clickedInv != null) {
				if(event.getCurrentItem().getType() != Material.AIR) {
					event.setCancelled(true);
					if(GamePlayer.getGamePlayer(player).isViewingMenu()) {
						Menu menu = GamePlayer.getGamePlayer(player).getViewingMenu();
						if(menu.hasMenuItem(event.getSlot())) {
							menu.getMenuItem(event.getSlot()).open(player, event.getClick());
						}
					} else {
						if(player.hasMetadata(ArenaEditCommand.editMeta)) { // if not viewing a menu and is editing arenas
							String arenaName = player.getMetadata(ArenaEditCommand.editMeta).get(0).asString();
							if(!arenaName.isEmpty()) { // if not general editing (block editing), cancel
								event.setCancelled(true);
							} else { // block editing, allow inventory manipulating
								event.setCancelled(false);
							}
						}
					}
				}
			}
		}
	}
}
