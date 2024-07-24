package me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Impl;

import java.util.Collection;

import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.ATanalorrUpgrade;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.TanalorrUpgradeType;
import me.Vark123.EpicRPGGornik.Tanalorr.Upgrades.Costs.ICost;

public class StandardTanalorrUpgrade extends ATanalorrUpgrade {
	
	public StandardTanalorrUpgrade(String id, int level, TanalorrUpgradeType type, String display, String valueDisplay,
			double value, Collection<ICost> costs) {
		super(id, level, type, display, valueDisplay, value, costs);
	}

}
