package ltd.indigostudios.paintball.listeners.gamelisteners;

import ltd.indigostudios.paintball.events.GameCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameCreateListener implements Listener {

    @EventHandler
    public void onCreate(GameCreateEvent event) {
        //event.getGame().getUsedArena().setIsInUse(true);
        event.getGame().waitForPlayers(true);
    }
}
