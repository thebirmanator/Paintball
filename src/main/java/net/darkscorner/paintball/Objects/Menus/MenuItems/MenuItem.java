package net.darkscorner.paintball.Objects.Menus.MenuItems;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Objects.PaintballGame;
import net.darkscorner.paintball.Objects.Menus.Menu;

public abstract class MenuItem {

	private MenuItem parent;
	private ItemStack icon;
	private Menu containedIn;
	
	public MenuItem(MenuItem parent, ItemStack icon) {
		this.icon = icon;
		this.parent = parent;
	}
	
	public abstract void open(Player player, ClickType click);
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public Menu getContainedIn() {
		return containedIn;
	}
	
	public void setContainedIn(Menu menu) {
		containedIn = menu;
	}
	
	public PaintballGame getAssociatedGame() {
		MenuItem item = this;
		int index = 0;
		while(item.getParent() != null || index < 10) {
			if(item instanceof GameItem) {
				GameItem gameItem = (GameItem) item;
				return gameItem.getGame();
			}
			item = item.getParent();
			index++;
		}
		return null;
	}
	
	public MenuItem getParent() {
		return parent;
	}
}
