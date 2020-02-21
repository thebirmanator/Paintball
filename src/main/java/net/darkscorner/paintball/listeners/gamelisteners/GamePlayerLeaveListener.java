package net.darkscorner.paintball.listeners.gamelisteners;

import net.darkscorner.paintball.objects.games.Team;
import net.darkscorner.paintball.objects.equippable.guns.ShotGun;
import net.darkscorner.paintball.objects.games.TeamGame;
import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.player.PlayerStat;
import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.objects.games.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GamePlayerLeaveEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.GameSettings;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;

import java.util.Set;

public class GamePlayerLeaveListener implements Listener {

	@EventHandler
	public void onGameLeave(GamePlayerLeaveEvent event) {
		GameSettings game = event.getGame();
		PlayerProfile player = event.getPlayer();
		boolean wasSpectator = game.getPlayers(false).contains(player);
		player.getPlayer().setGameMode(Main.defaultGamemode);
		
		// if the game isnt idle (they are in a lobby), dont tp them to the lobby
		player.getPlayer().teleport(event.getGame().getLobbySpawn());
		//player.setStatsBoard(StatsBoard.LOBBY);
		
		if(!wasSpectator) { // if they were not a spectator

			// remove paintball gun on leave
			player.getGun().removeFrom(player.getPlayer());
			// If team game, remove team armour
			if (game instanceof TeamGame) {
				player.getPlayer().getInventory().setArmorContents(null);
			}

			// send message to everyone that game that the player left
			if(game.getGameState() != GameState.ENDED) {
				for(PlayerProfile p : game.getAllPlayers()) {
					p.getPlayer().sendMessage(ChatColor.YELLOW + event.getPlayer().getPlayer().getName() + ChatColor.GRAY + " disconnected from the game.");
				}
			}

			// remove invulnerable if they leave
			//game.makeVulnerable(player.getPlayer());

			// remove gun cooldown if they leave
			if(player.getPlayer().hasMetadata(ShotGun.metaCooldown)) {
				player.getPlayer().removeMetadata(ShotGun.metaCooldown, Main.getInstance());
			}

			if (game.getGameState() == GameState.STARTED) {
				// If only one player remains in game, end it
				if (game.getPlayers(true).size() < 2) {
					game.endGame();
				}
				// If it's a team game and a team now has 0 players, end the game
				if (game instanceof TeamGame) {
					Set<Team> teams = ((TeamGame) game).getTeams();
					for (Team team : teams) {
						if (team.getMembers().size() < 1) {
							game.endGame();
							break;
						}
					}
				}
			}
			//TODO: figure out why it lags when you leave?
			new GameScoreboard(player, GameScoreboard.getContent(StatsBoard.LOBBY)).display();
			//GameScoreboard2.getBoard(player, StatsBoard.LOBBY).display();

			// save stats
			//player.addToTotal(PlayerStat.DEATHS, player.getCurrentGameStats().getStat(PlayerInGameStat.DEATHS));
			//player.addToTotal(PlayerStat.KILLS, player.getCurrentGameStats().getStat(PlayerInGameStat.KILLS));
			//player.addToTotal(PlayerStat.SHOTS, player.getCurrentGameStats().getStat(PlayerInGameStat.SHOTS));
			player.saveProfile();
		}
	}
}
