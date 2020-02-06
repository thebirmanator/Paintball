package net.darkscorner.paintball.objects.menus.game.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.game.GameMenu;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BackMenuItem extends GameMenuItem {

	private GameMenu backMenu;
	private static ItemStack templateItem;

	public BackMenuItem(GameMenu parent, GameMenu backMenu) {
		super(parent);
		this.backMenu = backMenu;
	}

	@Override
	public void use(Player player, ClickType click) {
		backMenu.open(player);
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		playerItem = templateItem;
		return this;
	}

	@Override
	public void createTemplate() {
		templateItem = new ItemEditor().buildItem(Material.BIRCH_DOOR, Text.format("&cGo Back")).getItemStack();
	}
}
