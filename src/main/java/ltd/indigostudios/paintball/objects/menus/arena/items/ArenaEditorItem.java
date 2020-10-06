package ltd.indigostudios.paintball.objects.menus.arena.items;

import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.arena.ArenaEditorMenu;

public abstract class ArenaEditorItem extends ClickableItem {

    public static String editingMeta = "editAttr";

    public ArenaEditorItem(ArenaEditorMenu editorMenu) {
        super(editorMenu);
    }

    public Arena getArena() {
        return ((ArenaEditorMenu) getOwningMenu()).getArena();
    }
    //material/display/leftclick/rightclick
    //private ItemStack powerupEditor = getItem(Material.BEACON, ChatColor.BLUE + "Powerup Locations", "to add location.", "to remove location.");
    //private ItemStack spawnpointsEditor = getItem(Material.GRASS_BLOCK, ChatColor.GREEN + "Spawnpoint Locations", "to add location.", "to remove location.");

    //private ItemStack specPointEditor = getItem(Material.BEDROCK, ChatColor.GOLD + "Spectate Location", "to set location, or", "to set location.");
    //private ItemStack lobbyEditor = getItem(Material.REDSTONE_TORCH, ChatColor.RED + "Lobby Location", "to set location, or", "to set location.");

    //private ItemStack nameEditor = getItem(Material.NAME_TAG, ChatColor.LIGHT_PURPLE + "Arena Name", "to change the name, or", "to change it.");
    //private ItemStack creatorEditor = getItem(Material.BOOK, ChatColor.AQUA + "Creator Name", "to change creator name, or", "to change it.");
    //private ItemStack doneItem = getItem(Material.RABBIT_FOOT, ChatColor.YELLOW + "Done", "to exit edit mode, or", "to exit.");

}
