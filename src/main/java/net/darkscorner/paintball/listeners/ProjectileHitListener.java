package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.games.Game;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.events.GamePlayerDeathEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;

public class ProjectileHitListener implements Listener {

	private Main main;
	
	public ProjectileHitListener(Main main) {
		this.main = main;
	}
	// when the paintball hits something
		@EventHandler
		public void onHit(ProjectileHitEvent event) {
			if(event.getEntity() instanceof Snowball) {
				if(event.getHitBlock() != null) {
					Block hitBlock = event.getHitBlock();
					if(event.getEntity().getShooter() instanceof Player) {
						Player shooter = (Player) event.getEntity().getShooter();
						PlayerProfile gp = PlayerProfile.getGamePlayer(shooter);
						gp.getPaint().showPaint(hitBlock.getLocation());
					}
				} else if(event.getHitEntity() != null) {
					if(event.getHitEntity() instanceof Player) {
						Player player = (Player) event.getHitEntity();
						if(player.getGameMode() == GameMode.SURVIVAL) {
							PlayerProfile victim = PlayerProfile.getGamePlayer(player);
							if (event.getEntity().getShooter() instanceof Player) {
								Player pShooter = (Player) event.getEntity().getShooter();
								if (!player.hasMetadata(Game.invulnerableMeta)) { // is vulnerable
									victim.playSound(SoundEffect.DEATH);
									PlayerProfile killer = PlayerProfile.getGamePlayer(pShooter);
									// wait a tick for the paintball to fire first
									if (!((Player) event.getEntity().getShooter()).equals(player)) { // shooter and victim are different players
										main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim, killer));
									} else {
										main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim));
									}
								} else {
									pShooter.sendMessage(ChatColor.RED + "That player has just respawned and is invulnerable.");
								}
							}
						}
					}
				}
			}
		}
}
