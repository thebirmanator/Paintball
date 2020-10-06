package ltd.indigostudios.paintball.events;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {

    private GameSettings game;
    private static HandlerList handlers = new HandlerList();

    public GameEndEvent(GameSettings game) {
        this.game = game;
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
