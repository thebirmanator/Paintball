package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.powerups.PowerUp;
import net.darkscorner.paintball.utils.Clicks;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.PowerUpUseEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;

public class PlayerInteractListener implements Listener {

	private Main main;
	public PlayerInteractListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Menu menu = Menu.getViewing(player);
		// Using an inventory editor
		if (event.getAction() != Action.PHYSICAL && menu != null) {
			int slot = player.getInventory().getHeldItemSlot();
			if (menu.hasClickableItem(slot)) {
				menu.getClickableItem(slot).use(player, Clicks.asClickType(event.getAction()));
				return;
			}
		}

		// Spectators can't pick up PowerUps or shoot paintballs
		if (player.getGameMode() != GameMode.SPECTATOR) {
			// Clicked a PowerUp
			Block block = event.getClickedBlock();
			PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
			if (block != null && PowerUp.isPowerUpBlock(block)) {
				PowerUp.getPowerUpBlock(block).use(player);
				block.setType(Material.AIR);
				main.getServer().getPluginManager().callEvent(new PowerUpUseEvent(PowerUp.getPowerUpBlock(block), event.getClickedBlock().getLocation(), playerProfile.getCurrentGame(), playerProfile));
				return;
			}

			// Shooting a paintball
			ItemStack handItem = event.getItem();
			if (handItem != null && Gun.isGun(handItem)) {
				Gun gun = Gun.getGun(handItem);
				gun.shoot(player);
			}
		}
	}
}
