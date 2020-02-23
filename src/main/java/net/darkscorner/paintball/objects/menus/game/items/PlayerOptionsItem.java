package net.darkscorner.paintball.objects.menus.game.items;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.game.GameMenu;
import net.darkscorner.paintball.utils.ItemEditor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.utils.SoundEffect;
import net.darkscorner.paintball.objects.player.PlayerProfile;

public class PlayerOptionsItem extends GameMenuItem {

	private Player representing;
	private static ItemStack templateItem;
	// this is the player head item in the game options
	public PlayerOptionsItem(Player representing, GameMenu parent) {
		super(parent);
		this.representing = representing;
	}

	@Override
	public void use(Player player, ClickType click) {

		switch (click) {
			case LEFT: // kick player from their game
				if (player.hasPermission("paintball.options.players")) {
					//SkullMeta meta = (SkullMeta) getIcon().getItemMeta();
					PlayerProfile gp = PlayerProfile.getGamePlayer(representing);
					gp.getCurrentGame().removePlayer(gp);
					player.sendMessage(Main.prefix + "Kicked " + ChatColor.GREEN + gp.getPlayer().getName() + ChatColor.GRAY + " from the game.");
					PlayerProfile.getGamePlayer(player).playSound(SoundEffect.FORWARD_CLICK);
					gp.getPlayer().sendMessage(Main.prefix + "You have been " + ChatColor.RED + "kicked" + ChatColor.GRAY + " from the game.");
					getOwningMenu().close(player, true);
				} else {
					player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to kick players from a game.");
				}
				break;
			default:
				break;
		}
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		ItemStack specific = templateItem.clone();
		if (player.hasPermission("paintball.options.players")) {
			specific = new ItemEditor(specific)
					.addAction(ClickType.LEFT, "to kick player from this game.")
			.getItemStack();

		}
		SkullMeta meta = (SkullMeta) specific.getItemMeta();
		meta.setOwningPlayer(representing);
		specific.setItemMeta(meta);
		playerItem = specific;
		return this;
	}

	@Override
	public void createTemplate() {
		templateItem = new ItemEditor(Material.PLAYER_HEAD, null)
				.getItemStack();
	}

}
