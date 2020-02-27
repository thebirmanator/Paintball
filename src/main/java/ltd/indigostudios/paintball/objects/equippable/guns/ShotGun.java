package ltd.indigostudios.paintball.objects.equippable.guns;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class ShotGun extends Gun {

    public static String metaCooldown = "cooldown";
    private static ShotGun instance;

    private ShotGun(ItemStack item) {
        super(item);
    }

    @Override
    public void shoot(Player from) {
        if (!from.hasMetadata(metaCooldown)) {
            for (int i = 0; i < 5; i++) {
                super.shoot(from);
            }
            from.setMetadata(metaCooldown, new FixedMetadataValue(Main.getInstance(), true));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                if (from.hasMetadata(metaCooldown)) {
                    from.removeMetadata(metaCooldown, Main.getInstance());
                }
            }, 20);
        }
    }

    @Override
    Vector getShotVelocity(Player from) {
        Vector velocity = super.getShotVelocity(from);
        double[] multipliers = new double[3];
        for (int i = 0; i < multipliers.length; i++) {
            multipliers[i] = ThreadLocalRandom.current().nextDouble(0.8, 1.2);
        }
        return velocity.setX(velocity.getX() * multipliers[0])
                .setY(velocity.getY() * multipliers[1])
                .setZ(velocity.getZ() * multipliers[2]);
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
