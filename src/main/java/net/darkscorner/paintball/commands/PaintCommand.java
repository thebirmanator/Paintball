package net.darkscorner.paintball.commands;

import java.util.ArrayList;
import java.util.List;

import net.darkscorner.paintball.GameState;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.Paint;
import net.darkscorner.paintball.objects.menus.GameMenu;
import net.darkscorner.paintball.objects.menus.menuitems.EquipPaintItem;

public class PaintCommand implements CommandExecutor {

	//public static List<Material> paints = new ArrayList<Material>();
	
	public String paint = "paint";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("paintball.command.paint")) {
				GamePlayer gp = GamePlayer.getGamePlayer(player);
				if(!gp.isInGame() || gp.getCurrentGame().getGameState() == GameState.IDLE || gp.getCurrentGame().getGameState() == GameState.COUNTDOWN) {
					GameMenu gameMenu = new GameMenu("Paints", null, 54);
					ItemStack icon = new ItemStack(Material.AIR);
					int index = 0;
					for(Paint paint : Paint.getAllCustomPaints()) {
						String permission = "paintball.paintcolour." + paint.getName();
						String friendlyName = paint.getName().toLowerCase();
						friendlyName = friendlyName.replaceAll("\\-", " ");
						friendlyName = WordUtils.capitalize(friendlyName);
						if(player.hasPermission(permission)) {
							icon = new ItemStack(paint.getDisplayIcon());
							ItemMeta meta = icon.getItemMeta();
							meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + friendlyName /*WordUtils.capitalize(friendlyMaterial)*/);
							List<String> lore = new ArrayList<String>();

							if(gp.getPaint().equals(paint)) { // player has this paint equipped already
								lore.add(ChatColor.WHITE + "Click" + ChatColor.GRAY + " to unequip.");
								meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
							} else {
								lore.add(ChatColor.WHITE + "Click" + ChatColor.GRAY + " to equip.");
							}

							meta.setLore(lore);
							icon.setItemMeta(meta);
						} else {
							icon = new ItemStack(Material.GRAY_DYE);
							ItemMeta meta = icon.getItemMeta();
							meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "LOCKED");
							List<String> lore = new ArrayList<String>();
							lore.add(ChatColor.GREEN + friendlyName/*WordUtils.capitalize(friendlyMaterial)*/);
							lore.add(ChatColor.GRAY + "Unlock using " + ChatColor.WHITE + "/store");
							meta.setLore(lore);
							icon.setItemMeta(meta);
						}
						EquipPaintItem paintItem = new EquipPaintItem(null, icon, paint);
						gameMenu.addButton(index, paintItem);
						index++;
					}
					gameMenu.openMenu(player);
					return true;
				} else {
					player.sendMessage(Main.prefix + "You may not use this command in a game.");
					return true;
				}
			} else {
				player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to view paints.");
				return true;
			}
		} else {
			sender.sendMessage(Main.prefix + "Sorry, only " + ChatColor.RED + "players" + ChatColor.GRAY + " can use this command.");
			return true;
		}
	}

}
