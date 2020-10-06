package ltd.indigostudios.paintball.objects.menus.shop.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.equippable.paint.Paint;
import ltd.indigostudios.paintball.objects.menus.shop.ShopMenu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BuyPaintItem extends ShopItem {

    private static ItemStack templateItem;
    private static ConfigurationSection config;
    private Paint paint;

    public BuyPaintItem(Paint paint, ShopMenu owningMenu) {
        super(owningMenu, config.getInt(paint.getName() + ".price", 0));
        this.paint = paint;
    }

    protected BuyPaintItem() {
        super(null, 0);
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
                .addAction(ClickType.UNKNOWN, Text.format("&cYou do not have enough money to buy this!"))
                .addAction(ClickType.UNKNOWN, Text.format("&7You need &a$" + (getPrice() - playerProfile.getMoney()) + " &7more."))
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
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + paint.getPermission() + " true server=lobby");
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
        config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "shop.yml")).getConfigurationSection("paints.items");
        templateItem = new ItemEditor(Material.GRAY_DYE, Text.format("&cLOCKED"))
                .addAction(ClickType.UNKNOWN, Text.format("You do not have enough money for this!"))
                .getItemStack();
    }
}
