package ltd.indigostudios.paintball.objects.scoreboards;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import ltd.indigostudios.paintball.objects.games.GameState;
import ltd.indigostudios.paintball.objects.games.TeamGame;
import ltd.indigostudios.paintball.objects.player.PlayerInGameStat;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.Text;

public enum Variables {

    PLAYER("%player%"), CURRENCY("%currency%"), ARENA("%arena%"), CURRENT_GAME_SHOTS("%shots%"),
    CURRENT_GAME_KILLS("%kills%"), CURRENT_GAME_DEATHS("%deaths%"), CURRENT_GAME_TIME_REMAINING("%timeleft%"),
    CURRENT_KDR("%kdr%"), CURRENT_ACCURACY("%accuracy%"),
    CURRENT_TEAM_NAME("%teamname%"), CURRENT_TEAM_KILLS("%teamkills%"), CURRENT_TEAM_DEATHS("%teamdeaths%");

    private final String variable;

    Variables(String variable) {
        this.variable = variable;
    }

    public String getAsString() {
        return variable;
    }

    public String getValue(PlayerProfile playerProfile) {
        GameSettings game = null;
        if (playerProfile.isInGame()) {
            game = playerProfile.getCurrentGame();
        }
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
                if (game.getGameState() == GameState.IDLE || game.getGameState() == GameState.COUNTDOWN) {
                    return "NOT STARTED";
                } else if (game.getGameState() == GameState.ENDED) {
                    return "ENDED";
                } else {
                    return Text.formatTime(playerProfile.getCurrentGame().getTimeRemaining());
                }
            case CURRENT_KDR:
                int deaths = playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.DEATHS);
                // Undefined KDR (no deaths)
                if (deaths == 0) { // undefined kda
                    return Text.format("&6PERFECT &7(no deaths)!");
                } else {
                    int kills = playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.KILLS);
                    float kdr = (float) kills / (float) deaths;
                    return String.format("%.2f", kdr);
                }
            case CURRENT_ACCURACY:
                int shots = playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.SHOTS);
                // Undefined accuracy (no shots)
                if (shots == 0) { // undefined accuracy
                    return Text.format("&6You know you're supposed to right click to shoot, right? (no shots fired)");
                } else {
                    int kills = playerProfile.getCurrentGameStats().getStat(PlayerInGameStat.KILLS);
                    float accuracy = (float) kills / (float) shots;
                    return String.format("%.2f", accuracy);
                }
            case CURRENT_TEAM_KILLS:
                if (game instanceof TeamGame) {
                    TeamGame teamGame = (TeamGame) game;
                    return teamGame.getTeam(playerProfile).getKills() + "";
                }
            case CURRENT_TEAM_NAME:
                if (game instanceof TeamGame) {
                    TeamGame teamGame = (TeamGame) game;
                    return teamGame.getTeam(playerProfile).getName();
                }
            case CURRENT_TEAM_DEATHS:
                if (game instanceof TeamGame) {
                    TeamGame teamGame = (TeamGame) game;
                    return teamGame.getTeam(playerProfile).getDeaths() + "";
                }
            default:
                return "INVALID";
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
