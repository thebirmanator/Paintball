package ltd.indigostudios.paintball.objects.menus.game.items;

import ltd.indigostudios.paintball.objects.menus.ChestBasedMenu;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BackMenuItem extends ClickableItem {

    private ChestBasedMenu backMenu;
    private static ItemStack templateItem;

    public BackMenuItem(ChestBasedMenu parent, ChestBasedMenu backMenu) {
        super(parent);
        this.backMenu = backMenu;
    }

    @Override
    public void use(Player player, ClickType click) {
        backMenu.open(player);
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = templateItem;
        return this;
    }

    @Override
    public void createTemplate() {
        templateItem = new ItemEditor(Material.BIRCH_DOOR, Text.format("&cGo Back")).getItemStack();
    }
}
