package ltd.indigostudios.paintball.events;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameCreateEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private GameSettings game;

    public GameCreateEvent(Main main, GameSettings game) {
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
