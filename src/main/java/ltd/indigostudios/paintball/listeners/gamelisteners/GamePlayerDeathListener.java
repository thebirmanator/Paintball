package ltd.indigostudios.paintball.listeners.gamelisteners;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.GamePlayerDeathEvent;
import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.games.GameState;
import ltd.indigostudios.paintball.objects.games.PaintballGame;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import ltd.indigostudios.paintball.objects.player.PlayerInGameStat;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.powerups.PowerUp;
import ltd.indigostudios.paintball.utils.SoundEffect;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class GamePlayerDeathListener implements Listener {

    private Main main;

    public GamePlayerDeathListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeath(GamePlayerDeathEvent event) {
        GameSettings game = event.getGame();
        Arena arena = game.getArena();
        PlayerProfile victim = event.getVictim();
        victim.getPlayer().setGameMode(GameMode.SPECTATOR);
        victim.getPlayer().teleport(arena.getSpectatingPoint());

        victim.getCurrentGameStats().addToStat(PlayerInGameStat.DEATHS, 1);
        if (game instanceof TeamGame) {
            ((TeamGame) game).getTeam(victim).addDeaths(1);
        }

        victim.playSound(SoundEffect.DEATH);

        PowerUp.clearEffects(victim.getPlayer());

        PlayerProfile killer = event.getKiller();
        String deathMsg = "";
        if (!killer.equals(victim)) {
            killer.playSound(SoundEffect.HIT);
            killer.getCurrentGameStats().addToStat(PlayerInGameStat.KILLS, 1);
            if (game instanceof TeamGame) {
                ((TeamGame) game).getTeam(killer).addKills(1);
            }
            killer.getCurrentGameStats().addToKillStreak(1);
            if (killer.getCurrentGameStats().getCurrentKillStreak() > 2) {
                killer.getPlayer().sendMessage(ChatColor.GRAY + "You have a " + ChatColor.RED + killer.getCurrentGameStats().getCurrentKillStreak() + " player kill streak" + ChatColor.GRAY + "!");
                for (PlayerProfile p : event.getGame().getAllPlayers()) {
                    if (!p.equals(killer)) {
                        p.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + killer.getPlayer().getName() + ChatColor.GRAY + " has a " + ChatColor.RED + killer.getCurrentGameStats().getCurrentKillStreak() + " player kill streak" + ChatColor.GRAY + "!");
                    }
                }
            }

            //killer.getGameScoreboard().update(killer.getPlayer().getScoreboard(), "%kills%", "" + killer.getCurrentGameStats().getStat(PlayerInGameStat.KILLS));
            if (game.getCoinsPerKill() > 0) {
                killer.addMoney(game.getCoinsPerKill());
                //Main.coins.addCoins(killer.getPlayer().getName(), game.getCoinsPerKill());
            }
            killer.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.AQUA + "+ $" + game.getCoinsPerKill()).create());
            victim.getPlayer().sendTitle(ChatColor.DARK_RED + "You were hit!", ChatColor.RED + "Killer: " + killer.getPlayer().getName(), 10, 40, 10);
            deathMsg = getDeathMessage(game, false);
            deathMsg = deathMsg.replaceAll("%killer%", killer.getPlayer().getName());
            deathMsg = deathMsg.replaceAll("%victim%", victim.getPlayer().getName());

        } else {
            victim.getPlayer().sendTitle(ChatColor.DARK_RED + "You were hit!", ChatColor.RED + "By... yourself", 10, 40, 10);
            deathMsg = getDeathMessage(game, true);
            deathMsg = deathMsg.replaceAll("%victim%", victim.getPlayer().getName());
        }

        for (PlayerProfile p : event.getGame().getAllPlayers()) {
            p.getPlayer().sendMessage(deathMsg);
        }

        // reset the killed player's kill streak
        if (victim.getCurrentGameStats().getCurrentKillStreak() > 0) {
            if (victim.getCurrentGameStats().getCurrentKillStreak() > 2) {
                killer.getPlayer().sendMessage(ChatColor.GRAY + "You have ended " + ChatColor.YELLOW + victim.getPlayer().getName() + "'s " + ChatColor.RED + victim.getCurrentGameStats().getCurrentKillStreak() + " player kill streak" + ChatColor.GRAY + "!");
                for (PlayerProfile p : event.getGame().getAllPlayers()) {
                    if (!p.equals(killer)) {
                        p.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + killer.getPlayer().getName() + ChatColor.GRAY + " has ended " + ChatColor.YELLOW + victim.getPlayer().getName() + "'s " + ChatColor.RED + victim.getCurrentGameStats().getCurrentKillStreak() + " player kill streak" + ChatColor.GRAY + "!");
                    }
                }
            }
            victim.getCurrentGameStats().setCurrentKillStreak(0);
            victim.getPlayer().sendMessage(ChatColor.RED + "Your kill streak has been reset!");
        }


        // respawn countdown timer
        new BukkitRunnable() {
            int playerRespawnTime = game.getRespawnTime();

            @Override
            public void run() {
                // Make sure the player is still online and in the game
                if (victim.getPlayer() != null && victim.isInGame()) {
                    if (playerRespawnTime <= 0) {
                        if (game.getGameState() == GameState.STARTED) {
                            victim.getPlayer().setGameMode(Main.defaultGamemode);
                            victim.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.YELLOW + "You have respawned!").create());
                            Random random = new Random();
                            int spawnIndex = random.nextInt(arena.getFreeForAllSpawnPoints().size());
                            Location respawnLoc = arena.getFreeForAllSpawnPoints().get(spawnIndex);
                            Location playerSpawn = respawnLoc.clone();
                            playerSpawn = playerSpawn.add(0.5, 0, 0.5);
                            victim.getPlayer().teleport(playerSpawn);
                            arena.getFreeForAllSpawnPoints().remove(spawnIndex);
                            ((PaintballGame) game).makeInvulnerable(victim.getPlayer(), 60);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> arena.getFreeForAllSpawnPoints().add(respawnLoc), 60);
                            this.cancel();
                        }
                        this.cancel();
                    } else if (playerRespawnTime <= 5) {
                        victim.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.YELLOW + "Respawning in " + ChatColor.GOLD + playerRespawnTime + " seconds").create());
                    }
                    playerRespawnTime--;
                } else {
                    cancel();
                }

            }
        }.runTaskTimer(main, 0, 20);

    }

    public String getDeathMessage(GameSettings game, boolean isSuicide) {
        String msg = "";
        Random random = new Random();

        if (isSuicide) {
            List<String> msgs = game.getDeathMsgs(GameSettings.DeathType.SUICIDE);
            int index = random.nextInt(msgs.size());
            msg = msgs.get(index);
        } else {
            List<String> msgs = game.getDeathMsgs(GameSettings.DeathType.NORMAL);
            int index = random.nextInt(msgs.size());
            msg = msgs.get(index);
        }

        return msg;
    }
}
