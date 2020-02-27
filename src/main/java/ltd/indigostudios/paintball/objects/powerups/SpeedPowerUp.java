package ltd.indigostudios.paintball.objects.powerups;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerUp extends PowerUp {

    private static SpeedPowerUp instance;

    private SpeedPowerUp(String effectName) {
        super(effectName);
    }

    @Override
    public boolean hasEffect(Player player) {
        return player.hasPotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void use(Player player) {
        /*
        int duration = getDuration();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            duration = duration + player.getPotionEffect(PotionEffectType.SPEED).getDuration();
        }*/
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, getDuration(), 2), true);
    }

    @Override
    public void remove(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    static SpeedPowerUp getInstance(String effectName) {
        if (instance == null) {
            instance = new SpeedPowerUp(effectName);
        }
        return instance;
    }
}
