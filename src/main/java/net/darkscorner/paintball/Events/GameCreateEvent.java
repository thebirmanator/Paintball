package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.PaintballGame;

public class GameCreateEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private PaintballGame game;
	
	public GameCreateEvent(Main main, PaintballGame game) {
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
