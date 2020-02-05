package net.darkscorner.paintball.objects.games;

import net.darkscorner.paintball.objects.arena.Arena;
import net.darkscorner.paintball.objects.Team;

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
}
