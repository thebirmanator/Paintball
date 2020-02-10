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

    }

    default Gun getGun() {
        return Gun.Type.valueOf(getConfig().getString("equipped-gun")).getGun();
    }

    default void setGun(Gun gun) {
        getConfig().set("equipped-gun", gun.getType().name());
    }
}
