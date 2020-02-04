package net.darkscorner.paintball.objects;

import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.player.PlayerProfile;

import java.util.Set;

public class Team {

    private String name;
    private Set<PlayerProfile> members;

    private Team(String name, Set<PlayerProfile> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Set<PlayerProfile> getMembers() {
        return members;
    }

    public static Team createTeam(String name, Game game, Set<PlayerProfile> members) {
        return new Team(name, members);
    }
}
