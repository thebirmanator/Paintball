package net.darkscorner.paintball.utils;

import org.bukkit.ChatColor;

public class Text {

    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
