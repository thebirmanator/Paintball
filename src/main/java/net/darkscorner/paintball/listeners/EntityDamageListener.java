package net.darkscorner.paintball.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.events.GamePlayerDeathEvent;
import net.darkscorner.paintball.objects.GamePlayer;
import net.darkscorner.paintball.objects.PaintballGame;

public class EntityDamageListener implements Listener {

	private Main main;
	public static int respawnTime;
	public EntityDamageListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			GamePlayer victim = GamePlayer.getGamePlayer(player);
			// player is in the void
			if(event.getCause() == DamageCause.VOID) {
				if(victim.isInGame()) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
						
						@Override
						public void run() {
							main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim));
						}
					});
				} else {
					// wait a tick because otherwise you stand, unable to move at spawn
					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
						
						@Override
						public void run() {
							player.teleport(PaintballGame.getLobbySpawn());
						}
					});
				}
			} else {
				// player is at 0 health
				if(victim.isInGame()) {
					if(event.getDamage() >= player.getHealth()) {
						main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim));
					}
				} else {
					if(event.getDamage() >= player.getHealth()) {
						player.teleport(PaintballGame.getLobbySpawn());
					}
				}
			}
			
			event.setCancelled(true);
		}
	}
}
