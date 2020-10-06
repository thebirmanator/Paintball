package ltd.indigostudios.paintball.objects.menus.shop.items;

import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.shop.ShopMenu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.entity.Player;
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
        new BuyPaintItem().createTemplate();
        new BuyGunItem().createTemplate();
        new BuyGunCategory(null).createTemplate();
        new BuyPaintCategory(null).createTemplate();
    }
}
