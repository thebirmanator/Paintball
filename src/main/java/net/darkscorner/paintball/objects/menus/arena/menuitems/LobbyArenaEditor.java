package net.darkscorner.paintball.objects.menus.arena.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.darkscorner.paintball.Main;

public class LobbyArenaEditor extends ArenaEditorItem implements TargetingItem {

	private Location location;

	public LobbyArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		getArena().setLobbyLocation(getLocation());
		player.sendMessage(Main.prefix + "Set lobby location for arena " + getArena().getName());
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		return null;
	}

	@Override
	public void createItem() {

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
