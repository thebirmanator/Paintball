package net.darkscorner.paintball.Objects.ArenaEditors;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.Commands.ArenaEditCommand;

public class EditorKit {

	private Player owner;
	private EditorItem[] editorItems;
	private Main main;
	
	private static HashMap<Player, EditorKit> activeKits = new HashMap<Player, EditorKit>();
	public EditorKit(Player owner, EditorItem[] editorItems) {
		this.owner = owner;
		this.editorItems = editorItems;
		
		main = Main.getPlugin(Main.class);
	}
	
	public boolean giveKit() {
		if(!hasKit(owner)) {
			for(int i = 0; i < editorItems.length; i++) {
				if(editorItems[i] != null) {
					owner.getInventory().setItem(i, editorItems[i].getItem());
				}
			}
			
			activeKits.put(owner, this);
			return true;
		}
		return false;
	}
	
	public void removeKit() {
		for(int i = 0; i < editorItems.length; i++) {
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
	
	public EditorItem getFromItemStack(ItemStack item) {
		for(int i = 0; i < editorItems.length; i++) {
			if(editorItems[i] != null) {
				if(editorItems[i].getItem().isSimilar(item)) {
					return editorItems[i];
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
