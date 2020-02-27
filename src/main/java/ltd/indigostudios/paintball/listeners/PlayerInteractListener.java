package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.PowerUpUseEvent;
import ltd.indigostudios.paintball.objects.equippable.guns.Gun;
import ltd.indigostudios.paintball.objects.games.PaintballGame;
import ltd.indigostudios.paintball.objects.menus.Menu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.powerups.PowerUp;
import ltd.indigostudios.paintball.utils.Clicks;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
				PowerUp powerUp = PowerUp.getPowerUpBlock(block);
				powerUp.use(player);
				//block.setType(Material.AIR);

				main.getServer().getPluginManager().callEvent(new PowerUpUseEvent(PowerUp.getPowerUpBlock(block), event.getClickedBlock().getLocation(), playerProfile.getCurrentGame(), playerProfile));
				powerUp.removePowerUp(block.getLocation());
				return;
			}

			// Shooting a paintball
			ItemStack handItem = event.getItem();
			if (handItem != null && Gun.isGun(handItem)) {
				if (playerProfile.isInGame()) {
					PaintballGame game = (PaintballGame) playerProfile.getCurrentGame();
					Gun gun = Gun.getGun(handItem);
					gun.shoot(player);
					// Player has shot a gun whilst invulnerable. Revoke invulnerability
					if (!game.isVulnerable(player)) {
						game.makeVulnerable(player);
						player.sendMessage(Text.format("&cYou are now vulnerable to paintball shots."));
					}
				}
			}
		}
	}
}
