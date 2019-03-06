package net.darkscorner.paintball.listeners;

import java.util.Set;

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
import net.darkscorner.paintball.objects.arenaeditors.CreatorEditor;
import net.darkscorner.paintball.objects.arenaeditors.EditorItem;
import net.darkscorner.paintball.objects.arenaeditors.NameEditor;

public class PlayerChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player sender = event.getPlayer();
		if(sender.hasMetadata(ArenaEditCommand.editMeta) && sender.hasMetadata(EditorItem.editingMeta)) {
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() { // make it synchronous
				
				@Override
				public void run() {
					Arena arena = Arena.getArena(sender.getMetadata(ArenaEditCommand.editMeta).get(0).asString());
					String editing = event.getMessage();
					editing = ChatColor.translateAlternateColorCodes('&', editing);
					if(sender.getMetadata(EditorItem.editingMeta).get(0).asString().equals(NameEditor.attrMeta)) { // editing arena name
						arena.setName(editing);
						sender.removeMetadata(NameEditor.editingMeta, Main.getPlugin(Main.class));
						sender.sendMessage(Main.prefix + "Set arena display name to " + editing);
					} else if(sender.getMetadata(EditorItem.editingMeta).get(0).asString().equals(CreatorEditor.attrMeta)) { // editing arena creator 
						arena.setCreator(editing);
						sender.removeMetadata(CreatorEditor.editingMeta, Main.getPlugin(Main.class));
						sender.sendMessage(Main.prefix + "Set arena creator name to " + editing);
					}
					
				}
			});
		} else {
			GamePlayer gSender = GamePlayer.getGamePlayer(sender);
			if(gSender.isInGame()) {
				if(gSender.getCurrentGame().getInGamePlayers().contains(gSender)) { // player is in game; send their message to everyone in game and spectating
					event.setCancelled(true);
					Set<GamePlayer> recipients = gSender.getCurrentGame().getAllPlayers();
					for(GamePlayer recipient : recipients) {
						recipient.getPlayer().sendMessage(String.format(event.getFormat(), sender.getDisplayName(), event.getMessage()));
					}
				} else { // player is spectating, send message to other spectators
					event.setCancelled(true);
					Set<GamePlayer> recipients = gSender.getCurrentGame().getSpectatingPlayers();
					for(GamePlayer recipient : recipients) {
						recipient.getPlayer().sendMessage(String.format(event.getFormat(), sender.getDisplayName(), event.getMessage()));
					}
				}
			} else { // player is not in game; send to anyone else not in a game
				event.setCancelled(true);
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!GamePlayer.getGamePlayer(p).isInGame()) {
						p.sendMessage(String.format(event.getFormat(), sender.getDisplayName(), event.getMessage()));
					}
				}
			}
		}
	}
}
