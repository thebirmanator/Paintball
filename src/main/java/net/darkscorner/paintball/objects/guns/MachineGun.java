package net.darkscorner.paintball.objects.guns;

import net.darkscorner.paintball.GunType;
import net.darkscorner.paintball.Main;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MachineGun extends Gun {

    public MachineGun(ItemStack item, GunType type) {
        super(item, type);
    }

    @Override
    public void shoot(Player from, Vector velocity) {
        new BukkitRunnable() {
            int times = 0;
            @Override
            public void run() {
                if(from.getGameMode() != GameMode.SPECTATOR) {
                    if (times < 3) {
                        if (velocity.equals(Gun.defaultVector)) {
                            from.launchProjectile(Snowball.class);
                        } else {
                            from.launchProjectile(Snowball.class, velocity);
                        }
                        times++;
                    } else {
                        cancel();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 2, 2);
    }
}
