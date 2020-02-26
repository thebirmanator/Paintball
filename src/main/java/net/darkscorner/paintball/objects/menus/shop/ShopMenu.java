package net.darkscorner.paintball.objects.menus.shop;

import net.darkscorner.paintball.objects.menus.ChestBasedMenu;

public class ShopMenu extends ChestBasedMenu {

    public ShopMenu(String name, int size) {
        super(name, size);
    }

    public ShopMenu(String name, ShopMenu parent, int size) {
        super(name, parent, size);
    }

    @Override
    public void showNavBar(boolean show) {

    }
}
