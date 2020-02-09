package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.utils.Vectors;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public abstract class Gun {
    private ItemStack item;

    private static Set<Gun> guns = new HashSet<>();

    protected Vector shotVector = new Vector(0, 0, 0);
    private static Gun defaultGun;

    public Gun(ItemStack item) {
        this.item = item;

        guns.add(this);
    }

    public ItemStack getItem() {
        return item;
    }

    public void shoot(Player from) {
        from.launchProjectile(Snowball.class, shotVector);
        if (from.hasMetadata("volleypowerup")) {
            shootVolley(from);
        }
    }

    void shootVolley(Player from) {
        Vector old = shotVector;
        Vector[] vectors = Vectors.getVolleyVectors(from);
        for (Vector vector : vectors) {
            shotVector = vector;
            shoot(from);
        }
        shotVector = old;
    }

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
