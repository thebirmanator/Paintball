package net.darkscorner.paintball.objects.games;

import net.darkscorner.paintball.objects.arena.Arena;
import net.darkscorner.paintball.objects.Team;
import net.darkscorner.paintball.objects.player.PlayerProfile;

import java.util.Set;

public class TeamGame extends BasePaintballGame {
    private Set<Team> teams;

    public TeamGame(Arena arena, Set<Team> teams) {
        super(arena);
        this.teams = teams;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    @Override
    public void addPlayer(PlayerProfile player, boolean setSpec) {
        super.addPlayer(player, setSpec);
        if (!setSpec) {
            Team first = getTeams().iterator().next();

            int teamSize = first.getMembers().size();
            for (Team team : getTeams()) {
                if (team.getMembers().size() < teamSize) {
                    team.addMember(player);
                    return;
                }
            }
            // All teams have same amount (or the first team has the fewest).
            // Add to first team
            first.addMember(player);
        }
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
}
