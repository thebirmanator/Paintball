package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ShotGun extends Gun {

    public static String metaCooldown = "cooldown";
    private static ShotGun instance;

    private ShotGun(ItemStack item) {
        super(item);
    }

    @Override
    public void shoot(Player from) {
        for (int i = 0; i < 5; i++) {
            /*
            if(velocity.equals(defaultVector)) {
                from.launchProjectile(Snowball.class);
            } else {
                from.launchProjectile(Snowball.class, velocity);
            }*/
            super.shoot(from);
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

    static ShotGun getInstance() {
        if (instance == null) {
            ItemStack gun = new ItemEditor(Material.STONE_HOE, Text.format("&a&lPAINTBALL GUN"))
                    .addAction(ClickType.UNKNOWN, "Shotgun: cluster shots")
                    .addAction(ClickType.RIGHT, "to shoot!")
                    .getItemStack();
            instance = new ShotGun(gun);
        }
        return instance;
    }
}
