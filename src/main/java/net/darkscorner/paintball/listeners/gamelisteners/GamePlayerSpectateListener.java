package net.darkscorner.paintball.listeners.gamelisteners;

import net.darkscorner.paintball.objects.scoreboards.GameScoreboard2;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GameSpectateEvent;

public class GamePlayerSpectateListener implements Listener {
	
	@EventHandler
	public void onGameSpectate(GameSpectateEvent event) {
		GameScoreboard2.getBoard(event.getPlayer(), StatsBoard.SPECTATE).display();
		event.getPlayer().getPlayer().sendMessage(Main.prefix + "You are now " + ChatColor.GREEN + "spectating" + ChatColor.GRAY + " this game.");
	}
}
