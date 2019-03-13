package net.darkscorner.paintball.commands;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.PlayerStat;
import net.darkscorner.paintball.objects.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.darkscorner.paintball.PlayerStat.*;

public class ViewStatsCommand implements CommandExecutor {

    public String viewstats = "viewstats";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("paintball.command.viewstats") || player.hasPermission("paintball.command.viewstats.others")) {
                GamePlayer gp = GamePlayer.getGamePlayer(player);
                if(args.length == 0) { // most basic command; show user their own "Overview" stats
                    sendStatsMessage(player, gp, null);
                    return true;
                } else {
                    String arg1 = args[0];

                    if(Bukkit.getPlayer(arg1) != null) { // put a player as the first argument
                        if(player.hasPermission("paintball.command.viewstats.others")) {
                            GamePlayer target = GamePlayer.getGamePlayer(Bukkit.getPlayer(arg1));
                            if(args.length == 1) { // only one argument; must want overview stats
                                sendStatsMessage(player, target, null);
                                return true;
                            } else {
                                String arg2 = args[1];
                                if(arg2.equalsIgnoreCase("shots")) {
                                    sendStatsMessage(player, target, SHOTS);
                                } else if(arg2.equalsIgnoreCase("kills")) {
                                    sendStatsMessage(player, target, KILLS);
                                } else if(arg2.equalsIgnoreCase("deaths")) {
                                    sendStatsMessage(player, target, DEATHS);
                                } else if(arg2.equalsIgnoreCase("games")) {
                                    sendStatsMessage(player, target, GAMES);
                                } else { // unrecognised second argument, send overview
                                    sendStatsMessage(player, target, null);
                                }
                                return true;
                            }
                        } else {
                            player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to view others' stats.");
                            return true;
                        }
                    } else { // playername is not the first argument
                        if(arg1.equalsIgnoreCase("shots")) {
                            sendStatsMessage(player, gp, SHOTS);
                        } else if(arg1.equalsIgnoreCase("kills")) {
                            sendStatsMessage(player, gp, KILLS);
                        } else if(arg1.equalsIgnoreCase("deaths")) {
                            sendStatsMessage(player, gp, DEATHS);
                        } else if(arg1.equalsIgnoreCase("games")) {
                            sendStatsMessage(player, gp, GAMES);
                        } else { // unrecognised first argument, send overview
                            sendStatsMessage(player, gp, null);
                        }
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
    }

    private void sendStatsMessage(Player sendTo, GamePlayer player, PlayerStat stat) {
        String title = "overview";
        String[] lines = new String[5];
        if(stat != null) {
            title = stat.toString();
            switch (stat) {
                case SHOTS:
                    lines[1] = ChatColor.GOLD + "  Shots: " + ChatColor.YELLOW + player.getTotalShots();
                    break;
                case KILLS:
                    lines[1] = ChatColor.GOLD + "  Kills: " + ChatColor.YELLOW + player.getTotalHits();
                    break;
                case DEATHS:
                    lines[1] = ChatColor.GOLD + "  Deaths: " + ChatColor.YELLOW + player.getTotalDeaths();
                    break;
                case GAMES:
                    lines[1] = ChatColor.GOLD + "  Games: " + ChatColor.YELLOW + player.getTotalGamesPlayed();
                    break;
                default:
                    break;
            }
            lines[2] = ChatColor.GOLD + "  Ranking: " + ChatColor.YELLOW + player.getRanking(stat) + "/" + GamePlayer.getTotalGamePlayers();
        } else {
            lines[1] = ChatColor.GOLD + "  Shots: " + ChatColor.YELLOW + player.getTotalShots();
            lines[2] = ChatColor.GOLD + "  Hits: " + ChatColor.YELLOW + player.getTotalHits();
            lines[3] = ChatColor.GOLD + "  Deaths: " + ChatColor.YELLOW + player.getTotalDeaths();
            lines[4] = ChatColor.GOLD + "  Games: " + ChatColor.YELLOW + player.getTotalGamesPlayed();
            return;
        }
        lines[0] = ChatColor.GOLD + "" + ChatColor.BOLD + player.getPlayer().getName() + "'s stats: " + ChatColor.YELLOW + "" + ChatColor.BOLD + title;

        sendTo.sendMessage(lines);

    }
}
