package ltd.indigostudios.paintball.objects.menus.equipment.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.equippable.paint.Paint;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.Menu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class EquipPaintItem extends EquipmentItem {

    private Paint paint;
    private static ItemStack templateItem;

    public EquipPaintItem(Paint paint, Menu owningMenu) {
        super(owningMenu);
        this.paint = paint;
    }

    @Override
    boolean hasPermission(Player player) {
        return player.hasPermission(paint.getPermission());
    }

    @Override
    boolean hasEquipped(Player player) {
        return PlayerProfile.getGamePlayer(player).getPaint().equals(paint);
    }

    @Override
    ItemStack getNoPermsItem() {
        return new ItemEditor(templateItem.getType(), templateItem.getItemMeta().getDisplayName())
                .addAction(ClickType.UNKNOWN, Text.format("&a" + Text.friendlyEnum(paint.getName())))
                .addAction(ClickType.UNKNOWN, Text.format("Unlock using &f/store"))
                .getItemStack();
    }

    @Override
    ItemStack getAvailableItem() {
        Material material = paint.getDisplayIcon();
        return new ItemEditor(material, Text.format("&a&l" + Text.friendlyEnum(paint.getName())))
                .addAction(ClickType.LEFT, "to equip.")
                .getItemStack();
    }

    @Override
    public void use(Player player, ClickType click) {
        if (hasPermission(player)) {
            PlayerProfile profile = PlayerProfile.getGamePlayer(player);
            if (hasEquipped(player)) {
                profile.setPaint(Paint.getDefaultPaint());
                player.sendMessage(Main.prefix + "Reset paint!");
            } else {
                profile.setPaint(paint);
                player.sendMessage(Main.prefix + "Set paint!");
            }
            getOwningMenu().close(player, true);
        } else {
            player.sendMessage(Main.prefix + "This paint is " + ChatColor.RED + "locked" + ChatColor.GRAY + " for you.");
        }
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = getNoPermsItem();
        if (hasPermission(player)) {
            if (hasEquipped(player)) {
                playerItem = getEquippedItem();
            } else {
                playerItem = getAvailableItem();
            }
        }
        return this;
    }

    @Override
    protected void createTemplate() {
        templateItem = new ItemEditor(Material.GRAY_DYE, Text.format("&cLOCKED"))
                //.addAction(ClickType.UNKNOWN, Text.format("&a" + Text.friendlyEnum(paint.getName())))
                .addAction(ClickType.UNKNOWN, Text.format("Unlock using &f/store"))
                .getItemStack();
    }
/*
	private Paint paint;
	public EquipPaintItem(GameMenuItem parent, ItemStack icon, Paint paint) {
		super(parent, icon);
		
		this.paint = paint;
	}

	@Override
	public void use(Player player, ClickType click) {
		if(getIcon().getType() != Material.GRAY_DYE) { // did not click on a locked dye
			if(getIcon().getEnchantments().isEmpty()) {
				PlayerProfile.getGamePlayer(player).setPaint(paint);
				player.sendMessage(Main.prefix + "Set custom paint colour!");
			} else {
				PlayerProfile.getGamePlayer(player).setPaint(Paint.getDefaultPaint());
				player.sendMessage(Main.prefix + "Reset custom paint colour.");
			}
		} else {
			player.sendMessage(Main.prefix + "This paint is " + ChatColor.RED + "locked" + ChatColor.GRAY + " for you.");
		}
		getContainedIn().closeMenu(player);
		
	}*/

}
