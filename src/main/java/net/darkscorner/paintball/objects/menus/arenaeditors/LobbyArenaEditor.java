package net.darkscorner.paintball.objects.menus.arenaeditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.Arena;

public class LobbyArenaEditor extends ArenaEditorItem {

	public LobbyArenaEditor(ItemStack item, Arena arena) {
		super(item, arena);
	}

	@Override
	public void use(Player player, Action action, Location loc) {
		getArena().setLobbyLocation(loc);
		player.sendMessage(Main.prefix + "Set lobby location for arena " + getArena().getName());
	}

}
