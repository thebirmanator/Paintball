package net.darkscorner.paintball.objects.powerups;

import net.darkscorner.paintball.Main;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerUp implements PowerUpSettings {

    private ConfigurationSection config;

    public PowerUp(String effectName) {
        config = mainConfig.getConfigurationSection(effectName);
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    public static List<PowerUp> getPowerUps() {
        List<PowerUp> powerUps = new ArrayList<>();
        for (Effect effect : Effect.values()) {
            powerUps.add(effect.getPowerUp());
        }
        return powerUps;
    }

    public static boolean isPowerUpBlock(Block block) {
        for(PowerUp powerup : getPowerUps()) {
            if(block.getType() == powerup.getMaterial()) {
                return true;
            }
        }
        return false;
    }

    public static PowerUp getPowerUpBlock(Block block) {
        for(PowerUp powerup : getPowerUps()) {
            if(block.getType() == powerup.getMaterial()) {
                return powerup;
            }
        }
        return null;
    }

    public enum Effect {
        JUMP(JumpPowerUp.getInstance("JUMP")), SPEED(SpeedPowerUp.getInstance("SPEED")),
        VOLLEY(VolleyPowerUp.getInstance("VOLLEY"));

        private final PowerUp powerUp;

        Effect(PowerUp powerUp) {
            this.powerUp = powerUp;
        }

        public PowerUp getPowerUp() {
            return powerUp;
        }
    }
}
