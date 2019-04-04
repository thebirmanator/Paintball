package net.darkscorner.paintball.commands;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.Paint;
import net.darkscorner.paintball.objects.guns.Gun;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.menus.menuitems.EquipGunItem;
import net.darkscorner.paintball.objects.menus.menuitems.EquipPaintItem;
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
                GamePlayer gp = GamePlayer.getGamePlayer(player);
                if(!gp.isInGame()) {
                    Menu menu = new Menu("Guns", null, 9);
                    ItemStack icon = new ItemStack(Material.AIR);
                    int index = 0;
                    for(Gun gun : Gun.getGuns()) {
                        String permission = "paintball.gun." + gun.getType().name().toLowerCase();
                        if(player.hasPermission(permission)) {
                            icon = new ItemStack(gun.getItem());
                            ItemMeta meta = icon.getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + gun.getType().name() /*WordUtils.capitalize(friendlyMaterial)*/);
                            List<String> lore = new ArrayList<String>();

                            if(gp.getGun().equals(gun)) { // player has this paint equipped already
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
                            lore.add(ChatColor.GREEN + gun.getType().name()/*WordUtils.capitalize(friendlyMaterial)*/);
                            lore.add(ChatColor.GRAY + "Unlock using " + ChatColor.WHITE + "/store");
                            meta.setLore(lore);
                            icon.setItemMeta(meta);
                        }
                        EquipGunItem paintItem = new EquipGunItem(null, icon, gun);
                        menu.addButton(index, paintItem);
                        index++;
                    }
                    menu.openMenu(player);
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