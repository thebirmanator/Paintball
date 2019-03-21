package net.darkscorner.paintball.objects.guns;

import net.darkscorner.paintball.GunType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SniperGun extends Gun {

    public SniperGun(ItemStack item, GunType type) {
        super(item, type);
    }

    @Override
    public void shoot(Player from, Vector velocity) {
        if(velocity.equals(defaultVector)) {
            Projectile projectile = from.launchProjectile(Snowball.class);
            projectile.setVelocity(projectile.getVelocity().multiply(2));
        } else {
            velocity = velocity.multiply(2);
            from.launchProjectile(Snowball.class, velocity);
        }
    }
}
