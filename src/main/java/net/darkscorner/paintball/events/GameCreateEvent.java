package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.games.Game;

public class GameCreateEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private Game game;
	
	public GameCreateEvent(Main main, Game game) {
		this.game = game;
	}
	
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
