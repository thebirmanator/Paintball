package ltd.indigostudios.paintball.objects.menus.game.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.game.GameMenu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.SoundEffect;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class EndGameItem extends GameMenuItem {

    private static ItemStack templateItem;

    public EndGameItem(GameMenu parent) {
        super(parent);
    }

    @Override
    public void use(Player player, ClickType click) {
        ((GameMenu) getOwningMenu()).getGame().endGame();
        player.sendMessage(Main.prefix + "You have " + ChatColor.RED + "force-ended" + ChatColor.GRAY + " the game.");
        PlayerProfile.getGamePlayer(player).playSound(SoundEffect.FORWARD_CLICK);
        getOwningMenu().close(player, true);
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        playerItem = templateItem;
        return this;
    }

    @Override
    public void createTemplate() {
        templateItem = new ItemEditor(Material.BEDROCK, Text.format("&cEnd Game"))
                .addAction(ClickType.LEFT, "to force-end this game.")
                .getItemStack();
    }

}
