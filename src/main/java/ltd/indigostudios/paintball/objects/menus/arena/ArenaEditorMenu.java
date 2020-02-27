package ltd.indigostudios.paintball.objects.menus.arena;

import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.objects.menus.arena.items.*;
import ltd.indigostudios.paintball.objects.menus.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ArenaEditorMenu extends Menu {

    private Arena arena;

    public ArenaEditorMenu(Arena arena) {
        this.arena = arena;
        addItem(0, new PowerupLocationArenaEditor(this));
        addItem(1, new SpawnpointsArenaEditor(this));
        addItem(3, new SpecPointArenaEditor(this));
        addItem(4, new LobbyArenaEditor(this));
        addItem(6, new NameArenaEditor(this));
        addItem(7, new CreatorArenaEditor(this));
        addItem(8, new DoneArenaEditor(this));
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public void open(Player player) {
        getItems().forEach((slot, item) -> player.getInventory().setItem(slot, item.getForPlayer(player).getItemStack()));
    }

    @Override
    public void close(Player player) {
        Set<Integer> slots = getItems().keySet();
        slots.forEach((slot) -> player.getInventory().setItem(slot, new ItemStack(Material.AIR)));
    }

    @Override
    public void close(Player player, boolean forceInv) {
        close(player);
    }

    @Override
    public void showNavBar(boolean show) {

    }
}
