package me.Vark123.EpicRPGGornik.PlayerSystem;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPGGornik.Main;
import me.Vark123.EpicRPGGornik.Tanalorr.TanalorrConfig;

@Getter
public class TanalorrMiner {

	Player player;
	@Setter
	PlayerMiner miner;
	
	private int level;
	private int diggedOres;
	
	public TanalorrMiner(Player player, int level, int diggedOres) {
		super();
		this.player = player;
		this.level = level;
		this.diggedOres = diggedOres;
	}
	
	public void update() {
		if(level >= TanalorrConfig.get().getMaxPlayerLevel())
			return;
		
		++diggedOres;
		if(diggedOres < TanalorrConfig.get().getOreUpdateAmount())
			return;
		
		diggedOres = 0;
		++level;
		
		player.sendMessage("§7["+Main.getInst().getPrefix()+"§7] §eTwoje zdolnosci gornicze wzrastaja!");
		player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1.1f);
		player.spawnParticle(Particle.TOTEM, player.getLocation().add(0, 1, 0), 16, 0.6f, 0.6f, 0.6f, 0.15f);
	}
	
	public double getVariancy() {
		return (level + 10.) / 100.;
	}
	
}
