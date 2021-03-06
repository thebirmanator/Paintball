package ltd.indigostudios.paintball.listeners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.GamePlayerDeathEvent;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageListener implements Listener {

    private Main main;
    public static int respawnTime;

    public EntityDamageListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            PlayerProfile victim = PlayerProfile.getGamePlayer(player);
            // player is in the void
            if (event.getCause() == DamageCause.VOID) {
                if (victim.isInGame()) {
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
                            //player.teleport(Game.getLobbySpawn());
                        }
                    });
                }
            } else {
                // player is at 0 health
                if (victim.isInGame()) {
                    if (event.getDamage() >= player.getHealth()) {
                        main.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(victim.getCurrentGame(), victim));
                    }
                } else {
                    if (event.getDamage() >= player.getHealth()) {
                        //player.teleport(Game.getLobbySpawn());
                    }
                }
            }

            event.setCancelled(true);
        }
    }
}
