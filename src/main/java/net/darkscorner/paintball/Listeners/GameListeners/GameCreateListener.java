package net.darkscorner.paintball.Listeners.GameListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.Events.GameCreateEvent;

public class GameCreateListener implements Listener {

	@EventHandler
	public void onCreate(GameCreateEvent event) {
		event.getGame().getUsedArena().setIsInUse(true);
		event.getGame().startWaitingForPlayers();
	}
}
