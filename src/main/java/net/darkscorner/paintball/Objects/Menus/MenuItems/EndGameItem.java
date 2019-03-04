package net.darkscorner.paintball.Objects.Menus.MenuItems;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.Objects.GamePlayer;

public class EndGameItem extends MenuItem {

	public EndGameItem(MenuItem parent, ItemStack icon) {
		super(parent, icon);
	}

	@Override
	public void open(Player player, ClickType click) {
		getAssociatedGame().endGame();
		player.sendMessage(Main.prefix + "You have " + ChatColor.RED + "force-ended" + ChatColor.GRAY + " the game.");
		GamePlayer.getGamePlayer((Player) player).playSound(SoundEffect.FORWARD_CLICK);
		getContainedIn().closeMenu(player);

	}

}
