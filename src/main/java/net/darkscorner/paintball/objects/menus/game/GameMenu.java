package net.darkscorner.paintball.objects.menus.game;

import java.util.Collection;

import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.darkscorner.paintball.objects.menus.game.items.BackMenuItem;

public class GameMenu extends Menu {

	private String name;
	private int size;
	private GameMenu parent;
	//private HashMap<Integer, MenuItem> items = new HashMap<Integer, MenuItem>();

	public GameMenu(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	public GameMenu(String name, GameMenu parent, int size) {
		this.name = name;
		this.size = size;
		this.parent = parent;
		/*
		if (parent != null) {
			ItemStack backIcon = new ItemStack(Material.BIRCH_DOOR);
			ItemMeta meta = backIcon.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Go back");
			backIcon.setItemMeta(meta);
			BackMenuItem backItem = new BackMenuItem(owningItem, backIcon);
			getItems().put(size - 1, backItem);
		}*/
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

	public Game getGame() {
		return parent.getGame();
	}

	@Override
	public void open(Player player) {
		Inventory inv = Bukkit.createInventory(player, size, name);

		for (Integer key : getItems().keySet()) {
			ClickableItem item = getItems().get(key);
			inv.setItem(key, item.getForPlayer(player).getItemStack());
		}
		
		player.openInventory(inv);
		viewing.put(player, this);
		
		//PlayerProfile.getGamePlayer(player).setViewingGameMenu(this);
	}

	@Override
	public void close(Player player) {
		viewing.remove(player);
		//PlayerProfile.getGamePlayer(player).setViewingGameMenu(null);
	}

	@Override
	public void close(Player player, boolean forceInv) {
		if (forceInv) {
			player.closeInventory();
		}
		close(player);
	}

	public Collection<ClickableItem> getClickableItems() {
		return getItems().values();
	}

	@Override
	public void showNavBar(boolean show) {
		if (parent != null && parent.getGame() != null) {
			if (show) {
				BackMenuItem backItem = new BackMenuItem(this, parent);
				getItems().put(size - 1, backItem);
			} else {
				getItems().remove(size - 1);
			}
		}
	}
}
