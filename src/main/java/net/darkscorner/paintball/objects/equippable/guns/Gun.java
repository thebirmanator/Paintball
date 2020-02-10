package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.powerups.VolleyPowerUp;
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

    //protected Vector shotVector = new Vector(0, 0, 0);
    //TODO: find out why this gives a null error thats weird (InitialiserError or something like that)
    private static Gun defaultGun = Type.STANDARD.getGun();

    public Gun(ItemStack item) {
        this.item = item;

        guns.add(this);
    }

    public ItemStack getItem() {
        return item;
    }

    public void shoot(Player from) {
        if (VolleyPowerUp.hasPowerUp(from)) {
            for (Vector velocity : Vectors.getVolleyVectors(from, getShotVelocity(from))) {
                from.launchProjectile(Snowball.class, velocity);
                PlayerProfile.getGamePlayer(from).getCurrentGameStats().addToStat(PlayerInGameStat.SHOTS, 1);
            }
        } else {
            from.launchProjectile(Snowball.class, getShotVelocity(from));
            PlayerProfile.getGamePlayer(from).getCurrentGameStats().addToStat(PlayerInGameStat.SHOTS, 1);
        }
    }

    Vector getShotVelocity(Player from) {
        return from.getEyeLocation().getDirection().normalize();
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

    public Type getType() {
        for (Type type : Type.values()) {
            if (type.getGun().equals(this)) {
                return type;
            }
        }
        return null;
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

    public enum Type {

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
