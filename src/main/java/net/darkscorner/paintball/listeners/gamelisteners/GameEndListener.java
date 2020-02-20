package net.darkscorner.paintball.listeners.gamelisteners;

import java.util.*;

import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.games.Team;
import net.darkscorner.paintball.objects.games.TeamGame;
import net.darkscorner.paintball.objects.player.PlayerGameStatistics;
import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.player.PlayerStat;
import net.darkscorner.paintball.objects.powerups.PowerUp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GameEndEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.GameSettings;

public class GameEndListener implements Listener {

	private Main main;
	public GameEndListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		GameSettings game = event.getGame();

		// Send end titles
		String subtitle = "";
		if (game instanceof TeamGame) {
			List<Team> teams = new ArrayList<>(((TeamGame) game).getTeams());
			teams.sort((t1, t2) -> {
				if (t1.getKills() > t2.getKills()) {
					return -1;
				} else if (t1.getKills() < t2.getKills()) {
					return 1;
				} else {
					return 0;
				}
			});
			subtitle = teams.get(0).getName() + " team won! (" + teams.get(0).getKills() + " kills)";
		}
		for (PlayerProfile p : game.getAllPlayers()) {
			p.getPlayer().sendTitle(net.md_5.bungee.api.ChatColor.GREEN + "Game Over!", subtitle, 5, 20, 5);
			p.playSound(SoundEffect.GAME_END);
		}
		
		// remove powerups from map
		for(Location loc : game.getArena().getPowerUpSpawnPoints()) {
			loc.getBlock().setType(Material.AIR);
		}
		
		// sort the players from highest to lowest score
		List<PlayerProfile> players = new ArrayList<PlayerProfile>(game.getPlayers(true));
		players.sort((p1, p2) -> {
			if (p1.getCurrentGameStats().getStat(PlayerInGameStat.KILLS) >
					p2.getCurrentGameStats().getStat(PlayerInGameStat.KILLS)) {
				return -1;
			} else if (p1.getCurrentGameStats().getStat(PlayerInGameStat.KILLS) <
					p2.getCurrentGameStats().getStat(PlayerInGameStat.KILLS)) {
				return 1;
			}
			return 0;

		});
		
		// report stats to each player, update their profiles
		for(PlayerProfile gp : game.getPlayers(true)) {
			sendStatsMessage(gp);

			// gameplayerleavelistener takes care of the other stats, update games played
			gp.addToTotal(PlayerStat.GAMES_PLAYED, 1);
			gp.saveProfile();

			gp.getPlayer().getInventory().clear();
			gp.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
		
		// trim the top three from the list and broadcast
		for(int i = 0; i < 3; i++) {
			if(i < players.size()) {
				String name = players.get(i).getPlayer().getName();
				int kills = players.get(i).getCurrentGameStats().getStat(PlayerInGameStat.KILLS);
				for(PlayerProfile pInGame : game.getAllPlayers()) {
					if(i == 0) { // top of the board
						pInGame.getPlayer().sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Leaderboard");
					}
					pInGame.getPlayer().sendMessage(ChatColor.GREEN + " #" + ChatColor.BOLD + (i + 1) + ChatColor.RESET + " " + name + ChatColor.GOLD + " (" + kills + " hits)");
				}
			} else {
				break;
			}
		}

		// send everyone to the lobby after some time
		Bukkit.getScheduler().runTaskLater(main, () -> {
			// use a list from the set because a normal for-each loop will throw a ConcurrentModificationException
			List<PlayerProfile> players1 = new ArrayList<>(game.getAllPlayers());
			for (int i = 0; i < players1.size(); i++) {
				PlayerProfile p = players1.get(i);
				//p.removePowerUps();
				PowerUp.clearEffects(p.getPlayer());
				p.getPlayer().teleport(game.getLobbySpawn());
				//p.setStatsBoard(StatsBoard.LOBBY);
				p.getPlayer().setGameMode(Main.defaultGamemode);
				p.getPlayer().sendMessage(Main.prefix + "You have been sent to the lobby.");
				game.removePlayer(p);
			}
			/*
			for (Iterator<PlayerProfile> profileIterator = game.getAllPlayers().iterator(); profileIterator.hasNext();) {
				PlayerProfile p = profileIterator.next();
				p.removePowerUps();
				p.getPlayer().teleport(game.getLobbySpawn());
				p.setStatsBoard(StatsBoard.LOBBY);
				p.getPlayer().setGameMode(Main.defaultGamemode);
				p.getPlayer().sendMessage(Main.prefix + "You have been sent to the lobby.");
				game.removePlayer(p);
			}*/
			//game.getAllPlayers().clear();

			//game.getUsedArena().setIsInUse(false);
		}, 100);
	}
	
	private void sendStatsMessage(PlayerProfile gp) {
		PlayerGameStatistics stats = gp.getCurrentGameStats();
		int deaths = stats.getStat(PlayerInGameStat.DEATHS);
		int kills = stats.getStat(PlayerInGameStat.KILLS);
		int shots = stats.getStat(PlayerInGameStat.SHOTS);

		String kdrMessage;
		if(deaths == 0) { // undefined kda
			kdrMessage = ChatColor.GOLD + "PERFECT " + ChatColor.GRAY + "(no deaths)!";
		} else {
			float kdr = (float) kills / (float) deaths;
			kdrMessage = String.format("%.2f", kdr);
		}
		gp.getPlayer().sendMessage("");
		gp.getPlayer().sendMessage(Main.prefix + "Game Stats:");
		//gp.getPlayer().sendMessage(ChatColor.AQUA + "   You have gained " + (gp.getStats().getKills() * gp.getCurrentGame().getCoinsPerKill()) + " Arcade Coins.");
		gp.getPlayer().sendMessage(ChatColor.GRAY + "   Your kill-death ratio: " + ChatColor.GREEN + kills + ChatColor.DARK_GRAY + ":" + ChatColor.RED + deaths + ChatColor.GRAY + "=" + kdrMessage);

		if(shots == 0) { // undefined accuracy
			gp.getPlayer().sendMessage(ChatColor.GRAY + "   Your accuracy: " + ChatColor.GOLD + "You know you're supposed to right click to shoot, right? (no shots fired)");
		} else {
			float accuracy = (float) kills / (float) shots;
			String accuracyMessage = String.format("%.2f", accuracy);
			gp.getPlayer().sendMessage(ChatColor.GRAY + "   Your accuracy: " + ChatColor.GOLD + accuracyMessage);
		}
		gp.getPlayer().sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "   Map Creator: " + ChatColor.DARK_AQUA + gp.getCurrentGame().getArena().getCreator());
		gp.getPlayer().sendMessage("");
	}
}
