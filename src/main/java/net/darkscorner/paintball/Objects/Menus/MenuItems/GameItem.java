package net.darkscorner.paintball.Objects.Menus.MenuItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.darkscorner.paintball.Main;
import net.darkscorner.paintball.SoundEffect;
import net.darkscorner.paintball.Objects.GamePlayer;
import net.darkscorner.paintball.Objects.PaintballGame;
import net.darkscorner.paintball.Objects.Menus.Menu;

public class GameItem extends MenuItem {

	private PaintballGame game;
	// main game options menu; shows all games going on
	public GameItem(ItemStack icon, PaintballGame game) {
		// no parent for this menu
		super(null, icon);
		
		this.game = game;
	}
	
	@Override
	public void open(Player player, ClickType click) {
		switch (click) {
		case LEFT: // send to spectate a game
			GamePlayer gp = GamePlayer.getGamePlayer(player);
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
			getContainedIn().closeMenu(player);
			break;
		case RIGHT: // open player list
			Menu playerOptionsMenu = new Menu("Players", this, 27);
			int index = 0;
			for(GamePlayer gamePlayer : game.getInGamePlayers()) {
				ItemStack icon = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) icon.getItemMeta();
				meta.setOwningPlayer(gamePlayer.getPlayer());
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.WHITE + "Left-click" + ChatColor.GRAY + " to kick player from this game.");
				meta.setLore(lore);
				icon.setItemMeta(meta);
				PlayerOptionsItem optionsItem = new PlayerOptionsItem(this, icon);
				playerOptionsMenu.addButton(index, optionsItem);
				index++;
			}
			getContainedIn().closeMenu(player);
			playerOptionsMenu.openMenu(player);
			break;
		case MIDDLE: // open game options
			if(player.hasPermission("paintball.options.use")) {
				Menu gameOptionsMenu = new Menu("Game Options", this, 9);
				
				ItemStack endGameIcon = new ItemStack(Material.BEDROCK);
				ItemMeta endGameMeta = endGameIcon.getItemMeta();
				endGameMeta.setDisplayName(ChatColor.RED + "End Game");
				List<String> endGameLore = new ArrayList<>();
				endGameLore.add(ChatColor.WHITE + "Left-click" + ChatColor.GRAY + " to force-end this game.");
				endGameMeta.setLore(endGameLore);
				endGameIcon.setItemMeta(endGameMeta);
				EndGameItem endGameItem = new EndGameItem(this, endGameIcon);
				gameOptionsMenu.addButton(0, endGameItem);
				getContainedIn().closeMenu(player);
				gameOptionsMenu.openMenu(player);
			} else {
				break;
			}
		default:
			break;
		}
	}
	
	public PaintballGame getGame() {
		return game;
	}

}
