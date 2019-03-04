package net.darkscorner.paintball.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Objects.PaintballGame;

public class GameStartEvent extends Event {

	private PaintballGame game;
	public GameStartEvent(PaintballGame game) {
		this.game = game;
	}
	
	private static HandlerList handlers = new HandlerList();
	
	public PaintballGame getGame() {
		return game;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
}
