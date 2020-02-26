package net.darkscorner.paintball.objects.player;

import net.darkscorner.paintball.objects.equippable.guns.Gun;
import net.darkscorner.paintball.objects.equippable.paint.Paint;
import org.bukkit.configuration.file.FileConfiguration;

public interface PlayerSettings {

    FileConfiguration getConfig();

    default Paint getPaint() {
        Paint paint = Paint.getPaint(getConfig().getString("equipped-paint"));
        if (paint == null) {
            paint = Paint.getDefaultPaint();
        }
        return paint;
    }

    default void setPaint(Paint paint) {
        getConfig().set("equipped-paint", paint.getName());
    }

    default Gun getGun() {
        return Gun.Type.valueOf(getConfig().getString("equipped-gun")).getGun();
    }

    default void setGun(Gun gun) {
        getConfig().set("equipped-gun", gun.getType().name());
    }

    default int getMoney() {
        return getConfig().getInt("money", 0);
    }

    default void setMoney(int amount) {
        getConfig().set("money", amount);
    }

    default void addMoney(int amount) {
        setMoney(getMoney() + amount);
    }

    default void subtractMoney(int amount) {
        int newAmount = getMoney() - amount;
        if (newAmount < 0) newAmount = 0;
        setMoney(newAmount);
    }
}
