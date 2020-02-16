package net.darkscorner.paintball.objects.player;

public enum PlayerInGameStat {

    SHOTS("shots"), KILLS("kills"), DEATHS("deaths"), BIGGEST_KILLSTREAK("biggest-killstreak");

    private final String stat;

    PlayerInGameStat(String stat) {
        this.stat = stat;
    }

    public String getStatPath() {
        return stat;
    }

    public String toPlaceholder() {
        return "%" + stat + "%";
    }
}
