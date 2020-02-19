package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.objects.games.GameSettings;

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
