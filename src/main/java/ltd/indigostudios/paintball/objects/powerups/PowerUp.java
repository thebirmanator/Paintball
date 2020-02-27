package ltd.indigostudios.paintball.objects.powerups;

import ltd.indigostudios.paintball.Main;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerUp implements PowerUpSettings {

    private ConfigurationSection config;
    private FixedMetadataValue meta;
    protected static String powerUpMeta = "powerup";

    public PowerUp(String effectName) {
        config = mainConfig.getConfigurationSection(effectName);
        meta = new FixedMetadataValue(Main.getInstance(), effectName);
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public FixedMetadataValue getMeta() {
        return meta;
    }

    public static List<PowerUp> getPowerUps() {
        List<PowerUp> powerUps = new ArrayList<>();
        for (Effect effect : Effect.values()) {
            powerUps.add(effect.getPowerUp());
        }
        return powerUps;
    }

    public static boolean isPowerUpBlock(Block block) {
        return block.hasMetadata(powerUpMeta);
        /*
        for(PowerUp powerup : getPowerUps()) {
            if(block.getType() == powerup.getMaterial()) {
                return true;
            }
        }
        return false;*/
    }

    public static PowerUp getPowerUpBlock(Block block) {
        /*
        for(PowerUp powerup : getPowerUps()) {
            if(block.getType() == powerup.getMaterial()) {
                return powerup;
            }
        }*/
        String effectName = block.getMetadata(powerUpMeta).get(0).asString();
        return Effect.valueOf(effectName).getPowerUp();
    }

    public static void removeEffect(Player player, Effect effect) {
        effect.getPowerUp().remove(player);
    }

    public static void clearEffects(Player player) {
        for (Effect effect : Effect.values()) {
            if (effect.getPowerUp().hasEffect(player)) {
                effect.getPowerUp().remove(player);
            }
        }
    }

    public abstract boolean hasEffect(Player player);

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
