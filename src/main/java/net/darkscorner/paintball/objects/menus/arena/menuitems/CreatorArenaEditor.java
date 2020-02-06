package net.darkscorner.paintball.objects.menus.arena.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import net.darkscorner.paintball.objects.menus.arena.ArenaEditorMenu;
import net.darkscorner.paintball.objects.menus.arena.menuitems.ArenaEditorItem;
import net.darkscorner.paintball.utils.ItemEditor;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import net.darkscorner.paintball.Main;

public class CreatorArenaEditor extends ArenaEditorItem {

	public static String attrMeta = "creator";
	private Main main = Main.getInstance();
	private static ItemStack templateItem;

	public CreatorArenaEditor(ArenaEditorMenu editorMenu) {
		super(editorMenu);
	}

	@Override
	public void use(Player player, ClickType clickType) {
		if(!player.hasMetadata(editingMeta)) {
			player.setMetadata(editingMeta, new FixedMetadataValue(main, attrMeta));
			
			player.sendMessage(Main.prefix + "Type the new creator's name in chat, or \"cancel\" to cancel the change.");
			
			new BukkitRunnable() {
				int seconds = 0;
				@Override
				public void run() {
					if(!player.hasMetadata(editingMeta)) { // no longer waiting to type a name
						cancel();
					} else if(seconds >= 10) { // ran out of time
						cancel();
						player.removeMetadata(editingMeta, main);
						player.sendMessage(Main.prefix + "Creator editing timed out. Using the previously set name.");
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
		templateItem = new ItemEditor(Material.BOOK, Text.format("&bCreator Name"))
				.addAction(ClickType.LEFT, "to change creator name, or")
				.addAction(ClickType.RIGHT, "to change it.")
				.getItemStack();

	}

}
