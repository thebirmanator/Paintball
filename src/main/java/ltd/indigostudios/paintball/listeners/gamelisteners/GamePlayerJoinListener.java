package ltd.indigostudios.paintball.listeners.gamelisteners;

import ltd.indigostudios.paintball.events.GamePlayerJoinEvent;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.games.Team;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.scoreboards.GameScoreboard;
import ltd.indigostudios.paintball.objects.scoreboards.StatsBoard;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GamePlayerJoinListener implements Listener {

	@EventHandler
	public void onGameJoin(GamePlayerJoinEvent event) {
		GameSettings game = event.getGame();
		PlayerProfile playerProfile = event.getPlayer();
		
		playerProfile.getPlayer().teleport(game.getArena().getLobby().getSpawnLocation());
		
		// Tell everyone that someone joined
		for (PlayerProfile p : game.getAllPlayers()) {
			if (!p.equals(playerProfile)) {
				p.getPlayer().sendMessage(Text.format("&e" + playerProfile.getPlayer().getName() + " &7joined the game."));
			}
		}

		// If team game, give team armour and tell them which team they're on
		if (game instanceof TeamGame) {
			TeamGame teamGame = (TeamGame) game;
			Team playerTeam = teamGame.getTeam(playerProfile);
			playerProfile.getPlayer().getInventory().setArmorContents(playerTeam.getArmourSet());
			playerProfile.getPlayer().sendMessage(Text.format("&7You are on team &f" + playerTeam.getName()));
			new GameScoreboard(playerProfile, GameScoreboard.getContent(StatsBoard.TEAM_GAME)).display();
		} else {
			new GameScoreboard(playerProfile, GameScoreboard.getContent(StatsBoard.FREE_FOR_ALL_GAME)).display();
		}
	}
}
