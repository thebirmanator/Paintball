package ltd.indigostudios.paintball.utils;

import ltd.indigostudios.paintball.Main;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Locations {

    public static Location stringToLoc(String locationString) {
        String[] parameters = locationString.split("\\|");
        World world = Main.getInstance().getServer().getWorld(parameters[0]);
        int[] coords = new int[5];
        for (int i = 1; i < parameters.length; i++) {
            coords[i - 1] = NumberUtils.createInteger(parameters[i]);
        }
        return new Location(world, coords[0], coords[1], coords[2], coords[3], coords[4]);
    }

    public static String locToString(Location location) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return world + "|" + x + "|" + y + "|" + z + "|" + pitch + "|" + yaw;
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
