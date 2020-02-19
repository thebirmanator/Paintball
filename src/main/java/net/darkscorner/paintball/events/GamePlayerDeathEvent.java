package net.darkscorner.paintball.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.GameSettings;

public class GamePlayerDeathEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	private PlayerProfile victim;
	private PlayerProfile attacker;
	private GameSettings game;
	
	public GamePlayerDeathEvent(GameSettings game, PlayerProfile victim, PlayerProfile attacker) {
		this.game = game;
		this.victim = victim;
		this.attacker = attacker;
	}
	
	public GamePlayerDeathEvent(GameSettings game, PlayerProfile victim) {
		this.game = game;
		this.victim = victim;
		this.attacker = victim;
	}
	
	public GameSettings getGame() {
		return game;
	}
	
	public PlayerProfile getVictim() {
		return victim;
	}
	
	public PlayerProfile getKiller() {
		return attacker;
	}
	
	public static HandlerList getHandlerList() { 
		return handlers; 
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

}
