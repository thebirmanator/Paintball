package net.darkscorner.paintball.objects.scoreboards;

import net.darkscorner.paintball.objects.games.GameSettings;
import net.darkscorner.paintball.objects.games.GameState;
import net.darkscorner.paintball.objects.player.PlayerInGameStat;
import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.utils.Text;

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
                GameSettings game = playerProfile.getCurrentGame();
                if (game.getGameState() == GameState.IDLE || game.getGameState() == GameState.COUNTDOWN) {
                    return "NOT STARTED";
                } else if (game.getGameState() == GameState.ENDED) {
                    return "ENDED";
                } else {
                    return Text.formatTime(playerProfile.getCurrentGame().getTimeRemaining());
                }
            default:
                return "Unknown";
        }
    }

    public static Variables fromString(String string) {
        for (Variables variable : values()) {
            if (variable.getAsString().equals(string)) {
                return variable;
            }
        }
        return null;
    }
}
