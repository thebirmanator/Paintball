package net.darkscorner.paintball.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.commands.ArenaEditCommand;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.arenaeditors.EditorKit;

public class PlayerLeaveListener implements Listener {

	private Main main;
	public PlayerLeaveListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		String quitMessage = ChatColor.RED + "- " + ChatColor.RESET + event.getPlayer().getDisplayName();
		event.setQuitMessage(quitMessage);
		
		GamePlayer gp = GamePlayer.getGamePlayer(event.getPlayer());
		if(gp.isInGame()) {
			gp.getCurrentGame().removePlayer(gp);
			
			// remove paintball gun on leave
			gp.getGun().removeFrom(event.getPlayer());
			
			// set to survival mode in case they were spectating
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		
		// remove edit kit
		if(EditorKit.hasKit(event.getPlayer())) {
			EditorKit.getActiveKit(event.getPlayer()).removeKit();
			event.getPlayer().removeMetadata(ArenaEditCommand.editMeta, main);
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		String quitMessage = ChatColor.RED + "-" + ChatColor.RESET + event.getPlayer().getName();
		event.setLeaveMessage(quitMessage);
		
		GamePlayer gp = GamePlayer.getGamePlayer(event.getPlayer());
		if(gp.isInGame()) {
			gp.getCurrentGame().removePlayer(gp);
			
			// remove paintball gun on leave
			event.getPlayer().getInventory().remove(Material.GOLDEN_HOE);
			
			// set to survival mode in case they were spectating
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		
		// remove edit kit
		if(EditorKit.hasKit(event.getPlayer())) {
			EditorKit.getActiveKit(event.getPlayer()).removeKit();
			event.getPlayer().removeMetadata(ArenaEditCommand.editMeta, main);
		}
	}
}
