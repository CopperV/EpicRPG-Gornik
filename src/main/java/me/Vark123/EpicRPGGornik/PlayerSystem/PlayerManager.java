package me.Vark123.EpicRPGGornik.PlayerSystem;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRPGGornik.FileManager;

@Getter
public final class PlayerManager {

	private static final PlayerManager inst = new PlayerManager();
	
	private final Map<Player, PlayerMiner> miners;
	
	private PlayerManager() {
		miners = new ConcurrentHashMap<>();
	}
	
	public static final PlayerManager get() {
		return inst;
	}
	
	public PlayerMiner registerPlayer(Player p) {
		PlayerMiner miner = FileManager.loadPlayer(p);
		return miners.put(p, miner);
	}
	
	public Optional<PlayerMiner> unregisterPlayer(Player p) {
		if(miners.containsKey(p)) {
			PlayerMiner miner = miners.get(p);
			miner.endTasks();
			FileManager.savePlayer(miner);
		}
		return Optional.ofNullable(miners.get(p));
	}
	
	public Optional<PlayerMiner> getPlayer(Player p) {
		return Optional.ofNullable(miners.get(p));
	}
	
}
