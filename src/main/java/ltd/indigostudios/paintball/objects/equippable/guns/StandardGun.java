package ltd.indigostudios.paintball.objects.equippable.guns;

import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
