package net.darkscorner.paintball.utils;

import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Vectors {

    // thanks to blablubbabc on the forums for this crazy maths
    // https://bukkit.org/threads/multiple-arrows-with-vectors.177643/
    public static Vector[] getVolleyVectors(Player from) {
        int[] angles = {20, 10, 0, -10, -20};

        Location pLoc = from.getLocation();
        Vector pDir = from.getEyeLocation().getDirection();

        // set vector to length 1
        pDir.normalize();

        // vector in the viewer's direction
        Vector dirY = (new Location(pLoc.getWorld(), 0, 0, 0, pLoc.getYaw(), 0)).getDirection().normalize();

        Vector[] vectors = new Vector[angles.length];
        for (int i = 0; i < angles.length; i++) {
            Vector velocity = pDir.clone();
            int angle = angles[i];
            if (angle != 0) {
                velocity = rotateY(dirY, angle);
                velocity.multiply(Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ())).subtract(dirY);
                velocity = pDir.clone().add(velocity).normalize();
            }
            vectors[i] = velocity;
        }
        return vectors;
    }

    private static Vector rotateY(Vector direction, double angleDirection) {
        double angleRotate = Math.toRadians(angleDirection);
        double x = direction.getX();
        double z = direction.getZ();

        double cos = Math.cos(angleRotate);
        double sin = Math.sin(angleRotate);

        return (new Vector(x*cos+z*(-sin), 0.0, x*sin+z*cos)).normalize();
    }
}
