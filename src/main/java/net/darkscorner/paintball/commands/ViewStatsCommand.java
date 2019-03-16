package net.darkscorner.paintball.commands;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.PlayerStat;
import net.darkscorner.paintball.objects.GamePlayer;
import org.apache.commons.lang.WordUtils;
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

                    if(Bukkit.getPlayer(arg1) != null || Bukkit.getOfflinePlayer(arg1) != null) { // put a player as the first argument
                        if(player.hasPermission("paintball.command.viewstats.others")) {
                            GamePlayer target;
                            if(Bukkit.getPlayer(arg1) != null) {
                                target = GamePlayer.getGamePlayer(Bukkit.getPlayer(arg1));
                            } else {
                                target = GamePlayer.getGamePlayer(Bukkit.getOfflinePlayer(arg1));
                            }
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
                                } else { // unrecognised second argument, send help
                                    player.sendMessage(Main.prefix + "Help for /viewstats:");
                                    player.sendMessage(ChatColor.DARK_GREEN + " /viewstats" + ChatColor.GREEN + ": View your own overview stats");
                                    player.sendMessage(ChatColor.DARK_GREEN + " /viewstats <playername>" + ChatColor.GREEN + ": View another player's overview stats");
                                    player.sendMessage(ChatColor.DARK_GREEN + " /viewstats <shots|kills|deaths|games>" + ChatColor.GREEN + ": View your own specific stat");
                                    player.sendMessage(ChatColor.DARK_GREEN + " /viewstats <playername> <shots|kills|deaths|games>" + ChatColor.GREEN + ": View another player's specific stat");
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
                        } else { // unrecognised first argument, send help
                            player.sendMessage(Main.prefix + "Help for /viewstats:");
                            player.sendMessage(ChatColor.DARK_GREEN + " /viewstats" + ChatColor.GREEN + ": View your own overview stats");
                            player.sendMessage(ChatColor.DARK_GREEN + " /viewstats <playername>" + ChatColor.GREEN + ": View another player's overview stats");
                            player.sendMessage(ChatColor.DARK_GREEN + " /viewstats <shots|kills|deaths|games>" + ChatColor.GREEN + ": View your own specific stat");
                            player.sendMessage(ChatColor.DARK_GREEN + " /viewstats <playername> <shots|kills|deaths|games>" + ChatColor.GREEN + ": View another player's specific stat");
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
        String title = "Overview";
        String[] lines = new String[5];
        if(stat != null) { // is not the overview stats page
            // format the stat name to look nice
            title = stat.toString();
            title = WordUtils.capitalize(title.toLowerCase());

            // display top 5 in the stat
            String[] leaders = new String[6];
            leaders[0] = ChatColor.GREEN + "" + ChatColor.BOLD + "Top 5 for " + title;

            int leadersLength = 5;
            if(GamePlayer.getOrderedByStat(stat).size() < leadersLength) {
                leadersLength = GamePlayer.getOrderedByStat(stat).size();
            }

            for(int i = 0; i < leadersLength; i++) {
                GamePlayer gp = GamePlayer.getOrderedByStat(stat).get(i);
                String name = gp.getOfflinePlayer().getName();
                long amount = gp.getTotal(stat);

                leaders[i+1] = ChatColor.DARK_GREEN + name + ChatColor.DARK_GRAY + ": " + ChatColor.GREEN + amount;
            }
            sendTo.sendMessage(leaders);

            // format text for personal ranking
            lines[1] = ChatColor.GOLD + "  " + title + ": " + ChatColor.YELLOW + player.getTotal(stat);
            lines[2] = ChatColor.GOLD + "  Ranking: " + ChatColor.YELLOW + player.getRanking(stat) + "/" + GamePlayer.getTotalGamePlayers();
        } else {
            for(int i = 0; i < values().length; i++) {
                String statName = PlayerStat.values()[i].toString();
                statName = WordUtils.capitalize(statName.toLowerCase());
                lines[i+1] = ChatColor.GOLD + "  " + statName + ": " + ChatColor.YELLOW + player.getTotal(PlayerStat.values()[i]);
            }
        }
        lines[0] = ChatColor.GOLD + "" + ChatColor.BOLD + player.getOfflinePlayer().getName() + "'s stats: " + ChatColor.YELLOW + "" + ChatColor.BOLD + title;

        sendTo.sendMessage(lines);

    }
}
