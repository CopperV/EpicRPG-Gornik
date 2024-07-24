package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.ATanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.ICost;

public class EdgeTanalorrUpgrade extends ATanalorrUpgrade {
	
	private Material m;
	
	public EdgeTanalorrUpgrade(String id, int level, TanalorrUpgradeType type, String display, String valueDisplay,
			double value, Collection<ICost> costs, Material m) {
		super(id, level, type, display, valueDisplay, value, costs);
		this.m = m;
	}

	@Override
	public ItemStack upgrade(Player p, ItemStack pickaxe) {
		ItemStack it = super.upgrade(p, pickaxe);
		if(it.equals(pickaxe))
			return it;
		
		it.setType(m);
		
		return it;
	}
	
	

}
