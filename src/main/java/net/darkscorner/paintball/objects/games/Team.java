package net.darkscorner.paintball.objects.games;

import net.darkscorner.paintball.objects.player.PlayerProfile;
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

    public ItemStack[] getArmourSet() {
        return armourSet;
    }
}
