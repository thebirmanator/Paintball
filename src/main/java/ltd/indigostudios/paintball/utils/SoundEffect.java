package ltd.indigostudios.paintball.utils;

public enum SoundEffect {

    FORWARD_CLICK(0), BACKWARD_CLICK(1), RUN_COMMAND(2), SHOOT(3), HIT(4), DEATH(5), GAME_START(6), GAME_END(7), POWER_UP(8);

    private final int effect;

    SoundEffect(int effect) {
        this.effect = effect;
    }

    public int getEffect() {
        return effect;
    }
}
