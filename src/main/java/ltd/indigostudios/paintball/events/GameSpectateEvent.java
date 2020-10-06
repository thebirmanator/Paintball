package ltd.indigostudios.paintball.events;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameSpectateEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private PlayerProfile player;
    private GameSettings game;

    public GameSpectateEvent(PlayerProfile player, GameSettings game) {
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

    public GameSettings getGame() {
        return game;
    }
}
