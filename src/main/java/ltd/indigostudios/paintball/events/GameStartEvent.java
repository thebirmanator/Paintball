package ltd.indigostudios.paintball.events;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

	private GameSettings game;
	public GameStartEvent(GameSettings game) {
		this.game = game;
	}
	
	private static HandlerList handlers = new HandlerList();
	
	public GameSettings getGame() {
		return game;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
}
