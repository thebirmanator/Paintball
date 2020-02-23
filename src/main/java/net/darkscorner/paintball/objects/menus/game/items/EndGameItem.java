package net.darkscorner.paintball.objects.menus.game.items;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.game.GameMenu;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.utils.SoundEffect;
import net.darkscorner.paintball.objects.player.PlayerProfile;

public class EndGameItem extends GameMenuItem {

	private static ItemStack templateItem;

	public EndGameItem(GameMenu parent) {
		super(parent);
	}

	@Override
	public void use(Player player, ClickType click) {
		((GameMenu) getOwningMenu()).getGame().endGame();
		player.sendMessage(Main.prefix + "You have " + ChatColor.RED + "force-ended" + ChatColor.GRAY + " the game.");
		PlayerProfile.getGamePlayer(player).playSound(SoundEffect.FORWARD_CLICK);
		getOwningMenu().close(player, true);
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		playerItem = templateItem;
		return this;
	}

	@Override
	public void createTemplate() {
		templateItem = new ItemEditor(Material.BEDROCK, Text.format("&cEnd Game"))
				.addAction(ClickType.LEFT, "to force-end this game.")
				.getItemStack();
	}

}
