package ltd.indigostudios.paintball.objects.games;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.events.*;
import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.objects.player.PlayerInGameStat;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.objects.scoreboards.GameScoreboard;
import ltd.indigostudios.paintball.objects.scoreboards.StatsBoard;
import ltd.indigostudios.paintball.objects.scoreboards.Variables;
import ltd.indigostudios.paintball.utils.SoundEffect;
import ltd.indigostudios.paintball.utils.Text;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.util.*;

public abstract class PaintballGame implements GameSettings {

    private GameState gameState = GameState.IDLE;
    // Maps player to if they are in game playing
    private Map<PlayerProfile, Boolean> allPlayers = new HashMap<>();
    private Arena arena;
    private static FileConfiguration config;

    private int currentTaskID = -1;
    private int currentGameTime = -1;

    // Game settings
    private static Set<Material> blacklisted;
    private static int startPlayerAmount;
    private static int maxPlayerAmount;
    private static int gameTime;
    private static Location lobbySpawn;
    private static int coinsForKill;
    private static int paintRadius;
    private static int respawnTime;
    private static Map<DeathType, List<String>> deathMsgs;
    private static List<String> cmdExceptions;

    public PaintballGame(Arena arena) {
        this.arena = arena;
        allGames.add(this);

        Main.getInstance().getServer().getPluginManager().callEvent(new GameCreateEvent(Main.getInstance(), this));
    }

    protected PaintballGame() {

    }

