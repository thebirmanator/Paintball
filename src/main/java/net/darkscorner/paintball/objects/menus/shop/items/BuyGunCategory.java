package net.darkscorner.paintball.objects.menus.shop.items;

import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.menus.shop.ShopMenu;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BuyGunCategory extends ClickableItem {

    private static ItemStack templateItem;

    public BuyGunCategory(Menu owningMenu) {
        super(owningMenu);
    }

    @Override
    public void use(Player player, ClickType click) {
        ShopMenu gunShop = new ShopMenu("Paint", 54);
        int index = 0;
        for (Gun gun : Gun.getGuns()) {
            if (gun.getType() != Gun.Type.STANDARD) {
                gunShop.addItem(index, new BuyGunItem(gun, gunShop, 10).getForPlayer(player));
                index++;
            }
        }
        gunShop.open(player);
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = templateItem;
        return this;
    }

    @Override
    protected void createTemplate() {
        templateItem = new ItemEditor(Material.GOLDEN_HOE, Text.format("&e&lGuns"))
                .addAction(ClickType.UNKNOWN, "Buy different guns to shoot in game!")
                .getItemStack();
    }
}
