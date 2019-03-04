package net.darkscorner.paintball.Objects.ArenaEditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Objects.Arena;

public abstract class EditorItem {

	private ItemStack item;
	private Arena arena;
	
	public static String editingMeta = "editAttr";
	
	public EditorItem(ItemStack item, Arena arena) {
		this.item = item;
		this.arena = arena;
	}
	
	public abstract void use(Player player, Action action, Location loc);
	
	public ItemStack getItem() {
		return item;
	}
	
	public Arena getArena() {
		return arena;
	}
}
