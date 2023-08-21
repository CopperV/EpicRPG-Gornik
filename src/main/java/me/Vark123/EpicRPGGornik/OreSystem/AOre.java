package me.Vark123.EpicRPGGornik.OreSystem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class AOre {

	protected Material material;
	protected Location location;
	protected double durability;
	
	public abstract void createOre();
	public abstract void destroyOre();
	public abstract void markAsMined(Player p);
	
	public Location getBlockLocation() {
		return location.getBlock().getLocation();
	}
	
}
