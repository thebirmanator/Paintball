package ltd.indigostudios.paintball.objects.menus.equipment.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.equippable.guns.Gun;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.Menu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class EquipGunItem extends EquipmentItem {

    private Gun gun;
    private static ItemStack templateItem;

    public EquipGunItem(Gun gun, Menu owningMenu) {
        super(owningMenu);
        this.gun = gun;
    }

    @Override
    boolean hasPermission(Player player) {
        return player.hasPermission(gun.getPermission());
    }

    @Override
    boolean hasEquipped(Player player) {
        return PlayerProfile.getGamePlayer(player).getGun().equals(gun);
    }

    @Override
    ItemStack getNoPermsItem() {
        return new ItemEditor(templateItem.getType(), templateItem.getItemMeta().getDisplayName())
                .addAction(ClickType.UNKNOWN, Text.format("&a" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.UNKNOWN, Text.format("Unlock using &f/store"))
                .getItemStack();
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
                //.addAction(ClickType.UNKNOWN, Text.format("&a" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.UNKNOWN, Text.format("Unlock using &f/store"))
                .getItemStack();
    }
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
