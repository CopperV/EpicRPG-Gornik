package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs;

import org.bukkit.entity.Player;

public interface ICost {

	public String display();
	public boolean check(Player p);
	public void spend(Player p);
	
}
