package net.darkscorner.paintball.objects.powerups;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerUp extends PowerUp {

    private static SpeedPowerUp instance;

    private SpeedPowerUp(String effectName) {
        super(effectName);
    }

    @Override
    public void use(Player player) {
        int duration = getDuration();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            duration = duration + player.getPotionEffect(PotionEffectType.SPEED).getDuration();
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 2), true);
    }

    static SpeedPowerUp getInstance(String effectName) {
        if (instance == null) {
            instance = new SpeedPowerUp(effectName);
        }
        return instance;
    }
}
