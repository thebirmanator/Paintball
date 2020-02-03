package net.darkscorner.paintball.objects.menus;

import net.darkscorner.paintball.objects.menus.menuitems.GameMenuItem;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

    private Map<Integer, GameMenuItem> items = new HashMap<>();

    public void addItem(int slot, GameMenuItem gameMenuItem) {
        items.put(slot, gameMenuItem);
    }

    public GameMenuItem getMenuItem(int slot) {
        return items.get(slot);
    }

    public boolean hasMenuItem(int slot) {
        return getMenuItem(slot) != null;
    }

    public abstract void open(Player player);

    protected Map<Integer, GameMenuItem> getItems() {
        return items;
    }
}
