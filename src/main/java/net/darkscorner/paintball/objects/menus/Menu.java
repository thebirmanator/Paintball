package net.darkscorner.paintball.objects.menus;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

    private Map<Integer, ClickableItem> items = new HashMap<>();

    public void addItem(int slot, ClickableItem clickableItem) {
        items.put(slot, clickableItem);
    }

    public ClickableItem getClickableItem(int slot) {
        return items.get(slot);
    }

    public boolean hasClickableItem(int slot) {
        return getClickableItem(slot) != null;
    }

    public abstract void open(Player player);

    public abstract void close(Player player);

    protected Map<Integer, ClickableItem> getItems() {
        return items;
    }
}
