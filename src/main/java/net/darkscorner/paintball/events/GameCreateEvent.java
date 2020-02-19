package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.games.GameSettings;

public class GameCreateEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private GameSettings game;
	
	public GameCreateEvent(Main main, GameSettings game) {
		this.game = game;
	}
	
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
