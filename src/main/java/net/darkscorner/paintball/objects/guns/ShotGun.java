package net.darkscorner.paintball.objects.guns;

import net.darkscorner.paintball.GunType;
import net.darkscorner.paintball.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ShotGun extends Gun {

    public static String metaCooldown = "cooldown";

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

        from.setMetadata(metaCooldown, new FixedMetadataValue(Main.getPlugin(Main.class), true));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                if(from.hasMetadata(metaCooldown)) {
                    from.removeMetadata(metaCooldown, Main.getPlugin(Main.class));
                }
            }
        }, 20);
    }
}
