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

    static StandardGun getInstance() {
        if (instance == null) {
            ItemStack gun = new ItemEditor(Material.GOLDEN_HOE, Text.format("&a&lPAINTBALL GUN"))
                    .addAction(ClickType.UNKNOWN, "Standard issue")
                    .addAction(ClickType.RIGHT, "to shoot!")
                    .getItemStack();
            instance = new StandardGun(gun);
        }
        return instance;
    }
}
