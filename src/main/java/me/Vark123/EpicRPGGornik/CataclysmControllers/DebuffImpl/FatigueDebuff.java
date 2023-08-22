package me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.CataclysmControllers.ADebuff;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;

public class FatigueDebuff extends ADebuff {

	public FatigueDebuff() {
		super("fatigue", .15);
	}

	@Override
	public void doAction(Player p) {
		PlayerManager.get().getPlayer(p).ifPresent(miner -> {
			p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.3f);
			miner.getCatMiner()
				.setDebuffGathering(0.4 * (1-miner.getCatMiner().getFatigueEffect()));
			new BukkitRunnable() {
				
				@Override
				public void run() {
					miner.getCatMiner().setDebuffGathering(0);
				}
			}.runTaskLater(Main.getInst(), 20*14);
		});
	}

}
