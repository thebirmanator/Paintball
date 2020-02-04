package net.darkscorner.paintball.objects.menus.game.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.menus.game.GameMenu;

public class GameItem extends GameMenuItem {

	private Game game;
	// main game options menu; shows all games going on
	public GameItem(Game game) {
		super(null);
		// no parent for this menu
		
		this.game = game;
	}
	
	@Override
	public void use(Player player, ClickType click) {
		switch (click) {
		case LEFT: // send to spectate a game
			PlayerProfile gp = PlayerProfile.getGamePlayer(player);
			gp.playSound(SoundEffect.FORWARD_CLICK);
			if(!gp.isInGame()) {
				game.addPlayer(gp, true);
				//game.setToSpectating(gp);
			} else {
				if(gp.getCurrentGame().equals(game)) {
					player.sendMessage(Main.prefix + "You cannot spectate a game that you're already in.");
				} else {
					gp.getCurrentGame().removePlayer(gp);
					game.addPlayer(gp, true);
					//game.setToSpectating(gp);
					
				}
			}
			getOwningMenu().close(player);
			break;
		case RIGHT: // open player list
			GameMenu playerOptionsMenu = new GameMenu("Players", getOwningMenu(), 27);
			//GameMenu playerOptionsGameMenu = new GameMenu("Players", this, 27);
			int index = 0;
			for (PlayerProfile playerProfile : game.getPlayers(true)) {
				ClickableItem clickableItem = new PlayerOptionsItem(playerProfile.getPlayer(), playerOptionsMenu).getForPlayer(player);
				playerOptionsMenu.addItem(index, clickableItem);
				index++;
			}
			getOwningMenu().close(player);
			playerOptionsMenu.open(player);
			break;
		case MIDDLE: // open game options
			if (player.hasPermission("paintball.options.use")) {
				GameMenu gameOptionsMenu = new GameMenu("Game Options", getOwningMenu(), 9);
				ClickableItem endGameItem = new EndGameItem(gameOptionsMenu).getForPlayer(player);
				gameOptionsMenu.addItem(0, endGameItem);
				getOwningMenu().close(player);
				gameOptionsMenu.open(player);
			} else {
				break;
			}
		default:
			break;
		}
	}

	@Override
	public ClickableItem getForPlayer(Player player) {
		return null;
	}

	@Override
	public void createItem() {

	}

	public Game getGame() {
		return game;
	}

}
