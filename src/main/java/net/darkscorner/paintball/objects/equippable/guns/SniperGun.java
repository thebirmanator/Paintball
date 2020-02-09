package net.darkscorner.paintball.objects.equippable.guns;

import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SniperGun extends Gun {

    private static SniperGun instance;

    public SniperGun(ItemStack item) {
        super(item);
        shotVector = shotVector.multiply(2);
    }

    /*
    @Override
    public void shoot(Player from) {
        if(velocity.equals(defaultVector)) {
            Projectile projectile = from.launchProjectile(Snowball.class);
            projectile.setVelocity(projectile.getVelocity().multiply(2));
        } else {
            velocity = velocity.multiply(2);
            from.launchProjectile(Snowball.class, velocity);
        }
    }*/

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
