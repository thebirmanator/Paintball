package ltd.indigostudios.paintball.commands;

import ltd.indigostudios.paintball.Main;
import ltd.indigostudios.paintball.objects.menus.shop.ShopMenu;
import ltd.indigostudios.paintball.objects.menus.shop.items.BuyGunCategory;
import ltd.indigostudios.paintball.objects.menus.shop.items.BuyPaintCategory;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaintballCmd implements CommandExecutor {

    public String paintballcmd = "paintball";
    private String[] helpMsg = {    Main.prefix + "Help",
                                    Text.format("&a/pb join &f- join a game"),
                                    Text.format("&a/pb leave &f- leave a game"),
                                    Text.format("&a/pb paint &f- view and set your paint"),
                                    Text.format("&a/pb guns &f- view and set your gun"),
                                    Text.format("&a/pb shop &f- buy new guns and paint"),
                                    Text.format("&a/pb bal &f- check your Paintball balance (that you can use at &7/pb shop&f)"),
                                    Text.format("&a/pb games &f- view various game options"),
                                    Text.format("&a/pb stats &f- view your stats"),};

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "shop": // Opening the shop
                        ShopMenu mainMenu = new ShopMenu("Shop", 54);
                        mainMenu.addItem(0, new BuyGunCategory(mainMenu));
                        mainMenu.addItem(1, new BuyPaintCategory(mainMenu));
                        mainMenu.open(player);
                        break;
                    case "bal": // Viewing their Paintball balance
                        player.sendMessage(Text.format(Main.prefix + "Your balance: &a$" + PlayerProfile.getGamePlayer(player).getMoney()));
                        break;
                    case "join":
                        player.performCommand("join");
                        break;
                    case "leave":
                        player.performCommand("leave");
                        break;
                    case "paint":
                        player.performCommand("paint");
                        break;
                    case "guns":
                        player.performCommand("guns");
                        break;
                    case "games":
                        player.performCommand("games");
                        break;
                    case "spec":
                        player.performCommand("spec");
                        break;
                    case "stats":
                        player.performCommand("viewstats");
                        break;
                    default:
                        player.sendMessage(helpMsg);
                        break;
                }
            }
        }
        return true;
    }
}
