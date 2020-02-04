package net.darkscorner.paintball.objects.menus.game.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.game.GameMenu;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.player.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class PlayerOptionsItem extends GameMenuItem {

	private Player representing;
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
					getOwningMenu().close(player);
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
		ItemStack template = getItemStack().clone();
		if (player.hasPermission("paintball.options.players")) {
			ItemMeta meta = template.getItemMeta();
			List<String> lore = new ArrayList<>();
			lore.add(Text.format("&fLeft-click &7to kick player from this game."));
			meta.setLore(lore);
			return new PlayerOptionsItem(representing, getOwningMenu());
		}
		return this;
	}

	@Override
	public void createItem() {
		ItemStack icon = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) icon.getItemMeta();
		meta.setOwningPlayer(representing);
		icon.setItemMeta(meta);
		setItemStack(icon);
	}

}
