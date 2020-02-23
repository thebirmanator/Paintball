package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Inventory clickedInv = event.getClickedInventory();
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
			// Can't interact with inventories whilst in game
			if (playerProfile.isInGame()) {
				event.setCancelled(true);
			}
			if (clickedInv != null) {
				Menu menu = Menu.getViewing(player);
				if (menu != null) {
					ClickableItem clickableItem = menu.getClickableItem(event.getSlot());
					if (clickableItem != null) {
						// Is not the player's inventory; slots in player inv also have slots with the same numbers
						if (!event.getClickedInventory().equals(player.getOpenInventory().getBottomInventory())) {
							clickableItem.use(player, event.getClick());
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
