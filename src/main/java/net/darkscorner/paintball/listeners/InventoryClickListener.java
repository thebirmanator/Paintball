package net.darkscorner.paintball.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.darkscorner.paintball.commands.ArenaEditCommand;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.menus.game.GameMenu;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Inventory clickedInv = event.getClickedInventory();
		if(event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if(clickedInv != null) {
				if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
					event.setCancelled(true);
					if(PlayerProfile.getGamePlayer(player).getViewingGameMenu()) {
						GameMenu gameMenu = PlayerProfile.getGamePlayer(player).getViewingMenu();
						if(gameMenu.hasMenuItem(event.getSlot())) {
							gameMenu.getMenuItem(event.getSlot()).open(player, event.getClick());
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
