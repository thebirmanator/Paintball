package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		for (Arena arena : Arena.getArenas()) {
			if (arena.isInArena(event.getPlayer()) || arena.getLobby().isInLobby(event.getPlayer())) {
				if (!arena.isEditing(event.getPlayer())) {
					event.setCancelled(true);
				}
			}
		}
	}
}
