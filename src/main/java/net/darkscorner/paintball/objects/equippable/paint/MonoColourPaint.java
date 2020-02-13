package net.darkscorner.paintball.objects.equippable.paint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class MonoColourPaint extends Paint {

    private Material paintColour;

    public MonoColourPaint(String name, Material displayIcon, Material paintColour) {
        super(name, displayIcon);
        this.paintColour = paintColour;
    }

    @Override
    public void paintTile(Location location) {
        Bukkit.getOnlinePlayers().forEach((player) -> player.sendBlockChange(location, Bukkit.createBlockData(paintColour)));
    }

    @Override
    public void removePaint(Location location) {
        Bukkit.getOnlinePlayers().forEach((player) -> player.sendBlockChange(location, location.getBlock().getBlockData()));
    }

    public static void loadMonoPaints() {
        customPaints.add(new MonoColourPaint("blue", Material.BLUE_WOOL, Material.BLUE_WOOL));
        customPaints.add(new MonoColourPaint("orange", Material.ORANGE_WOOL, Material.ORANGE_WOOL));
    }
}
