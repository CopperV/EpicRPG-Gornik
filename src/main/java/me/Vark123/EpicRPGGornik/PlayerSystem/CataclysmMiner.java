package me.Vark123.EpicRPGGornik.PlayerSystem;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPGGornik.Main;

@Getter
public class CataclysmMiner {

	//TLEN
	@Setter
	private int maxTime;
	@Setter
	private int remainTime;
	
	private BukkitTask joinDelayTask;
	private boolean joinDelay;
	
	public CataclysmMiner(int maxTime, int remainTime) {
		super();
		this.maxTime = maxTime;
		this.remainTime = remainTime;
		
		joinDelayTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				joinDelay = true;
			}
		}.runTaskLater(Main.getInst(), 20*60);
	}
	
	public void endTasks() {
		joinDelayTask.cancel();
	}
	
}
