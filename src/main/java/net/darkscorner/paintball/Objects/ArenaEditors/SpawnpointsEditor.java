package net.darkscorner.paintball.Objects.ArenaEditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.Objects.Arena;

public class SpawnpointsEditor extends EditorItem {

	public SpawnpointsEditor(ItemStack item, Arena arena) {
		super(item, arena);
	}

	@Override
	public void use(Player player, Action action, Location loc) {
		switch (action) {
		case LEFT_CLICK_BLOCK:
			getArena().getSpawnPoints().add(loc);
			player.sendMessage(Main.prefix + "Added spawn location for arena " + getArena().getName());
			break;
		case RIGHT_CLICK_BLOCK:
			if(getArena().getSpawnPoints().contains(loc)) {
				getArena().getSpawnPoints().remove(loc);
				player.sendMessage(Main.prefix + "Removed spawn location for arena " + getArena().getName());
			}
			break;
		default:
			break;
		}
		
	}

}
