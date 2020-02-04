package net.darkscorner.paintball.objects.menus.arena.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import net.darkscorner.paintball.objects.menus.arena.menuitems.ArenaEditorItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import net.darkscorner.paintball.Main;

public class NameArenaEditor extends ArenaEditorItem {

	public static String attrMeta = "name";
	
	private Main main;
	public NameArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
		main = Main.getPlugin(Main.class);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		if(!player.hasMetadata(editingMeta)) {
			player.setMetadata(editingMeta, new FixedMetadataValue(main, attrMeta));
			player.sendMessage(Main.prefix + "Type the new display name in chat.");
			
			new BukkitRunnable() {
				int seconds = 0;
				@Override
				public void run() {
					if(!player.hasMetadata(editingMeta)) { // no longer waiting to type a name
						cancel();
					} else if(seconds >= 10) { // ran out of time
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
		return null;
	}

	@Override
	public void createItem() {

	}

}
