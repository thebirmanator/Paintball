package ltd.indigostudios.paintball.objects.menus.arena.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.arena.ArenaEditorMenu;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class NameArenaEditor extends ArenaEditorItem {

    public static String attrMeta = "name";
    private static ItemStack templateItem;

    private Main main;

    public NameArenaEditor(ArenaEditorMenu editorMenu) {
        super(editorMenu);
        main = Main.getPlugin(Main.class);
    }

    @Override
    public void use(Player player, ClickType clickType) {
        if (!player.hasMetadata(editingMeta)) {
            player.setMetadata(editingMeta, new FixedMetadataValue(main, attrMeta));
            player.sendMessage(Main.prefix + "Type the new display name in chat.");

            new BukkitRunnable() {
                int seconds = 0;

                @Override
                public void run() {
                    if (!player.hasMetadata(editingMeta)) { // no longer waiting to type a name
                        cancel();
                    } else if (seconds >= 10) { // ran out of time
                        cancel();
                        player.removeMetadata(editingMeta, main);
                        player.sendMessage(Main.prefix + "Name editing timed out. Using the previously set name.");
                    } else {
                        seconds++;
                    }
                }
            }.runTaskTimer(main, 0, 20);
        } else {
            player.sendMessage(Main.prefix + "Please type \"cancel\" to stop your previous edit.");
        }
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = templateItem;
        return this;
    }

    @Override
    public void createTemplate() {
        templateItem = new ItemEditor(Material.NAME_TAG, Text.format("&dArena Name"))
                .addAction(ClickType.LEFT, "to change the name, or")
                .addAction(ClickType.RIGHT, "to change the name.")
                .getItemStack();
    }

}
