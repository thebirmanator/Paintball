package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.Game;

public class GamePlayerJoinEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private PlayerProfile player;
	private Game game;
	
	public GamePlayerJoinEvent(PlayerProfile player, Game game) {
		this.player = player;
		this.game = game;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public PlayerProfile getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
}
