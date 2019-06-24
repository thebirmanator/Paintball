package net.darkscorner.paintball.objects.menus.menuitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.Paint;

public class EquipPaintItem extends MenuItem {

	private Paint paint;
	public EquipPaintItem(MenuItem parent, ItemStack icon, Paint paint) {
		super(parent, icon);
		
		this.paint = paint;
	}

	@Override
	public void open(Player player, ClickType click) {
		if(getIcon().getType() != Material.GRAY_DYE) { // did not click on a locked dye
			if(getIcon().getEnchantments().isEmpty()) {
				GamePlayer.getGamePlayer(player).setPaint(paint);
				player.sendMessage(Main.prefix + "Set custom paint colour!");
			} else {
				GamePlayer.getGamePlayer(player).setPaint(Paint.getDefaultPaint());
				player.sendMessage(Main.prefix + "Reset custom paint colour.");
			}
		} else {
			player.sendMessage(Main.prefix + "This paint is " + ChatColor.RED + "locked" + ChatColor.GRAY + " for you.");
		}
		getContainedIn().closeMenu(player);
		
	}

}
