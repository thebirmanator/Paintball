package ltd.indigostudios.paintball.utils;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor {

    private ItemStack itemStack;
    private ItemMeta meta;

    public ItemEditor(Material material, String displayName) {
        itemStack = new ItemStack(material);
        meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
    }

    public ItemEditor(ItemStack itemStack) {
        this.itemStack = itemStack;
        meta = itemStack.getItemMeta();
    }

    public ItemEditor setMaterial(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemEditor setDisplayName(String displayName) {
        meta.setDisplayName(displayName);
        return this;
    }

    public ItemEditor addAction(ClickType clickType, String action) {
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(clickToString(clickType) + action);
        meta.setLore(lore);
        return this;
    }

    public ItemStack getItemStack() {
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private String clickToString(ClickType clickType) {
        switch (clickType) {
            case LEFT:
                return Text.format("&fLeft-click &7");
            case RIGHT:
                return Text.format("&fRight-click &7");
            case MIDDLE:
                return Text.format("&fMiddle-click &7");
            case UNKNOWN:
                return Text.format("&7");
            default:
                return Text.format("&fUnknown click &7");
        }
    }
}
