package net.darkscorner.paintball.objects.menus.shop.items;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.menus.shop.ShopMenu;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BuyGunItem extends ShopItem {

    private Gun gun;

    private ItemStack templateItem;

    public BuyGunItem(Gun gun, ShopMenu owningMenu, int price) {
        super(owningMenu, price);
        this.gun = gun;
    }

    @Override
    boolean hasBought(Player player) {
        return player.hasPermission(gun.getPermission());
    }

    @Override
    ItemStack getHasBoughtItem() {
        return new ItemEditor(gun.getItem().getType(), Text.format("&a&l" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.UNKNOWN, "You already own this paint.")
                .getItemStack();
    }

    @Override
    ItemStack getAvailableItem() {
        Material material = gun.getItem().getType();
        return new ItemEditor(material, Text.format("&a&l" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.UNKNOWN, Text.format("&6Price: &a$" + getPrice()))
                .addAction(ClickType.UNKNOWN, "")
                .addAction(ClickType.LEFT, "to buy.")
                .getItemStack();
    }

    @Override
    ItemStack getNotEnoughMoneyItem(PlayerProfile playerProfile) {
        return new ItemEditor(gun.getItem().getType(), Text.format("&a&l" + Text.friendlyEnum(gun.getType().name())))
                .addAction(ClickType.UNKNOWN, Text.format("&6Price: &c$" + getPrice()))
                .addAction(ClickType.UNKNOWN, "")
                .addAction(ClickType.UNKNOWN, Text.format("&cYou do not have enough money to buy this! &f(" + (getPrice() - playerProfile.getMoney() + ")")))
                .getItemStack();
    }

    @Override
    public void use(Player player, ClickType click) {
        PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
        if (!hasBought(player)) {
            if (hasEnoughMoney(playerProfile)) {
                player.sendMessage(Text.format(Main.prefix + "You've bought gun " + Text.friendlyEnum(gun.getType().name())));
                playerProfile.subtractMoney(getPrice());
                //TODO: configurable or something
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + gun.getPermission());
                getOwningMenu().close(player, true);
                playerProfile.saveProfile();
            } else {
                player.sendMessage(Text.format(Main.prefix + "&cYou do not have enough money for this gun."));
            }
        } else {
            player.sendMessage(Text.format(Main.prefix + "&7You already own this gun!"));
        }
    }

    @Override
    protected void createTemplate() {
        templateItem = new ItemEditor(Material.GRAY_DYE, Text.format("&cLOCKED"))
                .addAction(ClickType.UNKNOWN, Text.format(Main.prefix + "You do not have enough money for this!"))
                .getItemStack();
    }
}
