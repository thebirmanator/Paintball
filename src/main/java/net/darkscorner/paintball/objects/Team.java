package net.darkscorner.paintball.objects;

import net.darkscorner.paintball.objects.games.Game;
import net.darkscorner.paintball.objects.player.PlayerProfile;

import java.util.Set;

public class Team {

    private String name;
    private Set<PlayerProfile> members;

    public Team(String name, Set<PlayerProfile> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Set<PlayerProfile> getMembers() {
        return members;
    }

    public void addMember(PlayerProfile playerProfile) {
        members.add(playerProfile);
    }

    public void removeMember(PlayerProfile playerProfile) {
        members.remove(playerProfile);
    }

    public boolean hasPlayer(PlayerProfile playerProfile) {
        return members.contains(playerProfile);
    }
}
