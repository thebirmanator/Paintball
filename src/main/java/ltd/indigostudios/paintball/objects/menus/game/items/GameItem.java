package ltd.indigostudios.paintball.objects.menus.game.items;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;
import ltd.indigostudios.paintball.objects.menus.game.GameMenu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.ItemEditor;
import ltd.indigostudios.paintball.utils.SoundEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GameItem extends GameMenuItem {

    private GameSettings game;
    private static ItemStack templateItem;

    // main game options menu; shows all games going on
    public GameItem(GameMenu parent, GameSettings game) {
        super(parent);
        this.game = game;
    }

    @Override
    public void use(Player player, ClickType click) {
        switch (click) {
            case LEFT: // send to spectate a game
                PlayerProfile gp = PlayerProfile.getGamePlayer(player);
                gp.playSound(SoundEffect.FORWARD_CLICK);
                if (!gp.isInGame()) {
                    game.addPlayer(gp, true);
                    //game.setToSpectating(gp);
                } else {
                    player.sendMessage(Main.prefix + "You cannot spectate a game if you're in one. Do /leave if you want to leave this game.");
                }
                getOwningMenu().close(player, true);
                break;
            case RIGHT: // open player list
                GameMenu playerOptionsMenu = new GameMenu("Players", (GameMenu) getOwningMenu(), 27, game);
                //GameMenu playerOptionsGameMenu = new GameMenu("Players", this, 27);
                int index = 0;
                for (PlayerProfile playerProfile : game.getPlayers(true)) {
                    ClickableItem clickableItem = new PlayerOptionsItem(playerProfile.getPlayer(), playerOptionsMenu).getForPlayer(player);
                    playerOptionsMenu.addItem(index, clickableItem);
                    index++;
                }
                playerOptionsMenu.showNavBar(true);
                getOwningMenu().close(player);
                playerOptionsMenu.open(player);
                break;
            case MIDDLE: // open game options
                if (player.hasPermission("paintball.options.use")) {
                    GameMenu gameOptionsMenu = new GameMenu("Game Options", (GameMenu) getOwningMenu(), 9, game);
                    ClickableItem endGameItem = new EndGameItem(gameOptionsMenu).getForPlayer(player);
                    gameOptionsMenu.addItem(0, endGameItem);
                    gameOptionsMenu.showNavBar(true);
                    getOwningMenu().close(player);
                    gameOptionsMenu.open(player);
                } else {
                    break;
                }
            default:
                break;
        }
    }

    @Override
    public ClickableItem getForPlayer(Player player) {
        ItemEditor editor = new ItemEditor(templateItem.clone())
                .setMaterial(game.getArena().getMaterial())
                .setDisplayName(game.getArena().getName());
        if (player.hasPermission("paintball.options.use")) {
            editor.addAction(ClickType.MIDDLE, "to view options for this game.");
        }
        playerItem = editor.getItemStack();
        return this;
    }

    @Override
    public void createTemplate() {
        templateItem = new ItemEditor(Material.STONE, "Arena Name Here")
                .addAction(ClickType.LEFT, " to spectate this game.")
                .addAction(ClickType.RIGHT, "to view in-game players.")
                .getItemStack();
    }

    public GameSettings getGame() {
        return game;
    }
}
