package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MachineGun extends Gun {

    private static MachineGun instance;

    private MachineGun(ItemStack item) {
        super(item);
    }

    @Override
    public void shoot(Player from, Vector velocity) {
        new BukkitRunnable() {
            int times = 0;
            @Override
            public void run() {
                if(from.getGameMode() != GameMode.SPECTATOR) {
                    if (times < 3) {
                        if (velocity.equals(defaultVector)) {
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

    static MachineGun getInstance() {
        if (instance == null) {
            ItemStack gun = new ItemEditor(Material.IRON_HOE, Text.format("&a&lPAINTBALL GUN"))
                    .addAction(ClickType.UNKNOWN, "Machine gun: rapid fire")
                    .addAction(ClickType.RIGHT, "to shoot!")
                    .getItemStack();
            instance = new MachineGun(gun);
        }
        return instance;
    }
}
