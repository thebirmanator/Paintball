package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class SniperGun extends Gun {

    private static SniperGun instance;
    private String cooldownMeta = "sniperCooldown";

    public SniperGun(ItemStack item) {
        super(item);
    }

    @Override
    public void shoot(Player from) {
        if (!from.hasMetadata(cooldownMeta)) {
            super.shoot(from);
            from.setMetadata(cooldownMeta, new FixedMetadataValue(Main.getInstance(), true));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                if (from.hasMetadata(cooldownMeta)) {
                    from.removeMetadata(cooldownMeta, Main.getInstance());
                }
            }, 20);
        }
    }

    @Override
    Vector getShotVelocity(Player from) {
        return from.getEyeLocation().getDirection().normalize().multiply(2);
    }

    static SniperGun getInstance() {
        if (instance == null) {
            ItemStack gun = new ItemEditor(Material.DIAMOND_HOE, Text.format("&a&lPAINTBALL GUN"))
                    .addAction(ClickType.UNKNOWN, "Sniper: fast shots")
                    .addAction(ClickType.RIGHT, "to shoot!")
                    .getItemStack();
            instance = new SniperGun(gun);
        }
        return instance;
    }
}
