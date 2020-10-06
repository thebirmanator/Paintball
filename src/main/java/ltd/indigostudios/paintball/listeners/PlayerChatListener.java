package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.objects.games.PaintballGame;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import ltd.indigostudios.paintball.objects.menus.arena.ArenaEditorMenu;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        // Is editing an arena, do not allow chatting
        if (ArenaEditorMenu.getViewing(player) != null) {
            return;
        }

        Set<Player> recipients = event.getRecipients();
        PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);

        // This is a "real" chat event; a player actually typed the message
        if (event.isAsynchronous()) {
            recipients.removeIf((recipient) -> {
                PlayerProfile recipientProfile = PlayerProfile.getGamePlayer(recipient);
                // One is in a game and one isn't, remove recipient
                if (recipientProfile.isInGame() != playerProfile.isInGame()) return true;
                // If both players are in a game
                if (recipientProfile.isInGame()) {
                    // Both in a game
                    PaintballGame recipientGame = (PaintballGame) recipientProfile.getCurrentGame();
                    // Players are in the same game
                    if (recipientGame.equals(playerProfile.getCurrentGame())) {
                        // Remove if one player is spectating and one is playing
                        if (recipientGame.isPlaying(recipientProfile) != recipientGame.isPlaying(playerProfile))
                            return true;
                        // The game is a team game. Only members of the same team should receive player's message
                        if (recipientGame instanceof TeamGame) {
                            TeamGame teamGame = (TeamGame) recipientGame;
                            return !teamGame.getTeam(playerProfile).equals(teamGame.getTeam(recipientProfile));
                        }
                    } else { // Players are not in the same game, remove them
                        return true;
                    }
                }
                // Both not in game, do not remove recipient
                return false;
            });
        }
    }

	/*
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player sender = event.getPlayer();
		if(sender.hasMetadata(ArenaEditCommand.editMeta) && sender.hasMetadata(ArenaEditorItem.editingMeta)) { // using chat to edit arena
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() { // make it synchronous
				
				@Override
				public void run() {
					Arena arena = Arena.getArena(sender.getMetadata(ArenaEditCommand.editMeta).get(0).asString());
					String editing = event.getMessage();
					editing = ChatColor.translateAlternateColorCodes('&', editing);
					if(sender.getMetadata(ArenaEditorItem.editingMeta).get(0).asString().equals(NameArenaEditor.attrMeta)) { // editing arena name
						//arena.setName(editing);
						sender.removeMetadata(NameArenaEditor.editingMeta, Main.getPlugin(Main.class));
						sender.sendMessage(Main.prefix + "Set arena display name to " + editing);
					} else if(sender.getMetadata(ArenaEditorItem.editingMeta).get(0).asString().equals(CreatorArenaEditor.attrMeta)) { // editing arena creator
						//arena.setCreator(editing);
						sender.removeMetadata(CreatorArenaEditor.editingMeta, Main.getPlugin(Main.class));
						sender.sendMessage(Main.prefix + "Set arena creator name to " + editing);
					}
					
				}
			});
		} else {
			PlayerProfile gSender = PlayerProfile.getGamePlayer(sender);
			Set<Player> playerRecipients = event.getRecipients();
			Set<PlayerProfile> recipients = new HashSet<>();
			if(gSender.isInGame()) {
				if(gSender.getCurrentGame().getPlayers(true).contains(gSender)) { // player is in game; send their message to everyone in game and spectating
					recipients = gSender.getCurrentGame().getAllPlayers();
				} else { // player is spectating, send message to other spectators
					recipients = gSender.getCurrentGame().getPlayers(false);
				}
			} else { // player is not in game; send to anyone else not in a game
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!PlayerProfile.getGamePlayer(p).isInGame()) {
						recipients.add(PlayerProfile.getGamePlayer(p));
					}
				}
			}
			final Set<PlayerProfile> finalRecipients = recipients;
			playerRecipients.removeIf(new Predicate<Player>() {
				@Override
				public boolean test(Player player) {
					for(PlayerProfile p : finalRecipients) {
						if(player.equals(p.getPlayer())) { // player is in the game players; dont remove
							return false;
						}
					}
					// player is not in game players; remove
					return true;
				}
			});
		}
	}*/
}
