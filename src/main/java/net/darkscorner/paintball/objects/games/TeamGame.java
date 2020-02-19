package net.darkscorner.paintball.objects.games;

import net.darkscorner.paintball.objects.arena.Arena;
import net.darkscorner.paintball.objects.player.PlayerProfile;

import java.util.Set;

public class TeamGame extends PaintballGame {
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
        super.addPlayer(player, false);
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
