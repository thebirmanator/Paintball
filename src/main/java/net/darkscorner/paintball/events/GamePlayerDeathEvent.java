package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.games.Game;

public class GamePlayerDeathEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	private GamePlayer victim;
	private GamePlayer attacker;
	private Game game;
	
	public GamePlayerDeathEvent(Game game, GamePlayer victim, GamePlayer attacker) {
		this.game = game;
		this.victim = victim;
		this.attacker = attacker;
	}
	
	public GamePlayerDeathEvent(Game game, GamePlayer victim) {
		this.game = game;
		this.victim = victim;
		this.attacker = victim;
	}
	
	public Game getGame() {
		return game;
	}
	
	public GamePlayer getVictim() {
		return victim;
	}
	
	public GamePlayer getKiller() {
		return attacker;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
