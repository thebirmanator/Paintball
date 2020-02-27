package ltd.indigostudios.paintball.listeners.gamelisteners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ltd.indigostudios.paintball.objects.powerups.PowerUp;
import ltd.indigostudios.paintball.objects.games.Team;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import ltd.indigostudios.paintball.objects.games.GameState;
import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.GameStartEvent;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.games.GameSettings;

public class GameStartListener implements Listener {

	private Main main;
	public GameStartListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		GameSettings game = event.getGame();

		// End game if a team has 0 players
		if (game instanceof TeamGame) {
			Set<Team> teams = ((TeamGame) game).getTeams();
			for (Team team : teams) {
				if (team.getMembers().size() < 1) {
					game.endGame();
				}
			}
		}
		
		Random random = new Random();
		List<Location> availableSpawnpoints = new ArrayList<Location>();
		availableSpawnpoints.addAll(game.getArena().getFreeForAllSpawnPoints());
		for(PlayerProfile gp : game.getPlayers(true)) {
			// set gamemode, find a spawnpoint
			gp.getPlayer().setGameMode(Main.defaultGamemode);
			
			if(availableSpawnpoints.size() == 0) { // all spawn points have been used, add them all back to be used again
				availableSpawnpoints.addAll(game.getArena().getFreeForAllSpawnPoints());
			}
			
			// find a spawnpoint
			int spawnIndex = random.nextInt(availableSpawnpoints.size());
			Location spawn = availableSpawnpoints.get(spawnIndex).clone();
			availableSpawnpoints.remove(spawnIndex);
			spawn = spawn.add(0.5, 0, 0.5);
			gp.getPlayer().teleport(spawn);
			
			gp.getGun().giveTo(gp.getPlayer());
		}

		// spawn powerups at their spawn points
		List<Location> powerupSpawns = game.getArena().getPowerUpSpawnPoints();
		for(Location spawn : powerupSpawns) {
			// choose a random powerup
			int powerupIndex = random.nextInt(PowerUp.getPowerUps().size());
			PowerUp powerup = PowerUp.getPowerUps().get(powerupIndex);
			// choose how long to wait to spawn it
			int spawnDelay = random.nextInt(powerup.getMaxSpawnDelay());
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				@Override
				public void run() {
					if(event.getGame().getGameState() != GameState.ENDED) {
						powerup.spawnPowerUp(spawn);
					}
					
				}
			}, spawnDelay);
			
		}
	}
}
