package net.darkscorner.paintball.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.Arena;
import net.darkscorner.paintball.objects.player.PlayerProfile;

import java.util.ArrayList;
import java.util.List;

public class ArenaEditCommand implements CommandExecutor {

	private Main main;
	public ArenaEditCommand(Main main) {
		this.main = main;
	}
	
	public String arena = "arena";
	public static String editMeta = "editArena";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player) sender;
			PlayerProfile gp = PlayerProfile.getGamePlayer(player);
			gp.playSound(SoundEffect.RUN_COMMAND);
			if(player.hasPermission("paintball.arena.edit")) {
				if(args.length == 0) {
					if(!player.hasMetadata(editMeta)) {
						player.setMetadata(editMeta, new FixedMetadataValue(main, ""));
						player.sendMessage(Main.prefix + "Breaking blocks has been " + ChatColor.GREEN + "enabled" + ChatColor.GRAY + "!");
						return true;
					} else {
						player.removeMetadata(editMeta, main);
						player.sendMessage(Main.prefix + "Breaking blocks has been " + ChatColor.RED + "disabled" + ChatColor.GRAY + "!");
						return true;
					}
				} else {
					String arg1 = args[0];
					if(args.length >= 2) {
						String arenaName = args[1];
						switch (arg1) {
						case "create": // wants to create an arena
							String coloured = ChatColor.translateAlternateColorCodes('&', arenaName);
							Location defaultLoc = player.getLocation();
							List<Location> defaultLocations = new ArrayList<Location>();
							defaultLocations.add(defaultLoc);
							new Arena(coloured, defaultLocations, defaultLoc, defaultLocations, defaultLoc, Material.STONE, player.getName());
							player.sendMessage(Main.prefix + "Successfully created arena " + coloured);
							player.sendMessage(Main.prefix + "Make sure to set spawnpoints!");
							return true;
						case "delete": // wants to delete an arena
							if(getArena(arenaName) != null) { // the arena exists
								String realName = getArena(arenaName).getName();
								getArena(arenaName).remove();
								player.sendMessage(Main.prefix + "Sucessfully deleted arena " + realName);
							} else {
								player.sendMessage(Main.prefix + arenaName + " does not exist!");
							}
							return true;
						case "edit": // wants to edit an arena
							if(getArena(arenaName) != null) { // the arena exists
								player.setMetadata(editMeta, new FixedMetadataValue(main, getArena(arenaName).getName()));
								getArena(arenaName).giveEditKit(player);
								player.sendMessage(Main.prefix + "Now editing arena " + getArena(arenaName).getName());
							} else {
								player.sendMessage(Main.prefix + arenaName + " does not exist!");
							}
							return true;
						default:
							return true;
						}
					} else { // forgot an argument, send help message
						player.sendMessage("helpful help msg here");
						return true;
					}
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
	
	private Arena getArena(String simpleName) {
		for(Arena a : Arena.getArenas()) {
			if(a.getSimpleName().equalsIgnoreCase(simpleName)) {
				return a;
			}
		}
		return null;
	}
}
