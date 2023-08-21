package me.Vark123.EpicRPGGornik.OreSystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.bukkit.Location;

import lombok.Getter;

@Getter
public final class OreManager {

	private static final OreManager inst = new OreManager();
	
	private final Collection<AOre> ores;
	
	private OreManager() {
		ores = new HashSet<>();
	}
	
	public static final OreManager get() {
		return inst;
	}
	
	public void addOre(AOre ore) {
		if(ores.add(ore))
			ore.createOre();
		return;
	}
	
	public Optional<AOre> getOre(Location oreLocation) {
		return ores.stream()
				.filter(ore -> ore.getBlockLocation().equals(oreLocation))
				.findFirst();
	}
	
	public void clearOres() {
		ores.forEach(ore -> ore.destroyOre());
		ores.clear();
	}
	
}
