package net.darkscorner.paintball.objects.menus.equipment.items;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EquipGunItem extends EquipmentItem {

    private Gun gun;
    private static ItemStack templateItem;

    public EquipGunItem(Gun gun, Menu owningMenu) {
        super(owningMenu);
        this.gun = gun;
    }

    @Override
    boolean hasPermission(Player player) {
        return player.hasPermission("paintball.gun." + gun.getType().name().toLowerCase());
    }

    @Override
    boolean hasEquipped(Player player) {
        return PlayerProfile.getGamePlayer(player).getGun().equals(gun);
    }

    @Override
    ItemStack getNoPermsItem() {
        return templateItem;
    }

    @Override
    ItemStack getAvailableItem() {
        Material material = gun.getItem().getType();
        return new ItemEditor(material, Text.format("&a&l" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.LEFT, "to equip.")
                .getItemStack();
    }

    @Override
    public void use(Player player, ClickType click) {
        if (hasPermission(player)) {
            PlayerProfile profile = PlayerProfile.getGamePlayer(player);
            if (hasEquipped(player)) {
                profile.setGun(Gun.getDefault());
                player.sendMessage(Main.prefix + "Reset gun!");
            } else {
                profile.setGun(gun);
                player.sendMessage(Main.prefix + "Set gun!");
            }
            getOwningMenu().close(player, true);
        } else {
            player.sendMessage(Main.prefix + "This gun is " + ChatColor.RED + "locked" + ChatColor.GRAY + " for you.");
        }
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = getNoPermsItem();
        if (hasPermission(player)) {
            if (hasEquipped(player)) {
                playerItem = getEquippedItem();
            } else {
                playerItem = getAvailableItem();
            }
        }
        return this;
    }

    @Override
    protected void createTemplate() {
        templateItem = new ItemEditor(Material.GRAY_DYE, Text.format("&cLOCKED"))
                .addAction(ClickType.UNKNOWN, Text.format("&a" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.UNKNOWN, Text.format("Unlock using &f/store"))
                .getItemStack();
    }
//TODO: integrate equippable stuff into the menu system
    /*
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

    }*/
}
