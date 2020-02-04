package net.darkscorner.paintball.objects.menus.arena.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import net.darkscorner.paintball.objects.menus.arena.EditorKit;
import net.darkscorner.paintball.objects.menus.arena.menuitems.ArenaEditorItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.darkscorner.paintball.Main;

public class DoneArenaEditor extends ArenaEditorItem {

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
		return null;
	}

	@Override
	public void createItem() {

	}

}
