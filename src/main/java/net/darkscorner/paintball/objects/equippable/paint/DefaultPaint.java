package net.darkscorner.paintball.objects.equippable.paint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Random;

public class DefaultPaint extends Paint {

    private Material[] materials = {Material.REDSTONE_BLOCK, Material.COAL_BLOCK, Material.GOLD_BLOCK,
    Material.QUARTZ_BLOCK, Material.LAPIS_BLOCK, Material.PINK_CONCRETE};

    private static Random random = new Random();

    public DefaultPaint(String name, Material displayIcon, List<Material> paintMaterials) {
        super(name, displayIcon, paintMaterials);
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
}
