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

public class LobbyArenaEditor extends ArenaEditorItem implements TargetingItem {

	private Location location;
	private static ItemStack templateItem;

	public LobbyArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		getArena().getLobby().setSpawnLocation(getLocation());
		player.sendMessage(Main.prefix + "Set lobby location for arena " + getArena().getName());
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		playerItem = templateItem;
		return this;
	}

	@Override
	public void createTemplate() {
		templateItem = new ItemEditor(Material.REDSTONE_TORCH, Text.format("&cLobby Location"))
				.addAction(ClickType.LEFT, "to set location, or")
				.addAction(ClickType.RIGHT, "to set location.")
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
