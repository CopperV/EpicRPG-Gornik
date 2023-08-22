package me.Vark123.EpicRPGGornik.PlayerSystem.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PlayerManager.get().registerPlayer(e.getPlayer());
	}
	
}
