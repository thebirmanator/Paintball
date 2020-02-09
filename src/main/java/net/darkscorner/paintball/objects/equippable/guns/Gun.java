package net.darkscorner.paintball.objects.equippable.guns;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public abstract class Gun {
    private ItemStack item;

    private static Set<Gun> guns = new HashSet<>();

    public static Vector defaultVector = new Vector(0, 0, 0);
    private static Gun defaultGun;

    public Gun(ItemStack item) {
        this.item = item;

        guns.add(this);
    }

    public ItemStack getItem() {
        return item;
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
        return getGun(item) != null;
    }

    public static Set<Gun> getGuns() {
        return guns;
    }

    public static Gun getGun(ItemStack item) {
        for(Gun gun : guns) {
            if(gun.getItem().isSimilar(item)) {
                return gun;
            }
        }
        return null;
    }

    enum Type {

        STANDARD(StandardGun.getInstance()), SHOTGUN(ShotGun.getInstance()),
        MACHINE_GUN(MachineGun.getInstance()), SNIPER(SniperGun.getInstance());

        private final Gun gun;

        Type(Gun gun) {
            this.gun = gun;
        }

        public Gun getGun() {
            return gun;
        }
    }
}
