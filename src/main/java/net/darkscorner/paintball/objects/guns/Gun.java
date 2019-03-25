package net.darkscorner.paintball.objects.guns;

import net.darkscorner.paintball.GunType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public abstract class Gun {
    private ItemStack item;
    private GunType type;

    private static Set<Gun> guns = new HashSet<>();

    public static Vector defaultVector = new Vector(0, 0, 0);
    private static Gun defaultGun;

    public Gun(ItemStack item, GunType type) {
        this.item = item;
        this.type = type;

        guns.add(this);
    }

    public ItemStack getItem() {
        return item;
    }

    public GunType getType() {
        return type;
    }

    public abstract void shoot(Player from, Vector velocity);

    public void giveTo(Player player) {
        player.getInventory().setItem(0, item);
    }

    public void removeFrom(Player player) {
        player.getInventory().remove(item);
    }

    public static Gun getDefault() {
        return defaultGun;
    }

    public void setDefault() {
        defaultGun = this;
    }

    public static boolean isGun(ItemStack item) {
        if(getGun(item) != null) {
            return true;
        }
        return false;
    }

    public static Gun getGun(ItemStack item) {
        for(Gun gun : guns) {
            if(gun.getItem().isSimilar(item)) {
                return gun;
            }
        }
        return null;
    }
}
