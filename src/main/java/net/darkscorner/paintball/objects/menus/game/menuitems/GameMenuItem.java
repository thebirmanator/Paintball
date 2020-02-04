package net.darkscorner.paintball.objects.menus.game.menuitems;

import net.darkscorner.paintball.objects.menus.ClickableItem;

import net.darkscorner.paintball.objects.menus.game.GameMenu;

public abstract class GameMenuItem extends ClickableItem {

	private GameMenu owningMenu;
	
	public GameMenuItem(GameMenu owningMenu) {
		this.owningMenu = owningMenu;
	}
	
	public GameMenu getOwningMenu() {
		return owningMenu;
	}

	/*
	public Game getAssociatedGame() {
		GameMenuItem item = this;
		int index = 0;
		while(item.getParent() != null || index < 10) {
			if(item instanceof GameItem) {
				GameItem gameItem = (GameItem) item;
				return gameItem.getGame();
			}
			item = item.getParent();
			index++;
		}
		return null;
	}*/
}
