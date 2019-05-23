package net.darkscorner.paintball.listeners;

import me.themgrf.arcadecoinsapi.events.ArcadeCoinsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.darkscorner.paintball.objects.GamePlayer;

public class CoinChangeListener implements Listener {

	@EventHandler
	public void onChange(ArcadeCoinsChangeEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer());
		GamePlayer gp = GamePlayer.getGamePlayer(player);
		gp.getGameScoreboard().update(player.getScoreboard(), "%crystals%", "" + event.getCoins());
	}
}
