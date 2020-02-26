package net.darkscorner.paintball.objects.menus;

import net.darkscorner.paintball.objects.menus.arena.items.*;
import net.darkscorner.paintball.objects.menus.equipment.items.EquipmentItem;
import net.darkscorner.paintball.objects.menus.game.items.BackMenuItem;
import net.darkscorner.paintball.objects.menus.game.items.EndGameItem;
import net.darkscorner.paintball.objects.menus.game.items.GameItem;
import net.darkscorner.paintball.objects.menus.game.items.PlayerOptionsItem;
import net.darkscorner.paintball.objects.menus.shop.items.BuyPaintItem;
import net.darkscorner.paintball.objects.menus.shop.items.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ClickableItem {

    protected ItemStack playerItem;
    private Menu owningMenu;

    public ClickableItem(Menu owningMenu) {
        this.owningMenu = owningMenu;
    }

    public abstract void use(Player player, ClickType click);

    public abstract ClickableItem getForPlayer(Player player);

    protected abstract void createTemplate();

    public ItemStack getItemStack() {
        return playerItem;
    }

    public Menu getOwningMenu() {
        return owningMenu;
    }

    public static void loadItems() {
        //TODO: make these customisable in config (will allow reflection loading by storing class names)
        // Game menu
        new BackMenuItem(null, null).createTemplate();
        new EndGameItem(null).createTemplate();
        new GameItem(null,null).createTemplate();
        new PlayerOptionsItem(null, null).createTemplate();

        // Arena items
        new CreatorArenaEditor(null).createTemplate();
        new DoneArenaEditor(null).createTemplate();
        new LobbyArenaEditor(null).createTemplate();
        new NameArenaEditor(null).createTemplate();
        new PowerupLocationArenaEditor(null).createTemplate();
        new SpawnpointsArenaEditor(null).createTemplate();
        new SpecPointArenaEditor(null).createTemplate();

        ShopItem.loadShopItems();
        EquipmentItem.loadEquipmentItems();
    }
}
