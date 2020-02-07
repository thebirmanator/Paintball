package net.darkscorner.paintball.listeners.gamelisteners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.events.GameCreateEvent;

public class GameCreateListener implements Listener {

	@EventHandler
	public void onCreate(GameCreateEvent event) {
		//event.getGame().getUsedArena().setIsInUse(true);
		event.getGame().waitForPlayers(true);
	}
}
