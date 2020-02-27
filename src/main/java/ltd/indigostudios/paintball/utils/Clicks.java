package ltd.indigostudios.paintball.utils;

import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;

public class Clicks {

    public static ClickType asClickType(Action action) {
        if (action.name().contains("LEFT_CLICK")) {
            return ClickType.LEFT;
        }

        if (action.name().contains("RIGHT_CLICK")) {
            return ClickType.RIGHT;
        }
        return ClickType.UNKNOWN;
    }
}
