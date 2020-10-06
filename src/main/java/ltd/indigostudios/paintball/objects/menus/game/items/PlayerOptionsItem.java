package ltd.indigostudios.paintball.objects.menus.game.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.game.GameMenu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.SoundEffect;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerOptionsItem extends GameMenuItem {

    private Player representing;
    private static ItemStack templateItem;

    // this is the player head item in the game options
    public PlayerOptionsItem(Player representing, GameMenu parent) {
        super(parent);
        this.representing = representing;
    }

    @Override
    public void use(Player player, ClickType click) {

        switch (click) {
            case LEFT: // kick player from their game
                if (player.hasPermission("paintball.options.players")) {
                    //SkullMeta meta = (SkullMeta) getIcon().getItemMeta();
                    PlayerProfile gp = PlayerProfile.getGamePlayer(representing);
                    gp.getCurrentGame().removePlayer(gp);
                    player.sendMessage(Main.prefix + "Kicked " + ChatColor.GREEN + gp.getPlayer().getName() + ChatColor.GRAY + " from the game.");
                    PlayerProfile.getGamePlayer(player).playSound(SoundEffect.FORWARD_CLICK);
                    gp.getPlayer().sendMessage(Main.prefix + "You have been " + ChatColor.RED + "kicked" + ChatColor.GRAY + " from the game.");
                    getOwningMenu().close(player, true);
                } else {
                    player.sendMessage(Main.prefix + "Sorry, you do not have " + ChatColor.RED + "permission" + ChatColor.GRAY + " to kick players from a game.");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        ItemStack specific = templateItem.clone();
        if (player.hasPermission("paintball.options.players")) {
            specific = new ItemEditor(specific)
                    .addAction(ClickType.LEFT, "to kick player from this game.")
                    .getItemStack();

        }
        SkullMeta meta = (SkullMeta) specific.getItemMeta();
        meta.setOwningPlayer(representing);
        specific.setItemMeta(meta);
        playerItem = specific;
        return this;
    }

    @Override
    public void createTemplate() {
        templateItem = new ItemEditor(Material.PLAYER_HEAD, null)
                .getItemStack();
    }

}
