package net.darkscorner.paintball.Events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.darkscorner.paintball.Objects.GamePlayer;
import net.darkscorner.paintball.Objects.PaintballGame;
import net.darkscorner.paintball.Objects.PowerUp;

public class PowerUpUseEvent extends Event {

	private static HandlerList handlers = new HandlerList();
	
	private PaintballGame game;
	private Location location;
	private PowerUp powerUp;
	private GamePlayer player;
	
	public PowerUpUseEvent(PowerUp powerUp, Location location, PaintballGame game, GamePlayer player) {
		this.powerUp = powerUp;
		this.location = location;
		this.game = game;
		this.player = player;
	}
	
	public GamePlayer getPlayer() {
		return player;
	}
	
	public PowerUp getPowerUp() {
		return powerUp;
	}
	
	public Location getLocation() {
		return location;
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
