package net.darkscorner.paintball.objects.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableItem {

    protected ItemStack playerItem;

    public abstract void use(Player player, ClickType click);

    public abstract ClickableItem getForPlayer(Player player);

    public abstract void createTemplate();

    public ItemStack getItemStack() {
        return playerItem;
    }
}
