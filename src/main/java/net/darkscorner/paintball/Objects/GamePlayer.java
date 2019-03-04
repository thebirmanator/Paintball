package net.darkscorner.paintball.Objects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.Objects.Menus.Menu;
import net.darkscorner.paintball.Objects.Scoreboards.GameScoreboard;
import net.darkscorner.paintball.Objects.Scoreboards.StatsBoard;

public class GamePlayer {
	// a gameplayer contains a player, their kills for that game, their deaths for that game, and shots fired for that game
	private static Set<GamePlayer> gamePlayers = new HashSet<GamePlayer>();
	private UUID uuid;
	private PaintballGame game;
	private GameStatistics gameStats;
	private GameScoreboard scoreboard;
	private Paint paintColour = Paint.getDefaultPaint();
	private Menu viewingMenu;
	
	public GamePlayer(Player player) {
		uuid = player.getUniqueId();
		gamePlayers.add(this);
		
		setStatsBoard(StatsBoard.LOBBY);
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public boolean isInGame() {
		if(game != null) {
			return true;
		}
		return false;
	}

	public PaintballGame getCurrentGame() {
		return game;
	}
	
	public void setCurrentGame(PaintballGame game) {
		this.game = game;
	}
	
	public GameStatistics getStats() {
		return gameStats;
	}
	
	public GameScoreboard getGameScoreboard() {
		return scoreboard;
	}
	
	public void createNewStats() {
		gameStats = new GameStatistics();
	}
	
	public Paint getPaint() {
		return paintColour;
	}
	
	public void setPaint(Paint paint) {
		paintColour = paint;
	}
	
	public static GamePlayer getGamePlayer(Player player) {
		for(GamePlayer p : gamePlayers) {
			if(p.uuid.equals(player.getUniqueId())) {
				return p;
			}
		}
		return null;
	}
	
	public Menu getViewingMenu() {
		return viewingMenu;
	}
	
	public boolean isViewingMenu() {
		if(getViewingMenu() != null) {
			return true;
		}
		return false;
	}
	
	public void setViewingMenu(Menu menu) {
		viewingMenu = menu;
	}
	
	public void setStatsBoard(StatsBoard board) {
		this.scoreboard = GameScoreboard.getBoard(board);
		Scoreboard scoreboard = this.scoreboard.generateScoreboard();
		getPlayer().setScoreboard(scoreboard);
		this.scoreboard.update(scoreboard, "%player%", getPlayer().getName());
		this.scoreboard.update(scoreboard, "%crystals%", "" + Main.crystals.getCrystals(getPlayer().getName()));
	}

	
	public void playSound(SoundEffect effect) {
		switch (effect) {
		case BACKWARD_CLICK:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, (float) 0.8);
			break;
		case DEATH:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 1, (float) 1.5);
			break;
		case FORWARD_CLICK:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, (float) 1.2);
			break;
		case HIT:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 2);
			break;
		case RUN_COMMAND:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_CHICKEN_STEP, 1, (float) 1.8);
			break;
		case SHOOT:
			getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EGG_THROW, 1, (float) 0.7);
			break;
		case GAME_END:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, (float) 1.6);
			break;
		case GAME_START:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, (float) 1.6);
			break;
		case POWER_UP:
			getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, (float) 2);
			break;
		default:
			break;
		}
	}
	
	public void removePowerUps() {
		if(getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
			getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
		
		if(getPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
			getPlayer().removePotionEffect(PotionEffectType.JUMP);
		}
		
		if(getPlayer().hasMetadata("volleypowerup")) {
			getPlayer().removeMetadata("volleypowerup", Main.getPlugin(Main.class));
		}
	}
}
