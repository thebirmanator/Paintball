package net.darkscorner.paintball.objects.menus.arenaeditors;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.objects.Arena;

public abstract class ArenaEditorItem extends ClickableItem {

	private ItemStack item;
	private Arena arena;
	
	public static String editingMeta = "editAttr";
	
	public ArenaEditorItem(ItemStack item, Arena arena) {
		this.item = item;
		this.arena = arena;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public Arena getArena() {
		return arena;
	}
}
