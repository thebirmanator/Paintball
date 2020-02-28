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
            boolean isWhitelist = game.blockCommands();
            String cmd = event.getMessage();
            // Remove slash in cmd if there is one
            if (cmd.startsWith("/")) {
                cmd = cmd.substring(1);
            }

            if (isWhitelist) {
                for (String whitelistedCmd : game.commandExceptions()) {
                    // Found on whitelist, allow
                    if (cmd.startsWith(whitelistedCmd)) {
                        return;
                    }
                }
                // Did not find the cmd on the whitelist, block it
                event.setCancelled(true);
                player.sendMessage(Main.prefix + "You may not use this command in a game.");
            } else {
                for (String blacklistedCmd : game.commandExceptions()) {
                    // Found on blacklist, disallow
                    if (cmd.startsWith(blacklistedCmd)) {
                        event.setCancelled(true);
                        player.sendMessage(Main.prefix + "You may not use this command in a game.");
                        return;
                    }
                }
            }
        }
    }
}
