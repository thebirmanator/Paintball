package net.darkscorner.paintball.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(!event.getPlayer().hasMetadata("editArena")) {
			event.setCancelled(true);
		} 
	}
}
