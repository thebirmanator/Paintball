package net.darkscorner.paintball.objects.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public interface IScoreboard {

    StatsBoard getType();

    default void displayScoreboard(Player player) {
        player.setScoreboard(getScoreboard());
    }

    default Scoreboard getScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard gameScoreboard = manager.getNewScoreboard();
        Objective objective = gameScoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "Paintball");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < getType().getContents().size(); i++) {
            objective.getScore(getType().getContents().get(i)).setScore(i);
        }
        return gameScoreboard;
    }

    default void update(String variable, String newValue) {
        List<String> displayText = getType().getContents();
        for (String text : displayText) {
            if (text.contains(variable)) { // this line in the scoreboard contains the changed variable
                int score = displayText.indexOf(text);
                Objective objective = getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                for (String entry : objective.getScoreboard().getEntries()) {
                    if (objective.getScore(entry).getScore() == score) { // if score of entry is the same as score in template
                        objective.getScoreboard().resetScores(entry);
                        objective.getScore(text.replace(variable, newValue)).setScore(score);
                    }
                }
            }
        }
    }
}
