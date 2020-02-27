package ltd.indigostudios.paintball.objects.menus.shop.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.equippable.paint.Paint;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.Menu;
import ltd.indigostudios.paintball.objects.menus.shop.ShopMenu;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class BuyPaintCategory extends ClickableItem {

    private static ItemStack templateItem;

    public BuyPaintCategory(Menu owningMenu) {
        super(owningMenu);
    }

    @Override
    public void use(Player player, ClickType click) {
        ShopMenu paintShop = new ShopMenu("Paint", (ShopMenu) this.getOwningMenu(), 54);
        int index = 0;
        for (Paint paint : Paint.getAllCustomPaints()) {
            paintShop.addItem(index, new BuyPaintItem(paint, paintShop).getForPlayer(player));
            index++;
        }
        paintShop.showNavBar(true);
        paintShop.open(player);
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = templateItem;
        return this;
    }

    @Override
    protected void createTemplate() {
        ConfigurationSection config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "shop.yml"));
        ConfigurationSection itemSection = config.getConfigurationSection("paints.display-item");
        ItemEditor itemEditor = new ItemEditor(Material.getMaterial(itemSection.getString("material")), Text.format(itemSection.getString("display-name")));
        List<String> descriptionLines = itemSection.getStringList("description");
        descriptionLines.forEach((line) -> itemEditor.addAction(ClickType.UNKNOWN, Text.format(line)));
        templateItem = itemEditor.getItemStack();
    }
}
