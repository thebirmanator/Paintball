package ltd.indigostudios.paintball.objects.arena;

import ltd.indigostudios.paintball.utils.Locations;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public interface ArenaSetting {

    FileConfiguration getConfig();

    default String getName() {
        return Text.format(getConfig().getString("display-name"));
    }

    default String getSimpleName() {
        return ChatColor.stripColor(getName());
    }

    default String getCreator() {
        return getConfig().getString("creator");
    }

    default List<Location> getFreeForAllSpawnPoints() {
        List<Location> locations = new ArrayList<>();
        getConfig().getStringList("game-spawnpoints").forEach((locString) -> {
            locations.add(Locations.stringToLoc(locString));
        });
        return locations;
    }

    default Location getSpectatingPoint() {
        String locString = getConfig().getString("spectating-spawnpoint");
        return Locations.stringToLoc(locString);
    }

    default void setSpectatingPoint(Location loc) {
        getConfig().set("spectating-spawnpoint", Locations.locToString(loc));
    }

    default Material getMaterial() {
        return Material.getMaterial(getConfig().getString("item-for-guis"));
    }

    /*
    default Location getLobbyLocation() {
        String locString = getConfig().getString("waiting-lobby");
        return Locations.stringToLoc(locString);
    }

    default void setLobbyLocation(Location loc) {
        getConfig().set("waiting-lobby", Locations.locToString(loc));
    }*/
    ArenaLobby getLobby();

    default void setMaterial(Material material) {
        getConfig().set("item-for-guis", material.name());
    }

    default List<Location> getPowerUpSpawnPoints() {
        List<Location> locations = new ArrayList<>();
        getConfig().getStringList("powerup-spawnpoints").forEach((locString) -> {
            locations.add(Locations.stringToLoc(locString));
        });
        return locations;
    }

    default Location[] getBoundaries() {
        Location[] boundaries = new Location[2];
        List<String> locStrings = getConfig().getStringList("boundaries");
        for (int i = 0; i < boundaries.length; i++) {
            boundaries[i] = Locations.stringToLoc(locStrings.get(i));
        }
        return boundaries;
    }

    default boolean allowsTeams() {
        return getConfig().getBoolean("allows-teams", false);
    }
}
