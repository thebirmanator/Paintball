package net.darkscorner.paintball;

public enum PlayerStat {

    SHOTS(0), KILLS(1), DEATHS(2), GAMES(3);

    private final int stat;

    PlayerStat(int effect) {
        this.stat = effect;
    }

    public int getStat() {
        return stat;
    }
}
