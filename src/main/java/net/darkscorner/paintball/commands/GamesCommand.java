package net.darkscorner.paintball.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.menus.GameMenu;
import net.darkscorner.paintball.objects.menus.menuitems.GameItem;
import net.darkscorner.paintball.objects.menus.menuitems.GameMenuItem;

public class GamesCommand implements CommandExecutor {

	public String games = "games";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// command layout: /game <PaintballGame> spectate|players|options <kick(players) playername> or endgame(options)
		if(sender instanceof Player) {
			Player player = (Player) sender;
			GamePlayer gp = GamePlayer.getGamePlayer(player);
			gp.playSound(SoundEffect.RUN_COMMAND);
			if(player.hasPermission("paintball.command.games")) {
				GameMenu mainGameMenu = new GameMenu("Games", null, 27);
				int index = 0;
				for(Game game : Game.getGames()) {
					
					ItemStack icon = new ItemStack(game.getUsedArena().getMaterial());
					ItemMeta meta = icon.getItemMeta();
					meta.setDisplayName(game.getUsedArena().getName());
					List<String> lore = new ArrayList<String>();
					lore.add(ChatColor.WHITE + "Left-click" + ChatColor.GRAY + " to spectate this game.");
					lore.add(ChatColor.WHITE + "Right-click" + ChatColor.GRAY + " to view in-game players.");
					if(player.hasPermission("paintball.options.use")) {
						lore.add(ChatColor.WHITE + "Middle-click" + ChatColor.GRAY + " to view options for this game.");
					}
					meta.setLore(lore);
					icon.setItemMeta(meta);
					GameItem gameItem = new GameItem(icon, game);
					mainGameMenu.addButton(index, gameItem);
					index++;
				}
				if(args.length == 0) {
					mainGameMenu.openMenu(player);
				} else {
					String arenaName = args[0];
					for(Game g : Game.getGames()) {
						if(g.getUsedArena().getSimpleName().equalsIgnoreCase(arenaName)) {
							if(args.length > 1) {
								String option = args[1];
								GameMenuItem gameMenuItem = null;
								for(GameMenuItem mi : mainGameMenu.getMenuItems()) {
									if(mi.getAssociatedGame().equals(g)) {
										gameMenuItem = mi;
									}
								}
								if(gameMenuItem != null) {
									switch (option) {
									case "spectate":
										gameMenuItem.open(player, ClickType.LEFT);
										break;
									case "players":
										if(args.length > 2) {
											if(args[2].equalsIgnoreCase("kick")) {
												if(player.hasPermission("paintball.options.players")) {
													if (args.length > 3 && Bukkit.getPlayer(args[3]) != null) { // kick a player from the game
														GamePlayer victim = GamePlayer.getGamePlayer(Bukkit.getPlayer(args[3]));
														if (victim.isInGame() && victim.getCurrentGame().equals(g)) {
															victim.getCurrentGame().removePlayer(victim);
														} else {
															player.sendMessage(Main.prefix + "That player is not in this game.");
														}
													} else {
														player.sendMessage(Main.prefix + "Please give a valid playername.");
													}
												} else {
													player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to kick players from a game.");
												}
											} else { // open player menu
												gameMenuItem.open(player, ClickType.RIGHT);
											}
										} else { // open player menu
											gameMenuItem.open(player, ClickType.RIGHT);
										}
										break;
									case "options":
										if(player.hasPermission("paintball.options.use")) {
											if (args.length > 2) {
												if (args[2].equalsIgnoreCase("endgame")) {
													g.endGame();
												} else { // open options menu
													gameMenuItem.open(player, ClickType.MIDDLE);
												}
											} else {
												gameMenuItem.open(player, ClickType.MIDDLE);
											}
										} else {
											player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to do that.");
										}
										break;
									default:
										player.sendMessage(Main.prefix + "Help for /games:");
										player.sendMessage(ChatColor.DARK_GREEN + " /games" + ChatColor.GREEN + ": Opens the games menu");
										player.sendMessage(ChatColor.DARK_GREEN + " /games <arena> spectate" + ChatColor.GREEN + ": Spectate the game associated with that arena");
										if(player.hasPermission("paintball.options.players")) {
											player.sendMessage(ChatColor.DARK_GREEN + " /games <arena> players kick <playername>" + ChatColor.GREEN + ": Kicks player from their game");
										}
										if(player.hasPermission("paintball.options.use")) {
											player.sendMessage(ChatColor.DARK_GREEN + " /games <arena> options endgame" + ChatColor.GREEN + ": Ends the game associated with that arena");
										}
										break;
									}
								}
							} else {
								player.sendMessage(Main.prefix + "Help for /games:");
								player.sendMessage(ChatColor.DARK_GREEN + " /games" + ChatColor.GREEN + ": Opens the games menu");
								player.sendMessage(ChatColor.DARK_GREEN + " /games <arena> spectate" + ChatColor.GREEN + ": Spectate the game associated with that arena");
								if(player.hasPermission("paintball.options.players")) {
									player.sendMessage(ChatColor.DARK_GREEN + " /games <arena> players kick <playername>" + ChatColor.GREEN + ": Kicks player from their game");
								}
								if(player.hasPermission("paintball.options.use")) {
									player.sendMessage(ChatColor.DARK_GREEN + " /games <arena> options endgame" + ChatColor.GREEN + ": Ends the game associated with that arena");
								}
							}
						}
						return true;
					}
					// did not find a game with the inputed arena name
					player.sendMessage(Main.prefix + arenaName + " is not being used right now or does not exist.");
				}
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
