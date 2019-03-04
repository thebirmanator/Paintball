package net.darkscorner.paintball.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.Commands.ArenaEditCommand;
import net.darkscorner.paintball.Events.PowerUpUseEvent;
import net.darkscorner.paintball.Objects.GamePlayer;
import net.darkscorner.paintball.Objects.PowerUp;
import net.darkscorner.paintball.Objects.ArenaEditors.EditorKit;

public class PlayerInteractListener implements Listener {

	private Main main;
	public PlayerInteractListener(Main main) {
		this.main = main;
	}
	
	// shooting the paintball
		@EventHandler
		public void onRightClick(PlayerInteractEvent event) {
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
						*/
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
			
			Player player = event.getPlayer();

			if(event.getHand() == EquipmentSlot.HAND && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				Block block = event.getClickedBlock();
				GamePlayer gp = GamePlayer.getGamePlayer(player);
				// clicked a powerup
				if(block != null && PowerUp.isPowerUpBlock(block) && gp.getCurrentGame().getUsedArena().getPowerUpSpawnPoints().contains(block.getLocation())) {
					if(player.getGameMode() != GameMode.SPECTATOR) {
						main.getServer().getPluginManager().callEvent(new PowerUpUseEvent(PowerUp.getPowerUpBlock(block), event.getClickedBlock().getLocation(), gp.getCurrentGame(), gp));
						
						block.setType(Material.AIR);
					}
				} else { 
					if(player.getGameMode() != GameMode.SPECTATOR) {
						if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_HOE) { // shooting a paintball
							gp.playSound(SoundEffect.SHOOT);
							if(player.hasMetadata("volleypowerup")) { // volley powerup shot
								shootVolley(player);
							} else { // normal shot
								player.launchProjectile(Snowball.class);
								gp.getStats().addShot();
								gp.getGameScoreboard().update(player.getScoreboard(), "%shots%", "" + gp.getStats().getNumShotsFired());
							}
						}
					}
				}
			}
		}

		// thanks to blablubbabc on the forums for this crazy maths https://bukkit.org/threads/multiple-arrows-with-vectors.177643/
		public void shootVolley(Player player) {
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
				player.launchProjectile(Snowball.class, velocity);
				GamePlayer gp = GamePlayer.getGamePlayer(player);
				gp.getStats().addShot();
				gp.getGameScoreboard().update(player.getScoreboard(), "%shots%", "" + gp.getStats().getNumShotsFired());
			}
		}
		
		private Vector rotateY(Vector direction, double angleDirection) {
			double angleRotate = Math.toRadians(angleDirection);
			double x = direction.getX();
			double z = direction.getZ();
			
			double cos = Math.cos(angleRotate);
			double sin = Math.sin(angleRotate);
			
			return (new Vector(x*cos+z*(-sin), 0.0, x*sin+z*cos)).normalize();
		}
}
