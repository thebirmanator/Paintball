package net.darkscorner.paintball.objects.menus.shop.items;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.equippable.paint.Paint;
import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.shop.ShopMenu;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ShopItem extends ClickableItem {

    private int price;

    public ShopItem(ShopMenu owningMenu, int price) {
        super(owningMenu);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    abstract boolean hasBought(Player player);

    boolean hasEnoughMoney(PlayerProfile playerProfile) {
        return playerProfile.getMoney() >= price;
    }

    abstract ItemStack getHasBoughtItem();

    abstract ItemStack getAvailableItem();

    abstract ItemStack getNotEnoughMoneyItem(PlayerProfile playerProfile);

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = getHasBoughtItem();
        if (!hasBought(player)) {
            PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
            if (hasEnoughMoney(playerProfile)) {
                playerItem = getAvailableItem();
            } else {
                playerItem = getNotEnoughMoneyItem(playerProfile);
            }
        }
        return this;
    }

    public static void loadShopItems() {
        new BuyPaintItem(null, null, 0).createTemplate();
        new BuyGunItem(null, null, 0).createTemplate();
        new BuyGunCategory(null).createTemplate();
        new BuyPaintCategory(null).createTemplate();
    }
}
