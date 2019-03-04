package net.darkscorner.paintball.listeners.gamelisteners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GameSpectateEvent;

public class GamePlayerSpectateListener implements Listener {
	
	@EventHandler
	public void onGameSpectate(GameSpectateEvent event) {
		event.getPlayer().getPlayer().sendMessage(Main.prefix + "You are now " + ChatColor.GREEN + "spectating" + ChatColor.GRAY + " this game.");
	}
}