    public Arena getArena() {
        return arena;
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
            currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), getWaitingTask(), 0, 20);
        } else {
            Bukkit.getScheduler().cancelTask(currentTaskID);
        }
    }

    private Runnable getWaitingTask() {
        return () -> {
            if (getGameState() == GameState.IDLE && getPlayers(true).size() < getStartPlayerAmount()) {
                for (PlayerProfile p : getPlayers(true)) {
                    p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(Text.format("&6&lWaiting for &e" + (getStartPlayerAmount() - getPlayers(true).size()) + " &6player(s).")).create());
                }
            } else {
                waitForPlayers(false);
                // Has enough players to start game, commence countdown!
                if (getPlayers(true).size() >= getStartPlayerAmount()) {
                    countdown(true);
                }
            }
        };
    }

    public void countdown(boolean start) {
        if (start) {
            gameState = GameState.COUNTDOWN;
            currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), getCountdownTask(), 0, 20);
        } else {
            Bukkit.getScheduler().cancelTask(currentTaskID);
        }
    }

    private Runnable getCountdownTask() {
        return new Runnable() {
            int countdownTime = 20;

            @Override
            public void run() {
                if (getPlayers(true).size() >= getStartPlayerAmount()) {
                    if (countdownTime > 0) {
                        for (PlayerProfile p : getAllPlayers()) {
                            p.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(Text.format("&6&l" + countdownTime)).create());
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
        };
    }

    public void cancelCurrentTask() {
        Bukkit.getScheduler().cancelTask(currentTaskID);
        currentTaskID = -1;
    }

    public void startGame() {
        GameSettings game = this;
        currentGameTime = 0;
        currentTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            currentGameTime++;
            if (currentGameTime > getGameTimeLength()) {
                endGame();
                cancelCurrentTask();
            }
            // TODO: have each game have their statsboard?
            if (this instanceof FreeForAllGame) {
                getPlayers(true).forEach((player) -> GameScoreboard.getBoard(player, StatsBoard.FREE_FOR_ALL_GAME).update(Variables.CURRENT_GAME_TIME_REMAINING));
                getPlayers(false).forEach((player) -> GameScoreboard.getBoard(player, StatsBoard.SPECTATE).update(Variables.CURRENT_GAME_TIME_REMAINING));
            } else {
                getPlayers(true).forEach((player) -> GameScoreboard.getBoard(player, StatsBoard.TEAM_GAME).update(Variables.CURRENT_GAME_TIME_REMAINING));
                getPlayers(true).forEach((player) -> GameScoreboard.getBoard(player, StatsBoard.SPECTATE).update(Variables.CURRENT_GAME_TIME_REMAINING));
            }
        }, 0, 20);

        for (PlayerProfile p : getAllPlayers()) {
            // tell everyone that the game has started
            p.getPlayer().sendTitle(ChatColor.GREEN + "Go!", "", 5, 20, 5);
            p.playSound(SoundEffect.GAME_START);
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

    public boolean isPlaying(PlayerProfile playerProfile) {
        return allPlayers.get(playerProfile);
    }

    public void addPlayer(PlayerProfile player, boolean setSpec) {
        allPlayers.put(player, !setSpec);
        player.createNewStats(this, setSpec);
        if (!setSpec) {
            Main.getInstance().getServer().getPluginManager().callEvent(new GamePlayerJoinEvent(player, this));
        } else {
            setToSpectating(player);
        }
    }

    public void removePlayer(PlayerProfile player) {
        Main.getInstance().getServer().getPluginManager().callEvent(new GamePlayerLeaveEvent(player, this));
        allPlayers.remove(player);
        player.clearCurrentGameStats();
    }

    public void setToSpectating(PlayerProfile player) {
        allPlayers.replace(player, false);
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
        Location specPoint = arena.getSpectatingPoint();
        player.getPlayer().teleport(specPoint);

        Main.getInstance().getServer().getPluginManager().callEvent(new GameSpectateEvent(player, this));
    }

    //TODO: make use of this
    public void makeInvulnerable(Player player, int time) {
        player.setMetadata(invulnerableMeta, new FixedMetadataValue(Main.getInstance(), true));
        //ItemStack helmet = new ItemStack(Material.GRAY_STAINED_GLASS);
        //ItemMeta meta = helmet.getItemMeta();
        //meta.setDisplayName(ChatColor.RED + "Paintball Helmet");
        //helmet.setItemMeta(meta);
        //player.getInventory().setHelmet(helmet);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> makeVulnerable(player), time);
    }

    public boolean isVulnerable(Player player) {
        return !player.hasMetadata(invulnerableMeta);
    }

    public void makeVulnerable(Player player) {
        if (player.hasMetadata(invulnerableMeta)) {
            player.removeMetadata(invulnerableMeta, Main.getInstance());
            //player.getInventory().setHelmet(null);
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
        allGames.remove(this);
    }

    // Settings overrides
    @Override
    public FileConfiguration getGameConfig() {
        return config;
    }

    @Override
    public Set<Material> getUnpaintableMaterials() {
        return blacklisted;
    }

    @Override
    public int getStartPlayerAmount() {
        return startPlayerAmount;
    }

    @Override
    public int getMaxPlayerAmount() {
        return maxPlayerAmount;
    }

    @Override
    public int getGameTimeLength() {
        return gameTime;
    }

    @Override
    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    @Override
    public int getCoinsPerKill() {
        return coinsForKill;
    }

    @Override
    public int getPaintRadius() {
        return paintRadius;
    }

    @Override
    public int getRespawnTime() {
        return respawnTime;
    }

    @Override
    public List<String> getDeathMsgs(DeathType type) {
        return deathMsgs.get(type);
    }

    @Override
    public int getTimeRemaining() {
        return getGameTimeLength() - currentGameTime;
    }

    public List<PlayerProfile> sortByStat(PlayerInGameStat stat) {
        // Sort the players from highest to lowest score
        List<PlayerProfile> players = new ArrayList<>(getPlayers(true));
        players.sort((p1, p2) -> Integer.compare(p2.getCurrentGameStats().getStat(stat), p1.getCurrentGameStats().getStat(stat)));
        return players;
    }

    protected void loadSettings() {
        config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "main.yml"));
        blacklisted = GameSettings.super.getUnpaintableMaterials();
        startPlayerAmount = GameSettings.super.getStartPlayerAmount();
        maxPlayerAmount = GameSettings.super.getMaxPlayerAmount();
        gameTime = GameSettings.super.getGameTimeLength();
        lobbySpawn = GameSettings.super.getLobbySpawn();
        coinsForKill = GameSettings.super.getCoinsPerKill();
        paintRadius = GameSettings.super.getPaintRadius();
        respawnTime = GameSettings.super.getRespawnTime();
        deathMsgs = new HashMap<>();
        for (DeathType type : DeathType.values()) {
            deathMsgs.put(type, GameSettings.super.getDeathMsgs(type));
        }
        cmdExceptions = GameSettings.super.commandExceptions();
    }
}
