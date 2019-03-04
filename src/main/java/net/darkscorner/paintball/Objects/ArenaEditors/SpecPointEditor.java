package net.darkscorner.paintball.Objects.ArenaEditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.Objects.Arena;

public class SpecPointEditor extends EditorItem {

	public SpecPointEditor(ItemStack item, Arena arena) {
		super(item, arena);
	}

	@Override
	public void use(Player player, Action action, Location loc) {
		getArena().setSpectatingPoint(loc);
		player.sendMessage(Main.prefix + "Set spectating point for arena " + getArena().getName());
	}

}
