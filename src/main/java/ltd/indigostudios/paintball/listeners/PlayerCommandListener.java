package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener implements Listener {

    @EventHandler
    public void onSend(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
        if (playerProfile.isInGame()) {
            GameSettings game = playerProfile.getCurrentGame();
            boolean blockCmd = game.blockCommands();
            if (game.commandExceptions().contains(event.getMessage())) {
                event.setCancelled(blockCmd);
                if (blockCmd) {
                    player.sendMessage(Main.prefix + "You may not use this command in a game.");
                }
            }
        }
    }
}
