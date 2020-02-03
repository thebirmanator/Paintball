package net.darkscorner.paintball.objects;

import net.darkscorner.paintball.objects.games.Game;

import java.util.Set;

public class Team {

    private String name;
    private Set<GamePlayer> members;

    private Team(String name, Set<GamePlayer> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Set<GamePlayer> getMembers() {
        return members;
    }

    public static Team createTeam(String name, Game game, Set<GamePlayer> members) {
        return new Team(name, members);
    }
}
