package net.darkscorner.paintball.objects.menus.equipment.items;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.utils.ItemEditor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public abstract class EquipmentItem extends ClickableItem {

    public EquipmentItem(Menu owningMenu) {
        super(owningMenu);
    }

    abstract boolean hasPermission(Player player);

    abstract boolean hasEquipped(Player player);

    abstract ItemStack getNoPermsItem();

    abstract ItemStack getAvailableItem();

    ItemStack getEquippedItem() {
        ItemStack item = getAvailableItem().clone();
        ItemMeta meta = getAvailableItem().getItemMeta();
        meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(new ArrayList<>());
        item.setItemMeta(meta);
        return new ItemEditor(item)
                .addAction(ClickType.LEFT, "to unequip.")
                .getItemStack();
    }
}
