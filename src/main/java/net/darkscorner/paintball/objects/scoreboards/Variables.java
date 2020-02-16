package net.darkscorner.paintball.objects.scoreboards;

import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.player.PlayerProfile;

public enum Variables {

    PLAYER("%player%"), CURRENCY("%currency%"), ARENA("%arena%"), CURRENT_GAME_SHOTS("%shots%"),
    CURRENT_GAME_KILLS("%kills%"), CURRENT_GAME_DEATHS("%deaths%"), CURRENT_GAME_TIME_REMAINING("%timeleft%");

    private final String variable;

    Variables(String variable) {
        this.variable = variable;
    }

    public String getAsString() {
        return variable;
    }

    public String getValue(PlayerProfile playerProfile) {
        switch (this) {
            case PLAYER:
                return playerProfile.getPlayer().getName();
            case CURRENCY:
                return "0";
            case ARENA:
                return playerProfile.getCurrentGame().getArena().getName();
            case CURRENT_GAME_SHOTS:
                return playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.SHOTS) + "";
            case CURRENT_GAME_KILLS:
                return playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.KILLS) + "";
            case CURRENT_GAME_DEATHS:
                return playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.DEATHS) + "";
            case CURRENT_GAME_TIME_REMAINING:
                return "1";
            default:
                return "Unknown";
        }
    }
}
