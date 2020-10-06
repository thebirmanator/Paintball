package ltd.indigostudios.paintball.events;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.powerups.PowerUp;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PowerUpUseEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private GameSettings game;
    private Location location;
    private PowerUp powerUp;
    private PlayerProfile player;

    public PowerUpUseEvent(PowerUp powerUp, Location location, GameSettings game, PlayerProfile player) {
        this.powerUp = powerUp;
        this.location = location;
        this.game = game;
        this.player = player;
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public Location getLocation() {
        return location;
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
