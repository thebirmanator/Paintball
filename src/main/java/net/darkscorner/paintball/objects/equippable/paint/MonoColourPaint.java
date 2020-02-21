package net.darkscorner.paintball.objects.equippable.paint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

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
        ConfigurationSection monoSection = paintConfig.getConfigurationSection("mono-paints");
        Set<String> paintNames = monoSection.getKeys(false);
        for (String paintName : paintNames) {
            ConfigurationSection paintSection = monoSection.getConfigurationSection(paintName);
            Material displayIcon = Material.getMaterial(paintSection.getString("display-icon", "STONE"));
            Material paint = Material.getMaterial(paintSection.getString("paint", "STONE"));
            new MonoColourPaint(paintName, displayIcon, paint);
        }
    }
}
