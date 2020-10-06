package ltd.indigostudios.paintball.commands;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.menus.game.GameMenu;
import ltd.indigostudios.paintball.objects.menus.game.items.GameItem;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.SoundEffect;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamesCommand implements CommandExecutor {

    public String games = "games";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerProfile gp = PlayerProfile.getGamePlayer(player);
            gp.playSound(SoundEffect.RUN_COMMAND);
            if (player.hasPermission("paintball.command.games")) {
                GameMenu mainGameMenu = new GameMenu("Games", null, 27);
                int index = 0;
                for (GameSettings game : GameSettings.allGames) {
                    mainGameMenu.addItem(index, new GameItem(mainGameMenu, game));
                    index++;
                }
                mainGameMenu.open(player);
                return true;
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
