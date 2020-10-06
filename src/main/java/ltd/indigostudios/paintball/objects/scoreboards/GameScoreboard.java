package ltd.indigostudios.paintball.objects.scoreboards;

import com.google.common.collect.Lists;
import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameScoreboard {

    private PlayerProfile profile;
    private List<String> templateText;
    private Scoreboard scoreboard;

    private static Map<StatsBoard, List<String>> boardContents;
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "scoreboards.yml"));
    private static String title = config.getString("title");

    public GameScoreboard(PlayerProfile profile, List<String> templateText) {
        this.profile = profile;
        this.templateText = templateText;
        generateScoreboard();
    }

    public GameScoreboard(PlayerProfile profile, List<String> templateText, Scoreboard scoreboard) {
        this.profile = profile;
        this.templateText = templateText;
        this.scoreboard = scoreboard;
    }

    public static GameScoreboard getBoard(PlayerProfile profile, StatsBoard boardType) {
        if (profile.getPlayer().getScoreboard().getObjective("stats") == null) {
            return new GameScoreboard(profile, getContent(boardType));
        }
        return new GameScoreboard(profile, getContent(boardType), profile.getPlayer().getScoreboard());
    }

    public void display() {
        updateAll();
        profile.getPlayer().setScoreboard(scoreboard);
    }

    //TODO: find out why two lines that are the same except for the variable will only show the second one
    public void update(Variables variable) {
        for (String text : templateText) {
            if (text.contains(variable.getAsString())) {
                int score = templateText.indexOf(text);
                Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
                for (String entry : objective.getScoreboard().getEntries()) {
                    if (objective.getScore(entry).getScore() == score) { // if score of entry is the same as score in template
                        //Bukkit.broadcastMessage("found line");
                        objective.getScoreboard().resetScores(entry);
                        //Bukkit.broadcastMessage("reset line");
                        //Bukkit.broadcastMessage(text.replace(variable.getAsString(), variable.getValue(profile)));
                        objective.getScore(text.replace(variable.getAsString(), variable.getValue(profile))).setScore(score);
                    }
                }
            }
        }
    }

    public void updateAll() {
        for (Variables variable : Variables.values()) {
            update(variable);
        }
    }

    private void generateScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard gameScoreboard = manager.getNewScoreboard();
        Objective objective = gameScoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "Paintball");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < templateText.size(); i++) {
            objective.getScore(templateText.get(i)).setScore(i);
        }
        scoreboard = gameScoreboard;
    }

    public static List<String> getContent(StatsBoard boardType) {
        return boardContents.get(boardType);
    }

    public static void loadBoardPresets() {
        boardContents = new HashMap<>();
        Set<String> boardTypes = config.getKeys(false);
        for (String boardType : boardTypes) {
            if (config.isList(boardType)) {
                if (StatsBoard.valueOf(boardType) != null) {
                    // Get text in reverse because scoreboards are weird like that
                    List<String> displayText = Lists.reverse(config.getStringList(boardType));
                    displayText.forEach((text) -> displayText.set(displayText.indexOf(text), Text.format(text)));
                    StatsBoard board = StatsBoard.valueOf(boardType);
                    boardContents.put(board, displayText);
                }
            }
        }
    }
}
