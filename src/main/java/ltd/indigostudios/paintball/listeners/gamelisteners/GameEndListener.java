package ltd.indigostudios.paintball.listeners.gamelisteners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.GameEndEvent;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.games.PaintballGame;
import ltd.indigostudios.paintball.objects.games.Team;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import ltd.indigostudios.paintball.objects.player.PlayerInGameStat;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.player.PlayerStat;
import ltd.indigostudios.paintball.objects.powerups.PowerUp;
import ltd.indigostudios.paintball.utils.SoundEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameEndListener implements Listener {

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        GameSettings game = event.getGame();

        Set<PlayerProfile> players = game.getAllPlayers();
        // Do stuff that gets sent to all game players
        for (PlayerProfile p : players) {
            // Send end titles
            p.getPlayer().sendTitle(net.md_5.bungee.api.ChatColor.GREEN + "Game Over!", "", 5, 20, 5);
            p.playSound(SoundEffect.GAME_END);
            // Stuff for in game players only; no spectators
            if (game.getPlayers(true).contains(p)) {
                p.getPlayer().sendMessage(game.evaluateSummary(p));

                // Gameplayerleavelistener takes care of the other stats, update games played
                p.addToTotal(PlayerStat.GAMES_PLAYED, 1);
                p.saveProfile();

                p.getPlayer().getInventory().clear();
                p.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }

        // Schedule sending everyone to the lobby
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            // Copy the set and use the copy to avoid a ConcurrentModificationException
            Set<PlayerProfile> playersCopy = new HashSet<>(players);
            playersCopy.forEach((playerProfile -> {
                Player player = playerProfile.getPlayer();
                PowerUp.clearEffects(playerProfile.getPlayer());
                player.teleport(game.getLobbySpawn());
                player.setGameMode(Main.defaultGamemode);
                player.sendMessage(Main.prefix + "You have been sent to the lobby.");
                game.removePlayer(playerProfile);
            }));
        }, 100);

        // Game type specific stuff
        if (game instanceof TeamGame) {
            // List top teams with the most kills
            List<Team> topTeams = ((TeamGame) game).sortByKills();
            for (int i = 0; i < 3; i++) {
                if (i < topTeams.size()) {
                    String name = topTeams.get(i).getName();
                    int kills = topTeams.get(i).getKills();
                    for (PlayerProfile pInGame : game.getAllPlayers()) {
                        if (i == 0) { // top of the board
                            pInGame.getPlayer().sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Leaderboard");
                        }
                        pInGame.getPlayer().sendMessage(ChatColor.GREEN + " #" + ChatColor.BOLD + (i + 1) + ChatColor.RESET + " " + name + ChatColor.GOLD + " (" + kills + " hits)");
                    }
                } else {
                    break;
                }
            }
        } else {
            // List top 3 killers
            List<PlayerProfile> topKills = ((PaintballGame) game).sortByStat(PlayerInGameStat.KILLS);
            for (int i = 0; i < 3; i++) {
                if (i < topKills.size()) {
                    String name = topKills.get(i).getPlayer().getName();
                    int kills = topKills.get(i).getCurrentGameStats().getStat(PlayerInGameStat.KILLS);
                    for (PlayerProfile pInGame : game.getAllPlayers()) {
                        if (i == 0) { // top of the board
                            pInGame.getPlayer().sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Leaderboard");
                        }
                        pInGame.getPlayer().sendMessage(ChatColor.GREEN + " #" + ChatColor.BOLD + (i + 1) + ChatColor.RESET + " " + name + ChatColor.GOLD + " (" + kills + " hits)");
                    }
                } else {
                    break;
                }
            }
        }

        // Remove powerups from map
        for (Location loc : game.getArena().getPowerUpSpawnPoints()) {
            if (PowerUp.isPowerUpBlock(loc.getBlock())) {
                PowerUp.getPowerUpBlock(loc.getBlock()).removePowerUp(loc);
            }
        }
    }
}
