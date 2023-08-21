package me.Vark123.EpicRPGGornik.PlayerSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		PlayerManager.get().unregisterPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		PlayerManager.get().unregisterPlayer(e.getPlayer());
	}
	
}
