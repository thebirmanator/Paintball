package net.darkscorner.paintball.listeners;

import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.equippable.guns.ShotGun;
import net.darkscorner.paintball.objects.menus.Menu;
import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.powerups.PowerUp;
import net.darkscorner.paintball.objects.powerups.VolleyPowerUp;
import net.darkscorner.paintball.utils.Clicks;
import net.darkscorner.paintball.utils.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.events.PowerUpUseEvent;
import net.darkscorner.paintball.objects.player.PlayerProfile;

public class PlayerInteractListener implements Listener {

	private Main main;
	public PlayerInteractListener(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Menu menu = Menu.getViewing(player);
		// Using an inventory editor
		if (event.getAction() != Action.PHYSICAL && menu != null) {
			int slot = player.getInventory().getHeldItemSlot();
			if (menu.hasClickableItem(slot)) {
				menu.getClickableItem(slot).use(player, Clicks.asClickType(event.getAction()));
				return;
			}
		}

		// Clicked a PowerUp
		Block block = event.getClickedBlock();
		PlayerProfile playerProfile = PlayerProfile.getGamePlayer(player);
		if (block != null && PowerUp.isPowerUpBlock(block)) {
			PowerUp.getPowerUpBlock(block).use(player);
			block.setType(Material.AIR);
			main.getServer().getPluginManager().callEvent(new PowerUpUseEvent(PowerUp.getPowerUpBlock(block), event.getClickedBlock().getLocation(), playerProfile.getCurrentGame(), playerProfile));
			return;
		}

		// Shooting a paintball
		ItemStack handItem = event.getItem();
		if (handItem != null && Gun.isGun(handItem)) {
			Gun gun = Gun.getGun(handItem);
			gun.shoot(player);
			/*
			if (VolleyPowerUp.hasPowerUp(player)) {
				for (Vector velocity : Vectors.getVolleyVectors(player)) {
					gun.shoot(player);
					playerProfile.getCurrentGameStats().addToStat(PlayerInGameStat.SHOTS, 1);
				}
			} else {
				gun.shoot(player);
				playerProfile.getCurrentGameStats().addToStat(PlayerInGameStat.SHOTS, 1);
			}*/
		}
/*
			if(!event.getPlayer().hasMetadata(ArenaEditCommand.editMeta)) {
				event.setCancelled(true);
			} else {
				String editingArenaName = event.getPlayer().getMetadata(ArenaEditCommand.editMeta).get(0).asString();
				if(editingArenaName.equals("")) { // block edit mode, let them place blocks
					event.setCancelled(false);
				} else {
					event.setCancelled(true);
					if(event.hasItem()) {
						ItemStack clickedItem = event.getItem();
						//Arena arena = Arena.getArena(editingArenaName);
						Action click = event.getAction();
						/*
						if(clickedItem.) {
							EditorItem editor = (EditorItem) clickedItem;
							editor.use(event.getPlayer(), click, event.getClickedBlock().getLocation());
						}
						*//*
						if(EditorKit.hasKit(event.getPlayer())) {
							EditorKit kit = EditorKit.getActiveKit(event.getPlayer());
							if(kit.hasItemStack(clickedItem)) {
								Location loc = event.getPlayer().getLocation().getBlock().getLocation();
								if(event.getClickedBlock() != null) {
									loc = event.getClickedBlock().getLocation();
								}
								kit.getFromItemStack(clickedItem).use(event.getPlayer(), click, loc);
							}
						}
						
					}
				}
			}
			
			//Player player = event.getPlayer();

			if(event.getHand() == EquipmentSlot.HAND && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				//Block block = event.getClickedBlock();
				PlayerProfile gp = PlayerProfile.getGamePlayer(player);
				// clicked a powerup
				if(block != null && PowerUp.isPowerUpBlock(block) && gp.getCurrentGame().getArena().getPowerUpSpawnPoints().contains(block.getLocation())) {
					if(player.getGameMode() != GameMode.SPECTATOR) {
						main.getServer().getPluginManager().callEvent(new PowerUpUseEvent(PowerUp.getPowerUpBlock(block), event.getClickedBlock().getLocation(), gp.getCurrentGame(), gp));
						
						block.setType(Material.AIR);
					}
				}
					if(Gun.isGun(player.getInventory().getItemInMainHand())) { // shooting a paintball
						if(!player.hasMetadata(ShotGun.metaCooldown)) { // not on cooldown
							if (player.hasMetadata(Game.invulnerableMeta)) { // remove invulnerability on shot if they have it
								player.removeMetadata(Game.invulnerableMeta, main);
							}
							Gun gun = Gun.getGun(player.getInventory().getItemInMainHand());
							PlayerProfile gp = PlayerProfile.getGamePlayer(player);
							Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
								@Override
								public void run() {
									if(player.getGameMode() != GameMode.SPECTATOR) {
										gp.playSound(SoundEffect.SHOOT);
										if (player.hasMetadata("volleypowerup")) { // volley powerup shot
											shootVolley(player, gun);
										} else { // normal shot
											gun.shoot(player, Gun.defaultVector); // normal shot, use default velocity
											gp.getCurrentGameStats().addToStat(PlayerInGameStat.SHOTS, 1);
											gp.getGameScoreboard().update(player.getScoreboard(), "%shots%", "" + gp.getCurrentGameStats().getStat(PlayerInGameStat.SHOTS));
										}
									}
								}
							}, 2);
						}

			}
*/
	}
/*
	// thanks to blablubbabc on the forums for this crazy maths https://bukkit.org/threads/multiple-arrows-with-vectors.177643/
	public void shootVolley(Player player, Gun gun) {
			int[] angles = {20, 10, 0, -10, -20};
			
			Location pLoc = player.getLocation();
			Vector pDir = player.getEyeLocation().getDirection();
			
			// set vector to length 1
			pDir.normalize();
			
			// vector in the viewer's direction
			Vector dirY = (new Location(pLoc.getWorld(), 0, 0, 0, pLoc.getYaw(), 0)).getDirection().normalize();
			
			Vector velocity;
			for(int angle : angles) {
				if(angle != 0) {
					velocity = rotateY(dirY, angle);
					velocity.multiply(Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ())).subtract(dirY);
					velocity = pDir.clone().add(velocity).normalize();
				} else {
					velocity = pDir.clone();
				}
				gun.shoot(player, velocity);
				PlayerProfile gp = PlayerProfile.getGamePlayer(player);
				gp.getCurrentGameStats().addToStat(PlayerInGameStat.SHOTS, 1);
				gp.getGameScoreboard().update(player.getScoreboard(), "%shots%", "" + gp.getCurrentGameStats().getStat(PlayerInGameStat.SHOTS));
			}
	}
		
	private Vector rotateY(Vector direction, double angleDirection) {
			double angleRotate = Math.toRadians(angleDirection);
			double x = direction.getX();
			double z = direction.getZ();
			
			double cos = Math.cos(angleRotate);
			double sin = Math.sin(angleRotate);
			
			return (new Vector(x*cos+z*(-sin), 0.0, x*sin+z*cos)).normalize();
	}*/
}
