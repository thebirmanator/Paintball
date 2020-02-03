package net.darkscorner.paintball.objects.menus;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.menus.menuitems.BackMenuItem;
import net.darkscorner.paintball.objects.menus.menuitems.GameMenuItem;

public class GameMenu extends Menu {

	private String name;
	private int size;
	//private HashMap<Integer, MenuItem> items = new HashMap<Integer, MenuItem>();

	public GameMenu(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	public GameMenu(String name, GameMenu parent, int size) {
		this.name = name;
		this.size = size;
		
		if (parent != null) {
			ItemStack backIcon = new ItemStack(Material.BIRCH_DOOR);
			ItemMeta meta = backIcon.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Go back");
			backIcon.setItemMeta(meta);
			BackMenuItem backItem = new BackMenuItem(owningItem, backIcon);
			getItems().put(size - 1, backItem);
		}
	}
	/*
	public void addButton(int slot, MenuItem item) {
		if(slot != size - 1) {
			item.setContainedIn(this);
			items.put(slot, item);
		}
	}
	
	public MenuItem getMenuItem(int slot) {
		 return items.get(slot);
	}
	
	public boolean hasMenuItem(int slot) {
		if(getMenuItem(slot) != null) {
			return true;
		}
		
		return false;
	}*/

	@Override
	public void open(Player player) {
		Inventory inv = Bukkit.createInventory(player, size, name);

		for(Integer key : getItems().keySet()) {
			GameMenuItem item = getItems().get(key);
			inv.setItem(key, item.getIcon());
		}
		
		player.openInventory(inv);
		
		GamePlayer.getGamePlayer(player).setViewingGameMenu(this);
	}
	
	public void closeMenu(Player player) {
		if(player.getOpenInventory().getType() != InventoryType.CRAFTING) { // they have an open inv
			player.closeInventory();
		}
		GamePlayer.getGamePlayer(player).setViewingGameMenu(null);
	}
	
	public Collection<GameMenuItem> getMenuItems() {
		return getItems().values();
	}
}
