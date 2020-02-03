package net.darkscorner.paintball.objects.menus.arenaeditors;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.commands.ArenaEditCommand;

public class EditorKit {

	private Player owner;
	private ArenaEditorItem[] arenaEditorItems;
	private Main main;
	
	private static HashMap<Player, EditorKit> activeKits = new HashMap<Player, EditorKit>();
	public EditorKit(Player owner, ArenaEditorItem[] arenaEditorItems) {
		this.owner = owner;
		this.arenaEditorItems = arenaEditorItems;
		
		main = Main.getPlugin(Main.class);
	}
	
	public boolean giveKit() {
		if(!hasKit(owner)) {
			for(int i = 0; i < arenaEditorItems.length; i++) {
				if(arenaEditorItems[i] != null) {
					owner.getInventory().setItem(i, arenaEditorItems[i].getItem());
				}
			}
			
			activeKits.put(owner, this);
			return true;
		}
		return false;
	}
	
	public void removeKit() {
		for(int i = 0; i < arenaEditorItems.length; i++) {
			owner.getInventory().setItem(i, new ItemStack(Material.AIR));
		}
		
		activeKits.remove(owner);
		
		owner.removeMetadata(ArenaEditCommand.editMeta, main);
	}
	
	public boolean hasItemStack(ItemStack item) {
		if(getFromItemStack(item) != null) {
			return true;
		}
		return false;
	}
	
	public ArenaEditorItem getFromItemStack(ItemStack item) {
		for(int i = 0; i < arenaEditorItems.length; i++) {
			if(arenaEditorItems[i] != null) {
				if(arenaEditorItems[i].getItem().isSimilar(item)) {
					return arenaEditorItems[i];
				}
			}
		}
		
		return null;
	}
	
	public static boolean hasKit(Player player) {
		if(activeKits.containsKey(player)) {
			return true;
		}
		return false;
	}
	
	public static EditorKit getActiveKit(Player player) {
		return activeKits.get(player);
	}
}
