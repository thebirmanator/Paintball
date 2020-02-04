package net.darkscorner.paintball.objects.menus.equipment;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.guns.Gun;
import net.darkscorner.paintball.objects.menus.game.menuitems.GameMenuItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class EquipGunItem extends GameMenuItem {
//TODO: integrate equippable stuff into the menu system
    private Gun gun;
    public EquipGunItem(GameMenuItem parent, ItemStack icon, Gun gun) {
        super(parent, icon);

        this.gun = gun;
    }

    @Override
    public void open(Player player, ClickType click) {
        if(getIcon().getType() != Material.LIGHT_GRAY_DYE) { // did not click on a locked dye
            if(getIcon().getEnchantments().isEmpty()) {
                PlayerProfile.getGamePlayer(player).setGun(gun);
                player.sendMessage(Main.prefix + "Set gun!");
            } else {
                PlayerProfile.getGamePlayer(player).setGun(Gun.getDefault());
                player.sendMessage(Main.prefix + "Reset gun.");
            }
        } else {
            player.sendMessage(Main.prefix + "This gun is " + ChatColor.RED + "locked" + ChatColor.GRAY + " for you.");
        }
        getContainedIn().closeMenu(player);

    }
}
