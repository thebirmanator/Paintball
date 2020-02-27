package ltd.indigostudios.paintball.objects.menus.game.items;

import ltd.indigostudios.paintball.objects.menus.game.GameMenu;
import ltd.indigostudios.paintball.objects.menus.ClickableItem;

public abstract class GameMenuItem extends ClickableItem {
	
	public GameMenuItem(GameMenu owningMenu) {
		super(owningMenu);
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
