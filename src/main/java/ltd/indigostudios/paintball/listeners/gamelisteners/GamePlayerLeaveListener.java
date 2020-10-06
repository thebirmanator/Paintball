package ltd.indigostudios.paintball.listeners.gamelisteners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.GamePlayerLeaveEvent;
import ltd.indigostudios.paintball.objects.equippable.guns.ShotGun;
import ltd.indigostudios.paintball.objects.games.*;
import ltd.indigostudios.paintball.objects.player.PlayerInGameStat;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.player.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class GamePlayerLeaveListener implements Listener {

    @EventHandler
    public void onGameLeave(GamePlayerLeaveEvent event) {
        GameSettings game = event.getGame();
        PlayerProfile player = event.getPlayer();
        boolean wasSpectator = game.getPlayers(false).contains(player);
        player.getPlayer().setGameMode(Main.defaultGamemode);

        // if the game isnt idle (they are in a lobby), dont tp them to the lobby
        //player.getPlayer().teleport(event.getGame().getLobbySpawn());
        //player.setStatsBoard(StatsBoard.LOBBY);

        if (!wasSpectator) { // if they were not a spectator
            // remove paintball gun on leave
            player.getGun().removeFrom(player.getPlayer());
            // If team game, remove team armour
            if (game instanceof TeamGame) {
                player.getPlayer().getInventory().setArmorContents(null);
            }

            // send message to everyone that game that the player left
            if (game.getGameState() != GameState.ENDED) {
                for (PlayerProfile p : game.getAllPlayers()) {
                    p.getPlayer().sendMessage(ChatColor.YELLOW + event.getPlayer().getPlayer().getName() + ChatColor.GRAY + " disconnected from the game.");
                }
            }

            // remove invulnerable if they leave
            PaintballGame pbGame = (PaintballGame) game;
            if (!pbGame.isVulnerable(player.getPlayer())) {
                pbGame.makeVulnerable(player.getPlayer());
            }

            //TODO: not check metadata directly
            // remove gun cooldown if they leave
            if (player.getPlayer().hasMetadata(ShotGun.metaCooldown)) {
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
            //new GameScoreboard(player, GameScoreboard.getContent(StatsBoard.LOBBY)).display();
            //GameScoreboard2.getBoard(player, StatsBoard.LOBBY).display();

            // save stats
            player.addToTotal(PlayerStat.DEATHS, player.getCurrentGameStats().getStat(PlayerInGameStat.DEATHS));
            player.addToTotal(PlayerStat.KILLS, player.getCurrentGameStats().getStat(PlayerInGameStat.KILLS));
            player.addToTotal(PlayerStat.SHOTS, player.getCurrentGameStats().getStat(PlayerInGameStat.SHOTS));
            player.saveProfile();
        }
        // Clear scoreboard regardless of spec or in game
        player.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        player.getPlayer().teleport(event.getGame().getLobbySpawn());
    }
}
