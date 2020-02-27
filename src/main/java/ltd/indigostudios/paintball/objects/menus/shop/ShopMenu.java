package ltd.indigostudios.paintball.objects.menus.shop;

import ltd.indigostudios.paintball.objects.menus.ChestBasedMenu;
import ltd.indigostudios.paintball.objects.menus.game.items.BackMenuItem;

public class ShopMenu extends ChestBasedMenu {

    public ShopMenu(String name, int size) {
        super(name, size);
    }

    public ShopMenu(String name, ShopMenu parent, int size) {
        super(name, parent, size);
    }

    @Override
    public void showNavBar(boolean show) {
        if (getParent() != null) {
            if (show) {
                BackMenuItem backItem = new BackMenuItem(this, getParent());
                getItems().put(getSize() - 1, backItem);
            } else {
                getItems().remove(getSize() - 1);
            }
        }
    }
}
