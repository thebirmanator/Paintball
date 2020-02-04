package net.darkscorner.paintball.listeners.gamelisteners;

import net.darkscorner.paintball.objects.player.PlayerStat;
import net.darkscorner.paintball.objects.guns.ShotGun;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GamePlayerLeaveEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;

public class GamePlayerLeaveListener implements Listener {

	@EventHandler
	public void onGameLeave(GamePlayerLeaveEvent event) {
		Game game = event.getGame();
		PlayerProfile player = event.getPlayer();
		boolean wasSpectator = game.getSpectatingPlayers().contains(player);
		player.getPlayer().setGameMode(Main.defaultGamemode);
		
		// if the game isnt idle (they are in a lobby), dont tp them to the lobby
		player.getPlayer().teleport(Game.getLobbySpawn());
		player.setStatsBoard(StatsBoard.LOBBY);
		
		if(!wasSpectator) { // if they were not a spectator

			// remove paintball gun on leave
			player.getGun().removeFrom(player.getPlayer());

			// send message to everyone that game that the player left
			if(game.getGameState() != GameState.ENDED) {
				for(PlayerProfile p : game.getAllPlayers()) {
					p.getPlayer().sendMessage(ChatColor.YELLOW + event.getPlayer().getPlayer().getName() + ChatColor.GRAY + " disconnected from the game.");
				}
			}

			// remove invulnerable if they leave
			game.makeVulnerable(player.getPlayer());

			// remove gun cooldown if they leave
			if(player.getPlayer().hasMetadata(ShotGun.metaCooldown)) {
				player.getPlayer().removeMetadata(ShotGun.metaCooldown, Main.getPlugin(Main.class));
			}

			// if only one player remains in game, end it
			if(game.getGameState() == GameState.STARTED && game.getInGamePlayers().size() < 2) {
				game.endGame();
			}

			// save stats
			player.addToTotal(PlayerStat.DEATHS, player.getStats().getDeaths());
			player.addToTotal(PlayerStat.KILLS, player.getStats().getKills());
			player.addToTotal(PlayerStat.SHOTS, player.getStats().getNumShotsFired());
			player.saveProfile();
		}
	}
}
