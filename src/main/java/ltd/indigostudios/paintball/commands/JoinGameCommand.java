package ltd.indigostudios.paintball.commands;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.games.GameState;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class JoinGameCommand implements CommandExecutor {

    private Main main;

    public JoinGameCommand(Main main) {
        this.main = main;
    }

    public String join = "join";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("paintball.command.join")) {
                PlayerProfile gp = PlayerProfile.getGamePlayer(player);
                //gp.playSound(SoundEffect.RUN_COMMAND);
                if (!gp.isInGame()) {
                    Set<GameSettings> games = GameSettings.allGames;
                    for (GameSettings game : games) {
                        if ((game.getGameState() == GameState.IDLE || game.getGameState() == GameState.COUNTDOWN) && game.getPlayers(true).size() < game.getMaxPlayerAmount()) {
                            player.sendMessage(Main.prefix + "You have " + ChatColor.GREEN + "joined" + ChatColor.GRAY + " the game!");
                            game.addPlayer(gp, false);
                            return true;
                        }
                    }

                    // all games are full or going, create a new one
                    GameSettings game = GameSettings.createGame();
                    if (game != null) {
                        game.addPlayer(gp, false);
                        player.sendMessage(Main.prefix + "You have " + ChatColor.GREEN + "joined" + ChatColor.GRAY + " the game!");
                    } else {
                        // all arenas are full, tell player to wait
                        player.sendMessage(Main.prefix + "All games are " + ChatColor.RED + "full" + ChatColor.GRAY + ", please wait a few moments and try again.");
                    }
                    return true;
                } else {
                    player.sendMessage(Main.prefix + "You are already in a game.");
                    return true;
                }
            } else {
                player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to join a game.");
                return true;
            }
        } else {
            sender.sendMessage(Main.prefix + "Sorry, only " + ChatColor.RED + "players" + ChatColor.GRAY + " can use this command.");
            return true;
        }
    }

}
