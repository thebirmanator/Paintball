package net.darkscorner.paintball.objects.menus.arenaeditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.Arena;

public class PowerupLocationArenaEditor extends ArenaEditorItem {

	public PowerupLocationArenaEditor(ItemStack item, Arena arena) {
		super(item, arena);
	}

	@Override
	public void use(Player player, Action action, Location loc) {
		switch (action) {
		case LEFT_CLICK_BLOCK:
			getArena().getPowerUpSpawnPoints().add(loc);
			player.sendMessage(Main.prefix + "Added powerup spawn location for arena " + getArena().getName());
			break;
		case RIGHT_CLICK_BLOCK:
			if(getArena().getPowerUpSpawnPoints().contains(loc)) {
				getArena().getPowerUpSpawnPoints().remove(loc);
				player.sendMessage(Main.prefix + "Removed powerup spawn location for arena " + getArena().getName());
			}
			break;
		default:
			break;
		}

	}

}
