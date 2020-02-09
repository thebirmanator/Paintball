package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.menus.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.darkscorner.paintball.objects.games.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.arena.Arena;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;

public class JoinListener implements Listener {

	private Main main;
	public JoinListener(Main main) {
		this.main = main;
	}
	
	// when the player joins the server
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		// set server join message
		event.setJoinMessage(null);
		
		// remove paintball gun on join
		event.getPlayer().getInventory().remove(Material.GOLDEN_HOE);
		
		// remove edit kit
		if (Arena.getEditing(event.getPlayer()) != null) {
			Menu.getViewing(event.getPlayer()).close(event.getPlayer());
		}
		/*
		if(event.getPlayer().hasMetadata(ArenaEditCommand.editMeta)) {
			String arena = event.getPlayer().getMetadata(ArenaEditCommand.editMeta).get(0).asString();
			if(!arena.isEmpty()) { // is not in general block edit mode
				Arena.getArena(arena).removeEditKit(event.getPlayer());
			}
		}*/
					
		// set to survival mode in case they were spectating
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		// set to full health and hunger
		event.getPlayer().setHealth(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		event.getPlayer().setFoodLevel(20);
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				Player player = event.getPlayer();
				//player.teleport(Game.getLobbySpawn());
				PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
				if(playerProfile != null) {
					if(playerProfile.isInGame()) { // player was in a game
						Game game = playerProfile.getCurrentGame();
						if(game.getGameState() != GameState.ENDED) { // set player to spectate if their game hasnt ended
							game.setToSpectating(playerProfile);
						} else { // player game has ended
							//player.teleport(Game.getLobbySpawn());
							player.sendMessage(Main.prefix + "The game you were in has ended.");
							playerProfile.setStatsBoard(StatsBoard.LOBBY);
						}
					} else { // player is not in a game
						//player.teleport(Game.getLobbySpawn());
						player.sendMessage(Main.prefix + "Welcome! Do " + ChatColor.GREEN + "/join" + ChatColor.GRAY + " to enter a game or " + ChatColor.GREEN + "/spec" + ChatColor.GRAY + " to spectate one.");
						playerProfile.setStatsBoard(StatsBoard.LOBBY);
					}
				} else { // the player is new to the server
					new PlayerProfile(player);
					//player.teleport(Game.getLobbySpawn());
					player.sendMessage(Main.prefix + "Welcome! Do " + ChatColor.GREEN + "/join" + ChatColor.GRAY + " to enter a game or " + ChatColor.GREEN + "/spec" + ChatColor.GRAY + " to spectate one.");
				}

				// send join message late to get rank to show
				String joinMessage = ChatColor.GREEN + "+ " + ChatColor.RESET + event.getPlayer().getDisplayName();
				Bukkit.broadcastMessage(joinMessage);
			}
		}, 2);
	}
}
