package net.darkscorner.paintball.listeners.gamelisteners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GamePlayerLeaveEvent;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.PaintballGame;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;

public class GamePlayerLeaveListener implements Listener {

	@EventHandler
	public void onGameLeave(GamePlayerLeaveEvent event) {
		PaintballGame game = event.getGame();
		boolean wasSpectator = game.getSpectatingPlayers().contains(event.getPlayer());
		event.getPlayer().getPlayer().setGameMode(Main.defaultGamemode);
		
		// if the game isnt idle (they are in a lobby), dont tp them to the lobby
		event.getPlayer().getPlayer().teleport(PaintballGame.getLobbySpawn());
		event.getPlayer().setStatsBoard(StatsBoard.LOBBY);
		
		if(!wasSpectator) { // if they were not a spectator
			// send message to everyone that game that the player left
			if(game.getGameState() != GameState.ENDED) {
				for(GamePlayer p : game.getAllPlayers()) {
					p.getPlayer().sendMessage(ChatColor.YELLOW + event.getPlayer().getPlayer().getName() + ChatColor.GRAY + " disconnected from the game.");
				}
			}
			// if only one player remains in game, end it
			if(game.getGameState() == GameState.STARTED && game.getInGamePlayers().size() < 2) {
				game.endGame();
			}
		}
	}
}
