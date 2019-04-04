package net.darkscorner.paintball.objects.menus.menuitems;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.guns.Gun;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class EquipGunItem extends MenuItem {

    private Gun gun;
    public EquipGunItem(MenuItem parent, ItemStack icon, Gun gun) {
        super(parent, icon);

        this.gun = gun;
    }

    @Override
    public void open(Player player, ClickType click) {
        if(getIcon().getType() != Material.LIGHT_GRAY_DYE) { // did not click on a locked dye
            if(getIcon().getEnchantments().isEmpty()) {
                GamePlayer.getGamePlayer(player).setGun(gun);
                player.sendMessage(Main.prefix + "Set gun!");
            } else {
                GamePlayer.getGamePlayer(player).setGun(Gun.getDefault());
                player.sendMessage(Main.prefix + "Reset gun.");
            }
        } else {
            player.sendMessage(Main.prefix + "This gun is " + ChatColor.RED + "locked" + ChatColor.GRAY + " for you.");
        }
        getContainedIn().closeMenu(player);

    }
}
