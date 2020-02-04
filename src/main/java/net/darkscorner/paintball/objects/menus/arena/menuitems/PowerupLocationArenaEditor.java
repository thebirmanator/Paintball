package net.darkscorner.paintball.objects.menus.arena.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.darkscorner.paintball.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PowerupLocationArenaEditor extends ArenaEditorItem implements TargetingItem {

	private Location location;

	public PowerupLocationArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		switch (clickType) {
			case LEFT:
				getArena().getPowerUpSpawnPoints().add(getLocation());
				player.sendMessage(Main.prefix + "Added powerup spawn location for arena " + getArena().getName());
				break;
			case RIGHT:
				if(getArena().getPowerUpSpawnPoints().contains(getLocation())) {
					getArena().getPowerUpSpawnPoints().remove(getLocation());
					player.sendMessage(Main.prefix + "Removed powerup spawn location for arena " + getArena().getName());
				}
				break;
		default:
			break;
		}

	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		return null;
	}

	@Override
	public void createItem() {
		setItemStack(new ItemEditor().buildItem(Material.BEACON, Text.format("&9Powerup Locations"))
				.addAction(ClickType.LEFT, "to add a location.")
				.addAction(ClickType.RIGHT, "to remove a location.")
				.getItemStack());
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}
}
