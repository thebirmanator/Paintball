package ltd.indigostudios.paintball.objects.equippable.paint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;

public class DefaultPaint extends Paint {

    private Material[] materials = {Material.REDSTONE_BLOCK, Material.COAL_BLOCK, Material.GOLD_BLOCK,
            Material.QUARTZ_BLOCK, Material.LAPIS_BLOCK, Material.PINK_CONCRETE};

    private Random random;

    private static DefaultPaint instance;

    private DefaultPaint(String name, Material displayIcon) {
        super(name, displayIcon);
        random = new Random();
    }

    @Override
    public void paintTile(Location location) {
        Material material = materials[random.nextInt(materials.length)];
        Bukkit.getOnlinePlayers().forEach((player) -> player.sendBlockChange(location, Bukkit.createBlockData(material)));
    }

    @Override
    public void removePaint(Location location) {
        Bukkit.getOnlinePlayers().forEach((player) -> player.sendBlockChange(location, location.getBlock().getBlockData()));
    }

    public static DefaultPaint getInstance() {
        if (instance == null) {
            instance = new DefaultPaint("default", Material.STONE);
        }
        return instance;
    }
}
