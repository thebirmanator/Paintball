package ltd.indigostudios.paintball.objects.menus.arena.items;

import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.arena.ArenaEditorMenu;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import ltd.indigostudios.paintball.Main;
import org.bukkit.inventory.ItemStack;

public class SpawnpointsArenaEditor extends ArenaEditorItem implements TargetingItem {

	private Location location;
	private static ItemStack templateItem;

	public SpawnpointsArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		switch (clickType) {
			case LEFT:
				getArena().getFreeForAllSpawnPoints().add(getLocation());
				player.sendMessage(Main.prefix + "Added spawn location for arena " + getArena().getName());
				break;
			case RIGHT:
				if(getArena().getFreeForAllSpawnPoints().contains(getLocation())) {
					getArena().getFreeForAllSpawnPoints().remove(getLocation());
					player.sendMessage(Main.prefix + "Removed spawn location for arena " + getArena().getName());
				}
				break;
		default:
			break;
		}
		
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		playerItem = templateItem;
		return this;
	}

	@Override
	public void createTemplate() {
		templateItem = new ItemEditor(Material.GRASS_BLOCK, Text.format("&aSpawnpoint Locations"))
				.addAction(ClickType.LEFT, "to add a location.")
				.addAction(ClickType.RIGHT, "to remove a location.")
				.getItemStack();
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
