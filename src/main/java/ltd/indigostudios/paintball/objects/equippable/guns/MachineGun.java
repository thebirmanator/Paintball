package ltd.indigostudios.paintball.objects.equippable.guns;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MachineGun extends Gun {

    private static MachineGun instance;

    private MachineGun(ItemStack item) {
        super(item);
    }

    @Override
    public void shoot(Player from) {
        new BukkitRunnable() {
            int times = 0;

            @Override
            public void run() {
                if (from.getGameMode() != GameMode.SPECTATOR) {
                    if (times < 3) {
                        MachineGun.super.shoot(from);
                        times++;
                    } else {
                        cancel();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 2, 2);
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
