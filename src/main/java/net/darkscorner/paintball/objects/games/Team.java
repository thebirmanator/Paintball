package net.darkscorner.paintball.objects.games;

import net.darkscorner.paintball.objects.player.PlayerProfile;
import net.darkscorner.paintball.objects.scoreboards.GameScoreboard;
import net.darkscorner.paintball.objects.scoreboards.StatsBoard;
import net.darkscorner.paintball.objects.scoreboards.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Set;

public class Team {

    private String name;
    private Set<PlayerProfile> members;
    private Color colour;
    private ItemStack[] armourSet;
    private int kills = 0, deaths = 0;

    private ItemStack[] baseArmour = {  new ItemStack(Material.LEATHER_BOOTS),
                                        new ItemStack(Material.LEATHER_LEGGINGS),
                                        new ItemStack(Material.LEATHER_CHESTPLATE),
                                        new ItemStack(Material.LEATHER_HELMET)};

    public Team(String name, Color colour, Set<PlayerProfile> members) {
        this.name = name;
        this.members = members;
        this.colour = colour;
        createArmourSet();
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return ChatColor.stripColor(name);
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

    private void createArmourSet() {
        ItemStack[] armourSet = new ItemStack[4];
        for (int i = 0; i < armourSet.length; i++) {
            ItemStack item = baseArmour[i].clone();
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(colour);
            item.setItemMeta(meta);
            armourSet[i] = item;
        }
        this.armourSet = armourSet;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int amount) {
        kills = amount;
        for (PlayerProfile p : getMembers()) {
            GameScoreboard.getBoard(p, StatsBoard.TEAM_GAME).update(Variables.CURRENT_TEAM_KILLS);
        }
    }

    public void addKills(int amount) {
        amount = kills + amount;
        setKills(amount);
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int amount) {
        deaths = amount;
        for (PlayerProfile p : getMembers()) {
            GameScoreboard.getBoard(p, StatsBoard.TEAM_GAME).update(Variables.CURRENT_TEAM_DEATHS);
        }
    }

    public void addDeaths(int amount) {
        amount = deaths + amount;
        setDeaths(amount);
    }

    public ItemStack[] getArmourSet() {
        return armourSet;
    }
}
