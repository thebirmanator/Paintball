package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.objects.menus.Menu;
import ltd.indigostudios.paintball.objects.menus.arena.ArenaEditorMenu;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;

public class PlayerLeaveListener implements Listener {

	private Main main;
	public PlayerLeaveListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		//String quitMessage = ChatColor.RED + "- " + ChatColor.RESET + event.getPlayer().getDisplayName();
		//event.setQuitMessage(quitMessage);
		
		PlayerProfile gp = PlayerProfile.getGamePlayer(event.getPlayer());
		if(gp.isInGame()) {
			gp.getCurrentGame().removePlayer(gp);
			
			// remove paintball gun on leave
			gp.getGun().removeFrom(event.getPlayer());
			
			// set to survival mode in case they were spectating
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		
		// remove edit kit
		/*
		if(EditorKit.hasKit(event.getPlayer())) {
			EditorKit.getActiveKit(event.getPlayer()).removeKit();
			event.getPlayer().removeMetadata(ArenaEditCommand.editMeta, main);
		}*/
		if (Menu.getViewing(event.getPlayer()) instanceof ArenaEditorMenu) {
			Menu.getViewing(event.getPlayer()).close(event.getPlayer());
		}

		gp.unload();
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		String quitMessage = ChatColor.RED + "-" + ChatColor.RESET + event.getPlayer().getName();
		event.setLeaveMessage(quitMessage);
		
		PlayerProfile gp = PlayerProfile.getGamePlayer(event.getPlayer());
		if(gp.isInGame()) {
			gp.getCurrentGame().removePlayer(gp);
			
			// remove paintball gun on leave
			event.getPlayer().getInventory().remove(Material.GOLDEN_HOE);
			
			// set to survival mode in case they were spectating
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		
		// remove edit kit
		/*
		if(EditorKit.hasKit(event.getPlayer())) {
			EditorKit.getActiveKit(event.getPlayer()).removeKit();
			event.getPlayer().removeMetadata(ArenaEditCommand.editMeta, main);
		}*/
		if (Menu.getViewing(event.getPlayer()) instanceof ArenaEditorMenu) {
			Menu.getViewing(event.getPlayer()).close(event.getPlayer());
		}

		gp.unload();
	}
}
