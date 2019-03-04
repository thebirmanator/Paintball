package net.darkscorner.paintball.Objects.Menus.MenuItems;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackMenuItem extends MenuItem {

	public BackMenuItem(MenuItem parent, ItemStack icon) {
		super(parent, icon);
	}

	@Override
	public void open(Player player, ClickType click) {
		getParent().getContainedIn().openMenu(player);
		
	}

}
