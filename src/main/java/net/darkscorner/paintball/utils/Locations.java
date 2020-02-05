package net.darkscorner.paintball.utils;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Locations {

    public static Location stringToLoc(String locationString) {
        String[] parameters = locationString.split("\\|");
        World world = null;
        int[] coords = new int[5];
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                world = Bukkit.getWorld(parameters[0]);
            } else {
                coords[i - 1] = NumberUtils.createInteger(parameters[i]);
            }
        }
        return new Location(world, coords[0], coords[1], coords[2], coords[3], coords[4]);
    }

    public static String locToString(Location location) {

    }

    public static void locToConfig(FileConfiguration config, String configPath, Location loc) {
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        config.set(configPath + ".world", world);
        config.set(configPath + ".x", x);
        config.set(configPath + ".y", y);
        config.set(configPath + ".z", z);
        config.set(configPath + ".yaw", yaw);
        config.set(configPath + ".pitch", pitch);
    }
}
