package ltd.indigostudios.paintball.objects.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class ChestBasedMenu extends Menu {

    private String name;
    private int size;
    private ChestBasedMenu parent;

    public ChestBasedMenu(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public ChestBasedMenu(String name, ChestBasedMenu parent, int size) {
        this.name = name;
        this.size = size;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public ChestBasedMenu getParent() {
        return parent;
    }

    @Override
    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(player, size, name);

        for (Integer key : getItems().keySet()) {
            ClickableItem item = getItems().get(key);
            inv.setItem(key, item.getForPlayer(player).getItemStack());
        }

        player.openInventory(inv);
        viewing.put(player, this);

        //PlayerProfile.getGamePlayer(player).setViewingGameMenu(this);
    }

    @Override
    public void close(Player player) {
        viewing.remove(player);
        //PlayerProfile.getGamePlayer(player).setViewingGameMenu(null);
    }

    @Override
    public void close(Player player, boolean forceInv) {
        if (forceInv) {
            player.closeInventory();
        }
        close(player);
    }
}
