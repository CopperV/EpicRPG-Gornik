package me.Vark123.EpicRPGGornik.CataclysmControllers;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl.BlindnessDebuff;
import me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl.DizzyDebuff;
import me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl.FatigueDebuff;
import me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl.GasDebuff;
import me.Vark123.EpicRPGGornik.CataclysmControllers.DebuffImpl.NoDebuff;
import me.Vark123.EpicRPGGornik.PlayerSystem.CataclysmMiner;
import me.Vark123.EpicRPGGornik.PlayerSystem.PlayerManager;

@Getter
public final class CatMineController {

	private static final CatMineController inst = new CatMineController();

	private BukkitTask mineController;
	private BukkitTask debuffController;
	
	private final Collection<ADebuff> debuffList;
	
	private CatMineController() {
		debuffList = new HashSet<>();
		debuffList.add(new BlindnessDebuff());
		debuffList.add(new DizzyDebuff());
		debuffList.add(new FatigueDebuff());
		debuffList.add(new GasDebuff());
		debuffList.add(new NoDebuff());
	}
	
	public static final CatMineController get() {
		return inst;
	}
	
	public void start() {
		if(mineController != null 
				&& !mineController.isCancelled())
			mineController.cancel();
		if(debuffController != null
				&& !debuffController.isCancelled())
			debuffController.cancel();
		
		mineController = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().parallelStream()
					.filter(p -> p.getWorld().getName().equals("F_RPG_Cave"))
					.forEach(p -> {
						if(p.getGameMode().equals(GameMode.CREATIVE)
								|| p.getGameMode().equals(GameMode.SPECTATOR)
								|| p.isOp())
							return;
						PlayerManager.get().getPlayer(p).ifPresentOrElse(miner -> {
							RpgPlayer rpg = me.Vark123.EpicRPG.Players
									.PlayerManager.getInstance().getRpgPlayer(p);
							if(rpg.getInfo().getLevel() < 50) {
								p.setHealth(0);
								return;
							}
							
							CataclysmMiner catMiner = miner.getCatMiner();
							catMiner.updateOxygenBar();
							if(!catMiner.isJoinDelay())
								return;
							catMiner.setRemainTime(catMiner.getRemainTime() - 1);
						}, () -> {
							p.setHealth(0);
						});
					});
			}
		}.runTaskTimerAsynchronously(Main.getInst(), 0, 20*1);
		
		debuffController = new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().parallelStream()
					.filter(p -> p.getWorld().getName().equals("F_RPG_Cave"))
					.forEach(p -> {
						if(p.getGameMode().equals(GameMode.CREATIVE)
								|| p.getGameMode().equals(GameMode.SPECTATOR)
								|| p.isOp())
							return;

						PlayerManager.get().getPlayer(p).ifPresent(miner -> {
							CataclysmMiner catMiner = miner.getCatMiner();
							if(!catMiner.isJoinDelay())
								return;
							
							ADebuff debuff = catMiner.getRandomDebuff();
							if(debuff == null)
								return;
							debuff.doAction(p);
						});
					});
			}
		}.runTaskTimerAsynchronously(Main.getInst(), 0, 20*15);
	}
	
	public void stop() {
		if(mineController != null 
				&& !mineController.isCancelled())
			mineController.cancel();
		if(debuffController != null
				&& !debuffController.isCancelled())
			debuffController.cancel();
	}
	
}
