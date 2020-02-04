package net.darkscorner.paintball.objects.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableItem {

    private ItemStack itemStack;

    public abstract void use(Player player, ClickType click);

    public abstract ClickableItem getForPlayer(Player player);

    public abstract void createItem();

    public ItemStack getItemStack() {
        return itemStack;
    }

    protected void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
