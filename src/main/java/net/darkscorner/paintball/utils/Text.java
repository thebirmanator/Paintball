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

    public static String formatTime(int time) {
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        String timeString = "";
        if(minutes > 0 ) {
            timeString = String.format("%02dm %02ds", minutes, seconds);
        } else {
            timeString = String.format("%02ds", seconds);
        }
        return timeString;
    }
}
