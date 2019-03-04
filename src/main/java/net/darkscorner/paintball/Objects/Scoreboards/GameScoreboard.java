package net.darkscorner.paintball.objects.scoreboards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.google.common.collect.Lists;

public class GameScoreboard {

	private List<String> displayText;
	private StatsBoard type;
	
	private static Set<GameScoreboard> boards = new HashSet<GameScoreboard>();
	
	private GameScoreboard(String title, List<String> displayText, StatsBoard type) {
		/*
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard gameScoreboard = manager.getNewScoreboard();
		objective = gameScoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "Paintball");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		*/
		this.displayText = new ArrayList<String>(Lists.reverse(displayText));
		/*
		for(int i = 0; i < this.displayText.size(); i++) {
			objective.getScore(this.displayText.get(i)).setScore(i);
		}
		*/
		this.type = type;
		
		boards.add(this);
	}
	
	public List<String> getTemplateDisplayText() {
		return displayText;
	}
	
	public StatsBoard getType() {
		return type;
	}
	
	public Scoreboard generateScoreboard() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard gameScoreboard = manager.getNewScoreboard();
		Objective objective = gameScoreboard.registerNewObjective("stats", "dummy", ChatColor.GOLD + "" + ChatColor.BOLD + "Paintball");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(int i = 0; i < this.displayText.size(); i++) {
			objective.getScore(this.displayText.get(i)).setScore(i);
		}
		
		return gameScoreboard;
	}
	
	public void update(Scoreboard scoreboard, String variable, String newValue) {
		for(String text : displayText) {
			if(text.contains(variable)) { // this line in the scoreboard contains the changed variable
				int score = displayText.indexOf(text);
				Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
				for(String entry : objective.getScoreboard().getEntries()) {
					if(objective.getScore(entry).getScore() == score) { // if score of entry is the same as score in template
						objective.getScoreboard().resetScores(entry);
						objective.getScore(text.replace(variable, newValue)).setScore(score);
					}
				}
			}
		}
	}
	
	public static GameScoreboard getBoard(StatsBoard boardType) {
		for(GameScoreboard board : boards) {
			if(board.getType() == boardType) {
				return board;
			}
		}
		return null;
	}
	
	public static void createFromConfig(FileConfiguration config) {
		String title = config.getString("title");
		title = ChatColor.translateAlternateColorCodes('&', title);
		
		Set<String> boardTypes = config.getKeys(false);
		
		for(String boardType : boardTypes) {
			if(config.isList(boardType)) {
				if(StatsBoard.valueOf(boardType) != null) {
					List<String> displayText = config.getStringList(boardType);
					for(int i = 0; i < displayText.size(); i++) {
						String text = displayText.get(i);
						text = ChatColor.translateAlternateColorCodes('&', text);
						displayText.set(i, text);
					}
					StatsBoard board = StatsBoard.valueOf(boardType);
					new GameScoreboard(title, displayText, board);
				}
			}
		}
	}
}
