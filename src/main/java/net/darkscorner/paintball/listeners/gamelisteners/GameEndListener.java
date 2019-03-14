package net.darkscorner.paintball.listeners.gamelisteners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.darkscorner.paintball.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GameEndEvent;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.PaintballGame;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;

public class GameEndListener implements Listener {

	private Main main;
	public GameEndListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		PaintballGame game = event.getGame();
		
		// remove powerups from map
		for(Location loc : game.getUsedArena().getPowerUpSpawnPoints()) {
			loc.getBlock().setType(Material.AIR);
		}
		
		// sort the players from highest to lowest score
		List<GamePlayer> players = new ArrayList<GamePlayer>(game.getInGamePlayers());
		Collections.sort(players, new Comparator<GamePlayer>() {

			@Override
			public int compare(GamePlayer p1, GamePlayer p2) {
				if(p1.getStats().getKills() > p2.getStats().getKills()) {
					return -1;
				} else if(p1.getStats().getKills() < p2.getStats().getKills()) {
					return 1;
				}
				return 0;
				
			}
		});
		
		// report stats to each player, update their profiles
		for(GamePlayer gp : game.getInGamePlayers()) {
			sendStatsMessage(gp);

			// gameplayerleavelistener takes care of the other stats, update games played
			gp.addToTotal(PlayerStat.GAMES, 1);
			gp.saveProfile();

			gp.getPlayer().getInventory().clear();
			gp.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
		
		// trim the top three from the list and broadcast
		for(int i = 0; i < 3; i++) {
			if(i < players.size()) {
				String name = players.get(i).getPlayer().getName();
				int kills = players.get(i).getStats().getKills();
				for(GamePlayer pInGame : game.getAllPlayers()) {
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
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			public void run() {
				for(GamePlayer p : game.getAllPlayers()) {
					p.removePowerUps();
					p.getPlayer().teleport(PaintballGame.getLobbySpawn());
					p.setStatsBoard(StatsBoard.LOBBY);
					p.getPlayer().setGameMode(Main.defaultGamemode);
					p.getPlayer().sendMessage(Main.prefix + "You have been sent to the lobby.");
					game.removePlayer(p);
				}
				
				game.getUsedArena().setIsInUse(false);
			}
		}, 100);
	}
	
	private void sendStatsMessage(GamePlayer gp) {
		int deaths = gp.getStats().getDeaths();
		int kills = gp.getStats().getKills();
		int shots = gp.getStats().getNumShotsFired();

		String kdrMessage;
		if(deaths == 0) { // undefined kda
			kdrMessage = ChatColor.GOLD + "PERFECT " + ChatColor.GRAY + "(no deaths)!";
		} else {
			float kdr = (float) kills / (float) deaths;
			kdrMessage = String.format("%.2f", kdr);
		}
		gp.getPlayer().sendMessage("");
		gp.getPlayer().sendMessage(Main.prefix + "Game Stats:");
		gp.getPlayer().sendMessage(ChatColor.AQUA + "   You have gained " + (gp.getStats().getKills() * gp.getCurrentGame().getCrystalsPerKill()) + " crystals.");
		gp.getPlayer().sendMessage(ChatColor.GRAY + "   Your kill-death ratio: " + ChatColor.GREEN + kills + ChatColor.DARK_GRAY + ":" + ChatColor.RED + deaths + ChatColor.GRAY + "=" + kdrMessage);

		if(shots == 0) { // undefined accuracy
			gp.getPlayer().sendMessage(ChatColor.GRAY + "   Your accuracy: " + ChatColor.GOLD + "You know you're supposed to right click to shoot, right? (no shots fired)");
		} else {
			float accuracy = (float) kills / (float) shots;
			String accuracyMessage = String.format("%.2f", accuracy);
			gp.getPlayer().sendMessage(ChatColor.GRAY + "   Your accuracy: " + ChatColor.GOLD + accuracyMessage);
		}
		gp.getPlayer().sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "   Map Creator: " + ChatColor.DARK_AQUA + gp.getCurrentGame().getUsedArena().getCreator());
		gp.getPlayer().sendMessage("");
	}
}
