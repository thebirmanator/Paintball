package net.darkscorner.paintball.commands;

import net.darkscorner.paintball.objects.games.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.menus.equipment.EquipmentMenu;
import net.darkscorner.paintball.objects.menus.equipment.items.EquipGunItem;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.menus.game.GameMenu;
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

import java.util.ArrayList;
import java.util.List;

public class GunCommand implements CommandExecutor {

    public String gun = "guns";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("paintball.command.guns")) {
                PlayerProfile gp = PlayerProfile.getGamePlayer(player);
                if(!gp.isInGame() || gp.getCurrentGame().getGameState() == GameState.IDLE || gp.getCurrentGame().getGameState() == GameState.COUNTDOWN) {
                    EquipmentMenu menu = new EquipmentMenu("Guns", 9);
                    int index = 0;
                    for(Gun gun : Gun.getGuns()) {
                        if(!gun.equals(Gun.getDefault())) {
                            menu.addItem(index, new EquipGunItem(gun, menu));
                            index++;
                            /*
                            String permission = "temp.perm";
                            //String permission = "paintball.gun." + gun.getType().name().toLowerCase();
                            //String friendlyName = gun.getType().name().toLowerCase();
                            //friendlyName = friendlyName.replaceAll("\\_", " ");
                            //friendlyName = WordUtils.capitalize(friendlyName);
                            if (player.hasPermission(permission) || player.hasPermission("paintball.gun.*")) {
                                icon = new ItemStack(gun.getItem());
                                ItemMeta meta = icon.getItemMeta();
                                //meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + friendlyName /*WordUtils.capitalize(friendlyMaterial));
                                List<String> lore = new ArrayList<String>();

                                if (gp.getGun().equals(gun)) { // player has this paint equipped already
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
                                //lore.add(ChatColor.GREEN + friendlyName/*WordUtils.capitalize(friendlyMaterial));
                                lore.add(ChatColor.GRAY + "Unlock using " + ChatColor.WHITE + "/store");
                                meta.setLore(lore);
                                icon.setItemMeta(meta);
                            }
                            //EquipGunItem paintItem = new EquipGunItem(null, icon, gun);
                            //gameMenu.addItem(index, paintItem);
                            index++;*/
                        }
                    }
                    menu.open(player);
                    return true;
                } else {
                    player.sendMessage(Main.prefix + "You may not use this command in a game.");
                    return true;
                }
            } else {
                player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to view guns.");
                return true;
            }
        } else {
            sender.sendMessage(Main.prefix + "Sorry, only " + ChatColor.RED + "players" + ChatColor.GRAY + " can use this command.");
            return true;
        }
    }
}
