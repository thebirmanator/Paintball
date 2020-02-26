package net.darkscorner.paintball.objects.menus.shop.items;

import net.darkscorner.paintball.objects.equippable.paint.Paint;
import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.menus.shop.ShopMenu;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BuyPaintCategory extends ClickableItem {

    private static ItemStack templateItem;

    public BuyPaintCategory(Menu owningMenu) {
        super(owningMenu);
    }

    @Override
    public void use(Player player, ClickType click) {
        ShopMenu paintShop = new ShopMenu("Paint", 54);
        int index = 0;
        for (Paint paint : Paint.getAllCustomPaints()) {
            paintShop.addItem(index, new BuyPaintItem(paint, paintShop, 10).getForPlayer(player));
            index++;
        }
        paintShop.open(player);
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = templateItem;
        return this;
    }

    @Override
    protected void createTemplate() {
        templateItem = new ItemEditor(Material.PURPLE_WOOL, Text.format("&d&lPaints"))
                .addAction(ClickType.UNKNOWN, "Buy different paints to shoot in game!")
                .getItemStack();
    }
}
