package net.darkscorner.paintball.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(!event.getPlayer().hasMetadata("editArena")) {
			event.setCancelled(true);
		}
	}
}
