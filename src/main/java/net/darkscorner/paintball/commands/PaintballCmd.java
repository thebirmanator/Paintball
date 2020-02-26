package net.darkscorner.paintball.commands;

import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.equippable.paint.Paint;
import net.darkscorner.paintball.objects.menus.shop.ShopMenu;
import net.darkscorner.paintball.objects.menus.shop.items.BuyGunCategory;
import net.darkscorner.paintball.objects.menus.shop.items.BuyGunItem;
import net.darkscorner.paintball.objects.menus.shop.items.BuyPaintCategory;
import net.darkscorner.paintball.objects.menus.shop.items.BuyPaintItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballCmd implements CommandExecutor {

    public String paintballcmd = "paintball";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "shop":
                        /*
                        ShopMenu paintShop = new ShopMenu("Paint", 54);
                        int index = 0;
                        for (Paint paint : Paint.getAllCustomPaints()) {
                            paintShop.addItem(index, new BuyPaintItem(paint, paintShop, 10).getForPlayer(player));
                            index++;
                        }
                        paintShop.open(player);
                        return true;
                        ShopMenu gunShop = new ShopMenu("Paint", 54);
                        int index = 0;
                        for (Gun gun : Gun.getGuns()) {
                            if (gun.getType() != Gun.Type.STANDARD) {
                                gunShop.addItem(index, new BuyGunItem(gun, gunShop, 10).getForPlayer(player));
                                index++;
                            }
                        }
                        gunShop.open(player);
                        return true;*/
                        ShopMenu mainMenu = new ShopMenu("Shop", 54);
                        mainMenu.addItem(0, new BuyGunCategory(mainMenu));
                        mainMenu.addItem(1, new BuyPaintCategory(mainMenu));
                        mainMenu.open(player);
                }
            }
        }
        return true;
    }
}
