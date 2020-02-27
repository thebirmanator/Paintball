package ltd.indigostudios.paintball.objects.menus.game;

import java.util.Collection;

import ltd.indigostudios.paintball.objects.menus.ChestBasedMenu;
import ltd.indigostudios.paintball.objects.menus.game.items.BackMenuItem;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;

public class GameMenu extends ChestBasedMenu {

	private GameSettings game;

	public GameMenu(String name, int size) {
		super(name, size);
	}

	public GameMenu(String name, GameMenu parent, int size, GameSettings game) {
		super(name, parent, size);
		this.game = game;
	}
	
	public GameMenu(String name, GameMenu parent, int size) {
		super(name, parent, size);
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

	public GameSettings getGame() {
		if (game != null) {
			return game;
		} else {
			GameMenu parent = (GameMenu) getParent();
			while (parent != null) {
				if (parent.getGame() != null) {
					return parent.getGame();
				}
				parent = (GameMenu) parent.getParent();
			}
		}
		return null;
	}
/*
	@Override
	public void open(Player player) {
		Inventory inv = Bukkit.createInventory(player, getSize(), getName());

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
	}*/

	public Collection<ClickableItem> getClickableItems() {
		return getItems().values();
	}

	@Override
	public void showNavBar(boolean show) {
		if (getParent() != null /*&& ((GameMenu) getParent()).getGame() != null*/) {
			if (show) {
				BackMenuItem backItem = new BackMenuItem(this, (GameMenu) getParent());
				getItems().put(getSize() - 1, backItem);
			} else {
				getItems().remove(getSize() - 1);
			}
		}
	}
}
