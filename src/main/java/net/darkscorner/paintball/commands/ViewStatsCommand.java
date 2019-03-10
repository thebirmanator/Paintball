package net.darkscorner.paintball.commands;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewStatsCommand implements CommandExecutor {

    public String viewstats = "viewstats";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("paintball.command.viewstats") || player.hasPermission("paintball.command.viewstats.others")) {
                if(args.length == 0) { // most basic command; show user their own "Overview" stats
                    GamePlayer gp = GamePlayer.getGamePlayer(player);
                    sendStatsMessage(player, gp, "Overview");
                    return true;
                } else {
                    String arg1 = args[0];
                    if(Bukkit.getPlayer(arg1) != null) { // put a player as the first argument
                        GamePlayer target = GamePlayer.getGamePlayer(Bukkit.getPlayer(arg1));
                        sendStatsMessage(player, target, "Overview");
                        return true;
                    } else {
                        player.sendMessage(Main.prefix + "That's not a valid player!");
                        return true;
                    }

                }
            } else {
                player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to view stats.");
                return true;
            }
        } else {
            sender.sendMessage(Main.prefix + "Sorry, only " + ChatColor.RED + "players" + ChatColor.GRAY + " can use this command.");
            return true;
        }
        return false;
    }

    private void sendStatsMessage(Player sendTo, GamePlayer player, String type) {
        sendTo.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getPlayer().getName() + "'s stats: " + ChatColor.YELLOW + "" + ChatColor.BOLD + type);
        switch (type) {
            case "Overview":
                sendTo.sendMessage(ChatColor.GOLD + "  Shots: " + ChatColor.YELLOW + player.getTotalShots());
                sendTo.sendMessage(ChatColor.GOLD + "  Hits: " + ChatColor.YELLOW + player.getTotalHits());
                sendTo.sendMessage(ChatColor.GOLD + "  Deaths: " + ChatColor.YELLOW + player.getTotalDeaths());
                sendTo.sendMessage(ChatColor.GOLD + "  Games: " + ChatColor.YELLOW + player.getTotalGamesPlayed());
                break;
            default:
                sendTo.sendMessage("invalid type sent");
                break;
        }
    }
}
