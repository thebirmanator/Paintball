package net.darkscorner.paintball.objects.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class ClickableItem {
    //TODO: implement this into both the game items and the arena editors
    public abstract void use(Player player, ClickType click);
}
