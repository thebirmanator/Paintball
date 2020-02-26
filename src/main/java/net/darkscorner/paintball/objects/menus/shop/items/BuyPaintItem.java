package net.darkscorner.paintball.objects.menus.shop.items;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.equippable.paint.Paint;
import net.darkscorner.paintball.objects.menus.shop.ShopMenu;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BuyPaintItem extends ShopItem {

    private static ItemStack templateItem;
    private Paint paint;

    public BuyPaintItem(Paint paint, ShopMenu owningMenu, int price) {
        super(owningMenu, price);
        this.paint = paint;
    }

    boolean hasBought(Player player) {
        return player.hasPermission(paint.getPermission());
    }

    ItemStack getHasBoughtItem() {
        return new ItemEditor(paint.getDisplayIcon(), Text.format("&a&l" + Text.friendlyEnum(paint.getName())))
                .addAction(ClickType.UNKNOWN, "You already own this paint.")
                .getItemStack();
    }

    ItemStack getNotEnoughMoneyItem(PlayerProfile playerProfile) {
        return new ItemEditor(paint.getDisplayIcon(), Text.format("&a&l" + Text.friendlyEnum(paint.getName())))
                .addAction(ClickType.UNKNOWN, Text.format("&6Price: &c$" + getPrice()))
                .addAction(ClickType.UNKNOWN, "")
                .addAction(ClickType.UNKNOWN, Text.format("&cYou do not have enough money to buy this! &f(" + (getPrice() - playerProfile.getMoney() + ")")))
                .getItemStack();
    }

    @Override
    ItemStack getAvailableItem() {
        Material material = paint.getDisplayIcon();
        return new ItemEditor(material, Text.format("&a&l" + Text.friendlyEnum(paint.getName())))
                .addAction(ClickType.UNKNOWN, Text.format("&6Price: &a$" + getPrice()))
                .addAction(ClickType.UNKNOWN, "")
                .addAction(ClickType.LEFT, "to buy.")
                .getItemStack();
    }

    @Override
    public void use(Player player, ClickType click) {
        PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
        if (!hasBought(player)) {
            if (hasEnoughMoney(playerProfile)) {
                player.sendMessage(Text.format("You've bought paint " + paint.getName()));
                playerProfile.subtractMoney(getPrice());
                //TODO: configurable or something
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + paint.getPermission());
                getOwningMenu().close(player, true);
                playerProfile.saveProfile();
            } else {
                player.sendMessage(Text.format("&cYou do not have enough money for this paint."));
            }
        } else {
            player.sendMessage(Text.format("&7You already own this paint!"));
        }
    }

    @Override
    protected void createTemplate() {
        templateItem = new ItemEditor(Material.GRAY_DYE, Text.format("&cLOCKED"))
                .addAction(ClickType.UNKNOWN, Text.format("You do not have enough money for this!"))
                .getItemStack();
    }
}
