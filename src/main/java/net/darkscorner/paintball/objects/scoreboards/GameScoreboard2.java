package net.darkscorner.paintball.objects.scoreboards;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameScoreboard2 {

    private PlayerProfile profile;
    private List<String> templateText;
    private Scoreboard scoreboard;

    private static Map<StatsBoard, List<String>> boardContents;
    private static FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "scoreboards.yml"));
    private static String title = config.getString("title");

    public GameScoreboard2(PlayerProfile profile, List<String> templateText) {
        this.profile = profile;
        this.templateText = templateText;
        generateScoreboard();
    }

    public GameScoreboard2(PlayerProfile profile, List<String> templateText, Scoreboard scoreboard) {
        this.profile = profile;
        this.templateText = templateText;
        this.scoreboard = scoreboard;
    }

    public void display(Player player) {
        player.setScoreboard(scoreboard);
    }

    public void update() {
        for (String text : templateText) {
            for (Variables variable : Variables.values()) {
                if (text.contains(variable.getAsString())) { // this line in the scoreboard contains the changed variable
                    int score = templateText.indexOf(text);
                    Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
                    for (String entry : objective.getScoreboard().getEntries()) {
                        if (objective.getScore(entry).getScore() == score) { // if score of entry is the same as score in template
                            objective.getScoreboard().resetScores(entry);
                            objective.getScore(text.replace(variable.getAsString(), variable.getValue(profile))).setScore(score);
                        }
                    }
                }
            }
        }
    }

    private void generateScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard gameScoreboard = manager.getNewScoreboard();
        Objective objective = gameScoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "Paintball");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for(int i = 0; i < templateText.size(); i++) {
            objective.getScore(templateText.get(i)).setScore(i);
        }
        scoreboard = gameScoreboard;
    }

    public static void loadBoardPresets() {
        boardContents = new HashMap<>();
        Set<String> boardTypes = config.getKeys(false);
        for(String boardType : boardTypes) {
            if(config.isList(boardType)) {
                if(StatsBoard.valueOf(boardType) != null) {
                    List<String> displayText = config.getStringList(boardType);
                    displayText.forEach((text) -> displayText.set(displayText.indexOf(text), Text.format(text)));
                    StatsBoard board = StatsBoard.valueOf(boardType);
                    boardContents.put(board, displayText);
                }
            }
        }
    }

    public static List<String> contentFor(StatsBoard board) {
        return boardContents.get(board);
    }
}
