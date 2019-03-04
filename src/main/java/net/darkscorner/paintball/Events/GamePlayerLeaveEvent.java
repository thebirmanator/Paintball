package net.darkscorner.paintball.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Objects.GamePlayer;
import net.darkscorner.paintball.Objects.PaintballGame;

public class GamePlayerLeaveEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private GamePlayer player;
	private PaintballGame game;
	
	public GamePlayerLeaveEvent(GamePlayer player, PaintballGame game) {
		this.player = player;
		this.game = game;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public GamePlayer getPlayer() {
		return player;
	}
	
	public PaintballGame getGame() {
		return game;
	}
}
