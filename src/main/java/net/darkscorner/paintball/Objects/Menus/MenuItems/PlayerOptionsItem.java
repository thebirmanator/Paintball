package net.darkscorner.paintball.objects.menus.menuitems;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.GamePlayer;

public class PlayerOptionsItem extends MenuItem {

	// this is the player head item in the game options
	public PlayerOptionsItem(MenuItem parent, ItemStack icon) {
		super(parent, icon);
	}

	@Override
	public void open(Player player, ClickType click) {
		switch (click) {
		case LEFT: // kick player from their game
			SkullMeta meta =  (SkullMeta) getIcon().getItemMeta();
			GamePlayer gp = GamePlayer.getGamePlayer(meta.getOwningPlayer().getPlayer());
			gp.getCurrentGame().removePlayer(gp);
			player.sendMessage(Main.prefix + "Kicked " + ChatColor.GREEN + gp.getPlayer().getName() + ChatColor.GRAY + " from the game.");
			GamePlayer.getGamePlayer((Player) player).playSound(SoundEffect.FORWARD_CLICK);
			gp.getPlayer().sendMessage(Main.prefix + "You have been " + ChatColor.RED + "kicked" + ChatColor.GRAY + " from the game.");
			getContainedIn().closeMenu(player);
			break;
		default:
			break;
		}
		
	}

}
