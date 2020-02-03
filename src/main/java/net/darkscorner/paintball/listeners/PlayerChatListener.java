package net.darkscorner.paintball.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.commands.ArenaEditCommand;
import net.darkscorner.paintball.objects.Arena;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.menus.arenaeditors.CreatorArenaEditor;
import net.darkscorner.paintball.objects.menus.arenaeditors.ArenaEditorItem;
import net.darkscorner.paintball.objects.menus.arenaeditors.NameArenaEditor;

public class PlayerChatListener implements Listener {

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
						arena.setName(editing);
						sender.removeMetadata(NameArenaEditor.editingMeta, Main.getPlugin(Main.class));
						sender.sendMessage(Main.prefix + "Set arena display name to " + editing);
					} else if(sender.getMetadata(ArenaEditorItem.editingMeta).get(0).asString().equals(CreatorArenaEditor.attrMeta)) { // editing arena creator
						arena.setCreator(editing);
						sender.removeMetadata(CreatorArenaEditor.editingMeta, Main.getPlugin(Main.class));
						sender.sendMessage(Main.prefix + "Set arena creator name to " + editing);
					}
					
				}
			});
		} else {
			GamePlayer gSender = GamePlayer.getGamePlayer(sender);
			Set<Player> playerRecipients = event.getRecipients();
			Set<GamePlayer> recipients = new HashSet<>();
			if(gSender.isInGame()) {
				if(gSender.getCurrentGame().getInGamePlayers().contains(gSender)) { // player is in game; send their message to everyone in game and spectating
					recipients = gSender.getCurrentGame().getAllPlayers();
				} else { // player is spectating, send message to other spectators
					recipients = gSender.getCurrentGame().getSpectatingPlayers();
				}
			} else { // player is not in game; send to anyone else not in a game
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!GamePlayer.getGamePlayer(p).isInGame()) {
						recipients.add(GamePlayer.getGamePlayer(p));
					}
				}
			}
			final Set<GamePlayer> finalRecipients = recipients;
			playerRecipients.removeIf(new Predicate<Player>() {
				@Override
				public boolean test(Player player) {
					for(GamePlayer p : finalRecipients) {
						if(player.equals(p.getPlayer())) { // player is in the game players; dont remove
							return false;
						}
					}
					// player is not in game players; remove
					return true;
				}
			});
		}
	}
}
