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

	public PlayerMiner(Player player, CataclysmMiner catMiner) {
		super();
		this.player = player;
		this.catMiner = catMiner;
	}
	
	public void endTasks() {
		catMiner.endTasks();
	}
	
}
