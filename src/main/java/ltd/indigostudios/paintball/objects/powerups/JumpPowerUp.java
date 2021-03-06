package ltd.indigostudios.paintball.objects.powerups;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpPowerUp extends PowerUp {

    private static JumpPowerUp instance;

    private JumpPowerUp(String effectName) {
        super(effectName);
    }

    @Override
    public void use(Player player) {
        /*
        int duration = getDuration();
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            duration += player.getPotionEffect(PotionEffectType.JUMP).getDuration();
        }*/
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, getDuration(), 2), true);
    }

    @Override
    public void remove(Player player) {
        player.removePotionEffect(PotionEffectType.JUMP);
    }

    @Override
    public boolean hasEffect(Player player) {
        return player.hasPotionEffect(PotionEffectType.JUMP);
    }

    static JumpPowerUp getInstance(String effectName) {
        if (instance == null) {
            instance = new JumpPowerUp(effectName);
        }
        return instance;
    }
}
