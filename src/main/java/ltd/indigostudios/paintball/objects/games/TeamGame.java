package ltd.indigostudios.paintball.objects.games;

import ltd.indigostudios.paintball.objects.arena.Arena;
import ltd.indigostudios.paintball.objects.player.PlayerProfile;
import ltd.indigostudios.paintball.utils.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeamGame extends PaintballGame {
    private Set<Team> teams;
    private static String[] summary;

    public TeamGame(Arena arena, Set<Team> teams) {
        super(arena);
        this.teams = teams;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    @Override
    public void addPlayer(PlayerProfile player, boolean setSpec) {
        if (!setSpec) {
            Team first = getTeams().iterator().next();

            int teamSize = first.getMembers().size();
            boolean foundTeam = false;
            for (Team team : getTeams()) {
                if (team.getMembers().size() < teamSize) {
                    team.addMember(player);
                    foundTeam = true;
                    break;
                }
            }
            // All teams have same amount (or the first team has the fewest).
            // Add to first team
            if (!foundTeam) {
                first.addMember(player);
            }
        }
        super.addPlayer(player, setSpec);
    }

    @Override
    public void removePlayer(PlayerProfile player) {
        super.removePlayer(player);
        for (Team team : teams) {
            if (team.hasPlayer(player)) {
                team.removeMember(player);
            }
        }
    }

    @Override
    public void setToSpectating(PlayerProfile player) {
        super.setToSpectating(player);
        for (Team team : teams) {
            if (team.hasPlayer(player)) {
                team.removeMember(player);
            }
        }
    }

    public Team getTeam(PlayerProfile playerProfile) {
        for (Team team : getTeams()) {
            if (team.hasPlayer(playerProfile)) {
                return team;
            }
        }
        return null;
    }

    @Override
    public String[] getGameSummary() {
        if (summary == null) {
            List<String> summaryList = getGameConfig().getStringList("summary.team-game");
            summary = new String[summaryList.size()];
            for (String configSummary : summaryList) {
                summary[summaryList.indexOf(configSummary)] = Text.format(configSummary);
            }
        }
        return summary;
    }

    public List<Team> sortByKills() {
        List<Team> sortedTeams = new ArrayList<>(getTeams());
        sortedTeams.sort((t1, t2) -> Integer.compare(t2.getKills(), t1.getKills()));
        return sortedTeams;
    }
}
