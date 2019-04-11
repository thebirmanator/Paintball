package net.darkscorner.paintball.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.themgrf.darkcrystals.events.CrystalChangeEvent;
import net.darkscorner.paintball.objects.GamePlayer;

public class CrystalChangeListener implements Listener {

	@EventHandler
	public void onChange(CrystalChangeEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer());
		GamePlayer gp = GamePlayer.getGamePlayer(player);
		gp.getGameScoreboard().update(player.getScoreboard(), "%crystals%", "" + event.getCrystals());
	}
}
