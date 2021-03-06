package ltd.indigostudios.paintball.commands;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.SoundEffect;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameCommand implements CommandExecutor {

    public String leave = "leave";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerProfile gp = PlayerProfile.getGamePlayer(player);
            gp.playSound(SoundEffect.RUN_COMMAND);
            if (player.hasPermission("paintball.command.leave")) {
                if (gp.isInGame()) {
                    player.sendMessage(Main.prefix + "You have " + ChatColor.RED + "left" + ChatColor.GRAY + " the game!");
                    gp.getCurrentGame().removePlayer(gp);
                    return true;
                } else {
                    player.sendMessage(Main.prefix + "You are not in a game.");
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
