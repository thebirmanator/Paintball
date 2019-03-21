package net.darkscorner.paintball.objects.guns;

import net.darkscorner.paintball.GunType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShotGun extends Gun {

    public ShotGun(ItemStack item, GunType type) {
        super(item, type);
    }

    @Override
    public void shoot(Player from, Vector velocity) {
        for(int i = 0; i < 5; i++) {
            if(velocity.equals(defaultVector)) {
                from.launchProjectile(Snowball.class);
            } else {
                from.launchProjectile(Snowball.class, velocity);
            }
        }
    }
}
