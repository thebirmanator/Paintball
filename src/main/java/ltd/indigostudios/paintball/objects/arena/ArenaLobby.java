package ltd.indigostudios.paintball.objects.arena;

import ltd.indigostudios.paintball.utils.Locations;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class ArenaLobby {
    private ConfigurationSection config;
    private Arena owningArena;
    private Location[] boundaries;
    private Vector[] minMaxVectors;
    private Location spawnLocation;

    protected ArenaLobby(Arena owningArena, ConfigurationSection config) {
        this.owningArena = owningArena;
        this.config = config;
    }

    /*
    public ArenaLobby(Arena owningArena, Location[] boundaries) {
        this.owningArena = owningArena;
        this.boundaries = boundaries;
    }*/

    public Arena getOwningArena() {
        return owningArena;
    }

    public Location getSpawnLocation() {
        if (spawnLocation == null) {
            spawnLocation = Locations.stringToLoc(config.getString("spawn"));
        }
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

    //TODO: move to lobby settings interface?
    public Location[] getBoundaries() {
        if (boundaries == null) {
            boundaries = new Location[2];
            List<String> locStrings = config.getStringList("boundaries");
            for (int i = 0; i < boundaries.length; i++) {
                boundaries[i] = Locations.stringToLoc(locStrings.get(i));
            }
        }
        return boundaries;
    }

    public boolean isInLobby(Player player) {
        Location location = player.getLocation();
        Vector minVector = new Vector(0, 0, 0);
        Vector maxVector = new Vector(0, 0, 0);
        if (minMaxVectors == null) {
            minMaxVectors = new Vector[2];
            Location bound0 = getBoundaries()[0];
            Location bound1 = getBoundaries()[1];
            // x
            //TODO: clean this up?
            if (bound0.getBlockX() < bound1.getBlockX()) {
                minVector.setX(bound0.getBlockX());
                maxVector.setX(bound1.getBlockX());
            } else {
                minVector.setX(bound1.getBlockX());
                maxVector.setX(bound0.getBlockX());
            }
// y
            if (bound0.getBlockY() < bound1.getBlockY()) {
                minVector.setY(bound0.getBlockY());
                maxVector.setY(bound1.getBlockY());
            } else {
                minVector.setY(bound1.getBlockY());
                maxVector.setY(bound0.getBlockY());
            }
// z
            if (bound0.getBlockZ() < bound1.getBlockZ()) {
                minVector.setZ(bound0.getBlockZ());
                maxVector.setZ(bound1.getBlockZ());
            } else {
                minVector.setZ(bound1.getBlockZ());
                maxVector.setZ(bound0.getBlockZ());
            }
            minMaxVectors[0] = minVector;
            minMaxVectors[1] = maxVector;
        }
        return location.toVector().isInAABB(minMaxVectors[0], minMaxVectors[1]);
    }
}
