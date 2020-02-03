package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.objects.games.Game;

public class GameStartEvent extends Event {

	private Game game;
	public GameStartEvent(Game game) {
		this.game = game;
	}
	
	private static HandlerList handlers = new HandlerList();
	
	public Game getGame() {
		return game;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
}
