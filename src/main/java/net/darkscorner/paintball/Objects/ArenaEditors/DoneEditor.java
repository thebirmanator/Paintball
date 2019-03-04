package net.darkscorner.paintball.objects.arenaeditors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.Arena;

public class DoneEditor extends EditorItem {

	public DoneEditor(ItemStack item, Arena arena) {
		super(item, arena);
	}

	@Override
	public void use(Player player, Action action, Location loc) {
		EditorKit.getActiveKit(player).removeKit();
		player.sendMessage(Main.prefix + "Finished editing arena " + getArena().getName());
	}

}
