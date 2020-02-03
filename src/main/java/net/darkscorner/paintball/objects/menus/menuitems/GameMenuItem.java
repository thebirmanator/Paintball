package net.darkscorner.paintball.objects.menus.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.menus.GameMenu;

public abstract class GameMenuItem extends ClickableItem {

	private GameMenuItem parent;
	private ItemStack icon;
	private GameMenu containedIn;
	
	public GameMenuItem(GameMenuItem parent, ItemStack icon) {
		this.icon = icon;
		this.parent = parent;
	}
	
	public abstract void use(Player player, ClickType click);
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public GameMenu getContainedIn() {
		return containedIn;
	}
	
	public void setContainedIn(GameMenu gameMenu) {
		containedIn = gameMenu;
	}
	
	public Game getAssociatedGame() {
		GameMenuItem item = this;
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
	
	public GameMenuItem getParent() {
		return parent;
	}
}
