package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.arena.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		for (Arena arena : Arena.getArenas()) {
			if (arena.isInArena(event.getPlayer())) {
				if (!event.getPlayer().hasMetadata("editArena")) {
					event.setCancelled(true);
				}
			}
		}
	}
}
