package net.darkscorner.paintball.objects.games;

import net.darkscorner.paintball.GameState;
import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.events.*;
import net.darkscorner.paintball.objects.Arena;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BasePaintballGame implements Game {
    private static Set<Game> allGames = new HashSet<>();

    private GameState gameState = GameState.IDLE;
    // maps player to if they are in game playing
    private Map<PlayerProfile, Boolean> allPlayers = new HashMap<>();
    private Arena arena;

    private int currentTaskID = -1;

    public BasePaintballGame(Arena arena) {
        this.arena = arena;
        allGames.add(this);

        Main.getInstance().getServer().getPluginManager().callEvent(new GameCreateEvent(Main.getInstance(), this));
    }

    public static Set<Game> getGames() {
        return allGames;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        gameState = state;
    }

    public void waitForPlayers(boolean start) {
        if (start) {
            gameState = GameState.IDLE;
            BukkitRunnable waitingTask = new BukkitRunnable() {

                @Override
                public void run() {
                    if (gameState == GameState.IDLE && getPlayers(true).size() < getStartPlayerAmount()) {
                        for (PlayerProfile p : getPlayers(true)) {
                            p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.GOLD + "Waiting for " + ChatColor.YELLOW + (getStartPlayerAmount() - getPlayers(true).size()) + ChatColor.GOLD + " player(s).").create());
                        }
                    } else {
                        cancel();
                    }

                }
            };
            waitingTask.runTaskTimer(Main.getInstance(), 0, 20);
            currentTaskID = waitingTask.getTaskId();
        } else {
            Bukkit.getScheduler().cancelTask(currentTaskID);
        }
    }

    public void countdown(boolean start) {
        if (start) {
            gameState = GameState.COUNTDOWN;
            currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                int countdownTime = 20;

                @Override
                public void run() {
                    if (getPlayers(true).size() >= getStartPlayerAmount()) {
                        if (countdownTime > 0) {
                            for (PlayerProfile p : getAllPlayers()) {
                                p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(ChatColor.GOLD + "" + ChatColor.BOLD + countdownTime).create());
                            }
                            countdownTime--;
                        } else {
                            countdown(false);
                            startGame();
                        }
                    } else { // not enough people to start
                        countdown(false);
                        waitForPlayers(true);
                    }

                }
            }, 0, 20);
        } else {
            Bukkit.getScheduler().cancelTask(currentTaskID);
        }
    }

    public void cancelCurrentTask() {
        Bukkit.getScheduler().cancelTask(currentTaskID);
        currentTaskID = -1;
    }

    public void startGame() {

        // start the game timer
        Game game = this;
        currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            int currentTime = 0;
            @Override
            public void run() {
                int timeRemaining = getGameTimeLength() - currentTime;
                currentTime++;

                for (PlayerProfile p : getAllPlayers()) {
                    p.getGameScoreboard().update(p.getPlayer().getScoreboard(), "%timeleft%", "" + formatTime(timeRemaining));
                }

                if(currentTime > getGameTimeLength()) {
                    endGame();
                    cancelCurrentTask();
                    for(PlayerProfile p : getAllPlayers()) {
                        p.getGameScoreboard().update(p.getPlayer().getScoreboard(), "%timeleft%", "" + "ENDED");
                    }
                }

            }
        }, 0, 20);

        for(PlayerProfile p : getAllPlayers()) {
            // tell everyone that the game has started
            p.getPlayer().sendTitle(ChatColor.GREEN + "Go!", "", 5, 20, 5);
            p.playSound(SoundEffect.GAME_START);

            p.getGameScoreboard().update(p.getPlayer().getScoreboard(), "%timeleft%", "" + 0);
        }

        setGameState(GameState.STARTED);
        Main.getInstance().getServer().getPluginManager().callEvent(new GameStartEvent(game));

    }

    public Set<PlayerProfile> getPlayers(boolean isPlaying) {
        Set<PlayerProfile> desired = new HashSet<>();
        allPlayers.forEach((player, playState) -> {
            if (playState.equals(isPlaying)) {
                desired.add(player);
            }
        });
        return desired;
    }

    public void addPlayer(PlayerProfile player, boolean setSpec) {
        allPlayers.put(player, !setSpec);
        if(!setSpec) {
            //player.setCurrentGame(this);
            player.createNewStats(this);
            player.setStatsBoard(StatsBoard.INGAME);
            Scoreboard bukkitBoard = player.getPlayer().getScoreboard();
            player.getGameScoreboard().update(bukkitBoard, "%arena%", arena.getName());
            player.getGameScoreboard().update(bukkitBoard, "%shots%", "" + 0);
            player.getGameScoreboard().update(bukkitBoard, "%kills%", "" + 0);
            player.getGameScoreboard().update(bukkitBoard, "%deaths%", "" + 0);
            player.getGameScoreboard().update(bukkitBoard, "%timeleft%", "NOT STARTED");

            Main.getInstance().getServer().getPluginManager().callEvent(new GamePlayerJoinEvent(player, this));
        } else {
            //player.setCurrentGame(this);
            setToSpectating(player);
        }
    }

    public void removePlayer(PlayerProfile player) {
        allPlayers.remove(player);

        Main.getInstance().getServer().getPluginManager().callEvent(new GamePlayerLeaveEvent(player, this));
    }

    public void setToSpectating(PlayerProfile player) {
        allPlayers.replace(player, false);
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
        Location specPoint = arena.getSpectatingPoint();
        player.getPlayer().teleport(specPoint);

        player.setStatsBoard(StatsBoard.SPECTATE);
        Scoreboard bukkitBoard = player.getPlayer().getScoreboard();
        player.getGameScoreboard().update(bukkitBoard, "%arena%", arena.getName());
        player.getGameScoreboard().update(bukkitBoard, "%timeleft%", "NOT STARTED");
        Main.getInstance().getServer().getPluginManager().callEvent(new GameSpectateEvent(player, this));
    }

    public void makeInvulnerable(Player player, int time) {
        player.setMetadata(invulnerableMeta, new FixedMetadataValue(Main.getInstance(), true));
        ItemStack helmet = new ItemStack(Material.GRAY_STAINED_GLASS);
        ItemMeta meta = helmet.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Paintball Helmet");
        helmet.setItemMeta(meta);
        player.getInventory().setHelmet(helmet);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

            @Override
            public void run() {
                makeVulnerable(player);
            }
        }, time);
    }

    public void makeVulnerable(Player player) {
        if(player.hasMetadata(invulnerableMeta)) {
            player.removeMetadata(invulnerableMeta, Main.getInstance());
            player.getInventory().setHelmet(null);
        }
    }

    public Set<PlayerProfile> getAllPlayers() {
        return allPlayers.keySet();
    }

    public Arena getUsedArena() {
        return arena;
    }

    public void endGame() {
        gameState = GameState.ENDED;

        Main.getInstance().getServer().getPluginManager().callEvent(new GameEndEvent(this));

        for(PlayerProfile p : getAllPlayers()) {
            p.getPlayer().sendTitle(ChatColor.GREEN + "Game Over!", "", 5, 20, 5);
            p.playSound(SoundEffect.GAME_END);
        }

        allGames.remove(this);
    }
}
