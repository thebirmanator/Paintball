package net.darkscorner.paintball.objects.guns;

import net.darkscorner.paintball.GunType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class StandardGun extends Gun {

    public StandardGun(ItemStack item, GunType type) {
        super(item, type);
    }

    @Override
    public void shoot(Player from, Vector velocity) {
        if(velocity.equals(Gun.defaultVector)) {
            from.launchProjectile(Snowball.class);
        } else {
            from.launchProjectile(Snowball.class, velocity);
        }
    }
}
