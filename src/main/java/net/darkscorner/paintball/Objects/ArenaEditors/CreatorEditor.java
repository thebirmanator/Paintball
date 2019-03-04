package net.darkscorner.paintball.Objects.ArenaEditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.Objects.Arena;

public class CreatorEditor extends EditorItem {

	public static String attrMeta = "creator";
	private Main main;
	public CreatorEditor(ItemStack item, Arena arena) {
		super(item, arena);
		main = Main.getPlugin(Main.class);
	}

	@Override
	public void use(Player player, Action action, Location loc) {
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
			}.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
		} else {
			player.sendMessage(Main.prefix + "Please type \"cancel\" to stop your previous edit.");
		}
	}

}
