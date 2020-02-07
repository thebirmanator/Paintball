package net.darkscorner.paintball.objects.menus;

import net.darkscorner.paintball.objects.menus.game.menuitems.BackMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableItem {

    protected ItemStack playerItem;

    protected ClickableItem() {

    }

    public abstract void use(Player player, ClickType click);

    public abstract ClickableItem getForPlayer(Player player);

    public abstract void createTemplate();

    public ItemStack getItemStack() {
        return playerItem;
    }

    //TODO: load template items so it doesnt throw npe's on menu open
    public static void loadItems() {
        new BackMenuItem().createTemplate();
    }
}
