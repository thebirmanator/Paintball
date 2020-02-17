package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.Team;
import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.games.TeamGame;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.events.GamePlayerDeathEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileHitListener implements Listener {

	private Main main;
	
	public ProjectileHitListener(Main main) {
		this.main = main;
	}
	// when the paintball hits something
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Snowball) {
			// Hit a block
			if (event.getHitBlock() != null) {
				Block hitBlock = event.getHitBlock();
				ProjectileSource source = event.getEntity().getShooter();
				if (source instanceof Player) {
					Player shooter = (Player) source;
					PlayerProfile gp = PlayerProfile.getGamePlayer(shooter);
					gp.getPaint().showPaint(hitBlock.getLocation());
				}
			} else if (event.getHitEntity() != null) { // Hit an entity
				Entity entity = event.getHitEntity();
				if (entity instanceof Player) {
					PlayerProfile victim = PlayerProfile.getGamePlayer((Player) entity);
					// Player shot an in-game player
					if (victim.getCurrentGame().getPlayers(true).contains(victim)) {
						if (event.getEntity().getShooter() instanceof Player) {
							Player pShooter = (Player) event.getEntity().getShooter();
							//if (!player.hasMetadata(Game.invulnerableMeta)) { // is vulnerable
								PlayerProfile killer = PlayerProfile.getGamePlayer(pShooter);
								Game game = killer.getCurrentGame();
								// Check if players are on the same team if it's a team game
								if (game instanceof TeamGame) {
									TeamGame teamGame = (TeamGame) game;
									Team killerTeam = teamGame.getTeam(killer);
									if (killerTeam.hasPlayer(victim)) {
										return;
									}
								}
								// wait a tick for the paintball to fire first
								victim.playSound(SoundEffect.DEATH);
								if (!pShooter.equals(victim.getPlayer())) { // shooter and victim are different players
									main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim, killer));
								} else {
									main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim));
								}
						} /*else {
							//pShooter.sendMessage(ChatColor.RED + "That player has just respawned and is invulnerable.");
						}*/
					}
				}
			}
		}
	}
}
