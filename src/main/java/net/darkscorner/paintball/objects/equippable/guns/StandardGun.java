package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class StandardGun extends Gun {

    private static StandardGun instance;

    public StandardGun(ItemStack item) {
        super(item);
    }

    @Override
    public void shoot(Player from, Vector velocity) {
        if(velocity.equals(Gun.defaultVector)) {
            from.launchProjectile(Snowball.class);
        } else {
            from.launchProjectile(Snowball.class, velocity);
        }
    }

    static StandardGun getInstance() {
        if (instance == null) {
            ItemStack gun = new ItemEditor(Material.DIAMOND_HOE, Text.format("&a&lPAINTBALL GUN"))
                    .addAction(ClickType.UNKNOWN, "Sniper: fast shots")
                    .addAction(ClickType.RIGHT, "to shoot!")
                    .getItemStack();
            instance = new StandardGun(gun);
        }
        return instance;
    }
}
