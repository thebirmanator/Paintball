package ltd.indigostudios.paintball.objects.player;

public enum PlayerStat {

    SHOTS("shots"), KILLS("kills"), DEATHS("deaths"), GAMES_PLAYED("games-played");

    private final String stat;

    PlayerStat(String stat) {
        this.stat = stat;
    }

    public String getStatPath() {
        return stat;
    }
}
