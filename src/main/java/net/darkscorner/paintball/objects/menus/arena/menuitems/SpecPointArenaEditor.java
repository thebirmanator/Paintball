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

public class SpecPointArenaEditor extends ArenaEditorItem implements TargetingItem {

	private Location location;
	private static ItemStack templateItem;

	public SpecPointArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		getArena().setSpectatingPoint(getLocation());
		player.sendMessage(Main.prefix + "Set spectating point for arena " + getArena().getName());
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		playerItem = templateItem;
		return this;
	}

	@Override
	public void createTemplate() {
		templateItem = new ItemEditor(Material.BEDROCK, Text.format("&6Spectate Location"))
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
