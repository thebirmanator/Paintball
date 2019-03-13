package net.darkscorner.paintball.listeners.gamelisteners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.events.GamePlayerJoinEvent;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.PaintballGame;

public class GamePlayerJoinListener implements Listener {

	@EventHandler
	public void onGameJoin(GamePlayerJoinEvent event) {
		PaintballGame game = event.getGame();
		
		event.getPlayer().getPlayer().teleport(game.getUsedArena().getLobbyLocation());
		
		// tell everyone that someone joined
		for(GamePlayer p : game.getAllPlayers()) {
			if(!p.equals(event.getPlayer())) {
				p.getPlayer().sendMessage(ChatColor.YELLOW + event.getPlayer().getPlayer().getName() + ChatColor.GRAY + " joined the game.");
			}
		}
		
		// if players joining the game is the same or higher than required start amount, start the countdown
		if(game.getGameState() == GameState.IDLE) {
			if(game.getInGamePlayers().size() >= game.getStartPlayerAmount()) {
				game.startCountdown();
			}
		}
	}
}