package me.Vark123.EpicRPGGornik.PlayerSystem;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPGGornik.OreSystem.AOre;

@Getter
public class PlayerMiner {

	private Player player;
	
	@Setter
	private AOre miningOre;
	
	private CataclysmMiner catMiner;
	private TanalorrMiner tanMiner;

	public PlayerMiner(Player player, CataclysmMiner catMiner, TanalorrMiner tanMiner) {
		super();
		this.player = player;
		this.catMiner = catMiner;
		this.tanMiner = tanMiner;
		
		this.catMiner.setMiner(this);
		this.tanMiner.setMiner(this);
	}
	
	public void endTasks() {
		catMiner.endTasks();
	}
	
}
