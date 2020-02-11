package net.darkscorner.paintball.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

public class Text {

    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String friendlyEnum(String unfriendlyEnum) {
        String friendlyName = unfriendlyEnum.toLowerCase();
        friendlyName = friendlyName.replaceAll("_", " ");
        friendlyName = WordUtils.capitalize(friendlyName);
        return friendlyName;
    }
}
