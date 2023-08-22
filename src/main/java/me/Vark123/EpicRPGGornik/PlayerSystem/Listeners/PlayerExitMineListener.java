package me.Vark123.EpicRPGGornik.PlayerSystem.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;

public class PlayerExitMineListener implements Listener {
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		if(!e.getFrom().getName().equals("F_RPG_Cave"))
			return;
		
		p.getActivePotionEffects().forEach(effect -> {
			p.removePotionEffect(effect.getType());
		});
		PlayerManager.get().getPlayer(p).ifPresent(miner -> {
			miner.getCatMiner().getOxygenBar().setVisible(false);
		});
	}

}
