package net.darkscorner.paintball.objects.menus.arena.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import net.darkscorner.paintball.objects.menus.arena.EditorKit;
import net.darkscorner.paintball.objects.menus.arena.menuitems.ArenaEditorItem;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.darkscorner.paintball.Main;
import org.bukkit.inventory.ItemStack;

public class DoneArenaEditor extends ArenaEditorItem {

	private static ItemStack templateItem;

	public DoneArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		EditorKit.getActiveKit(player).removeKit();
		player.sendMessage(Main.prefix + "Finished editing arena " + getArena().getName());
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		playerItem = templateItem;
		return this;
	}

	@Override
	public void createTemplate() {
		//Material.RABBIT_FOOT, ChatColor.YELLOW + "Done", "to exit edit mode, or", "to exit."
		templateItem = new ItemEditor(Material.RABBIT_FOOT, Text.format("&eDone"))
				.addAction(ClickType.LEFT, "to exit edit mode, or")
				.addAction(ClickType.RIGHT, "to exit.")
				.getItemStack();
	}

}
