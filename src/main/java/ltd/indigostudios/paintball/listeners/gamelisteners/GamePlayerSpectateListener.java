package ltd.indigostudios.paintball.listeners.gamelisteners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.GameSpectateEvent;
import ltd.indigostudios.paintball.objects.scoreboards.GameScoreboard;
import ltd.indigostudios.paintball.objects.scoreboards.StatsBoard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GamePlayerSpectateListener implements Listener {

    @EventHandler
    public void onGameSpectate(GameSpectateEvent event) {
        new GameScoreboard(event.getPlayer(), GameScoreboard.getContent(StatsBoard.SPECTATE)).display();
        //GameScoreboard2.getBoard(event.getPlayer(), StatsBoard.SPECTATE).display();
        event.getPlayer().getPlayer().sendMessage(Main.prefix + "You are now " + ChatColor.GREEN + "spectating" + ChatColor.GRAY + " this game.");
    }
}
