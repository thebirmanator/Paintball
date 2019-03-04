package net.darkscorner.paintball.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Objects.PaintballGame;

public class GameEndEvent extends Event {

	private PaintballGame game;
	private static HandlerList handlers = new HandlerList();
	
	public GameEndEvent(PaintballGame game) {
		this.game = game;
	}
	
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
