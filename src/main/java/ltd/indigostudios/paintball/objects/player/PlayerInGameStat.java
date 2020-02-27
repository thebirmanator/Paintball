package ltd.indigostudios.paintball.objects.player;

import ltd.indigostudios.paintball.objects.scoreboards.Variables;

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

    public Variables toVariable() {
        return Variables.fromString(toPlaceholder());
    }
}
