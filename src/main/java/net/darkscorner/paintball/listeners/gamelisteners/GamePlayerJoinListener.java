package net.darkscorner.paintball.listeners.gamelisteners;

import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.events.GamePlayerJoinEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.Game;

public class GamePlayerJoinListener implements Listener {

	@EventHandler
	public void onGameJoin(GamePlayerJoinEvent event) {
		Game game = event.getGame();
		PlayerProfile playerProfile = event.getPlayer();
		
		playerProfile.getPlayer().teleport(game.getArena().getLobbyLocation());
		new GameScoreboard(playerProfile, GameScoreboard.getContent(StatsBoard.INGAME)).display();
		//GameScoreboard2.getBoard(playerProfile, StatsBoard.INGAME).display();
		
		// tell everyone that someone joined
		for(PlayerProfile p : game.getAllPlayers()) {
			if(!p.equals(event.getPlayer())) {
				p.getPlayer().sendMessage(ChatColor.YELLOW + event.getPlayer().getPlayer().getName() + ChatColor.GRAY + " joined the game.");
			}
		}
		
		// if players joining the game is the same or higher than required start amount, start the countdown
		//if(game.getGameState() == GameState.IDLE) {
		//	if(game.getPlayers(true).size() >= game.getStartPlayerAmount()) {
		//		game.countdown(true);
		//	}
		//}
	}
}
